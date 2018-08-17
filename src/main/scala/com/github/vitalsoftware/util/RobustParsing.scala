package com.github.vitalsoftware.util

import play.api.libs.json._

object RobustParsing {
  // Todo: Make this tail recursive
  //  @scala.annotation.tailrec
  def robustParsing[A](reads: Reads[A], json: JsValue, seen: Seq[JsPath] = Nil): (Option[JsError], Option[A]) = json.validate(reads)
    .fold(
      invalid = { errors =>
        val paths = errors.map(_._1)

        // if there are intersects with arrays, we remove the entire array so that if the array has a default, we can
        // fall back to that. see test "recover on arrays with default values"
        val sanitizedPaths = paths.intersect(seen)
          .filter(_.path.exists(_.isInstanceOf[IdxPathNode]))
          .map(p => p.path.splitAt(p.path.lastIndexWhere(_.isInstanceOf[IdxPathNode]))._1)
          .filter(_.nonEmpty)
          .map(JsPath.apply)
        val unseen = paths.diff(seen)
        val pathsToPrune = sanitizedPaths ++ unseen

        if (pathsToPrune.nonEmpty) {
          val transforms = pathsToPrune
            .map(prune)
            .reduce(_ andThen _)

          val pruned = json.transform(transforms)

          RobustParsing.robustParsing(reads, pruned.get, seen ++ pathsToPrune) match {
            case (_, maybeResult) => (Some(JsError(errors)), maybeResult)
          }
        } else {
          (Some(JsError(errors)), None)
        }
      },
      valid = res => (None, Some(res))
    )

  def prune(path: JsPath): Reads[_ >: JsObject <: JsValue] = {
    val index = path.path.indexWhere(_.isInstanceOf[IdxPathNode])
    val splitIndex = if (index == -1) Int.MaxValue else index
    path.path.splitAt(splitIndex) match {
      // primitive array, just return the value
      case (Nil, Nil)                       => path.json.pick
      // no arrays, safe to prune
      case (path, Nil)                      => JsPath(path).json.prune
      // encountered an array JsValue, prune each element
      case (path, IdxPathNode(idx) :: tail) => prunePathInArray(JsPath(path), idx, JsPath(tail))
      // unhandled, just return the value
      case _                                => path.json.pick
    }
  }

  def prunePathInArray(toArrayElement: JsPath, index: Int, afterArray: JsPath): Reads[_ >: JsObject <: JsValue] = toArrayElement.path match {
    case Nil => Reads.of[JsArray].map { case JsArray(arr) => JsArray(arr.updated(index, arr(index).transform(prune(afterArray)).get)) }
    case _ => toArrayElement.json.update(
      Reads.of[JsArray].map { case JsArray(arr) => JsArray(arr.updated(index, arr(index).transform(prune(afterArray)).get)) }
    )
  }
}
