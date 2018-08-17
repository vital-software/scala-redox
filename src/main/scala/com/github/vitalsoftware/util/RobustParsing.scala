package com.github.vitalsoftware.util

import play.api.libs.json._

object RobustParsing {
  // Todo: Make this tail recursive
  //  @scala.annotation.tailrec
  def robustParsing[A](reads: Reads[A], json: JsValue, seen: Seq[JsPath] = Nil): (Option[JsError], Option[A]) = json.validate(reads)
    .fold(
      invalid = { errors =>
        val paths = errors.map(_._1)

        // if we haven't seen these paths before prune and process
        if (paths.intersect(seen).isEmpty) {
          // if paths contain arrays we need to

          val transforms = paths
            .map(prune _)
            .reduce(_ andThen _)

          val pruned = json.transform(transforms)

          RobustParsing.robustParsing(reads, pruned.get, paths) match {
            case (_, maybeResult) => (Some(JsError(errors)), maybeResult)
          }
        } else {
          val intersectsWithArrays = paths.intersect(seen)
            .filter(_.path.exists(_.isInstanceOf[IdxPathNode]))
          val sanitizedPaths = intersectsWithArrays
            .map(p => p.path.splitAt(p.path.indexWhere(_.isInstanceOf[IdxPathNode]))._1)
            .filter(_.nonEmpty)
            .map(JsPath.apply _) ++ paths.diff(seen)
          if (sanitizedPaths.nonEmpty) {
            val transforms = sanitizedPaths
              .map(prune _)
              .reduce(_ andThen _)
            val pruned = json.transform(transforms)
            RobustParsing.robustParsing(reads, pruned.get, paths) match {
              case (_, maybeResult) => (Some(JsError(errors)), maybeResult)
            }
          } else {
            (Some(JsError(errors)), None)
          }
        }
      },
      valid = res => (None, Some(res))
    )

  def prune(path: JsPath): Reads[_ >: JsObject <: JsValue] = {
    val index = path.path.indexWhere(_.isInstanceOf[IdxPathNode])
    val splitIndex = if (index == -1) Int.MaxValue else index
    path.path.splitAt(splitIndex) match {
      case (Nil, Nil)                       => path.json.pick
      case (Nil, IdxPathNode(idx) :: tail)  => prunePathInArray(JsPath(Nil), idx, JsPath(tail))
      case (path, Nil)                      => JsPath(path).json.prune
      case (path, IdxPathNode(idx) :: tail) => prunePathInArray(JsPath(path), idx, JsPath(tail))
      case _                                => path.json.pick
    }
  }

  def prunePathInArray(toArrayElement: JsPath, index: Int, afterArray: JsPath): Reads[_ >: JsObject <: JsValue] = (toArrayElement.path, afterArray.path) match {
    case (Nil, Nil) => Reads.of[JsArray].map { case JsArray(arr) => JsArray(arr) }
    case (_, Nil) => toArrayElement.json.update {
      Reads.of[JsArray].map { case JsArray(arr) => JsArray(arr) }
    }
    case (Nil, _) => Reads.of[JsArray].map { case JsArray(arr) => JsArray(arr.updated(index, arr(index).transform(prune(afterArray)).get)) }
    case _ => toArrayElement.json.update(
      Reads.of[JsArray].map { case JsArray(arr) => JsArray(arr.updated(index, arr(index).transform(prune(afterArray)).get)) }
    )
  }
}
