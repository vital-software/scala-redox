package com.github.vitalsoftware.util

import play.api.libs.json._

object RobustParsing {
  // Todo: Make this tail recursive
  //  @scala.annotation.tailrec
  def robustParsing[A](reads: Reads[A], json: JsValue, seen: Seq[JsPath] = Nil): (Option[JsError], Option[A]) = json.validate(reads)
    .fold(
      invalid = { errors =>
        val paths = errors.map(_._1)

        val sanitizedPaths = paths.intersect(seen).map(p => p.path.splitAt(p.path.length - 2)).map {
          // if the last node is an element in an array, remove the entire array
          case (beforeArray, IdxPathNode(_) :: _) =>
            beforeArray
          // otherwise drop the last node in path
          case (before, after) => (before ::: after).dropRight(1)
        }
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
