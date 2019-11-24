package com.github.vitalsoftware.util

import play.api.libs.json._

import scala.util.Try

trait RobustPrimitives {
  private def alternative(partial: PartialFunction[JsValue, JsValue])(json: JsValue) =
    JsSuccess((partial.applyOrElse(json, identity[JsValue])))

  private val alternativeStringReads: Reads[JsValue] = new Reads[JsValue] {
    override def reads(json: JsValue): JsResult[JsValue] =
      alternative {
        case JsNumber(n)  => JsString(n.toString)
        case JsBoolean(b) => JsString(b.toString)
      }(json)
  }

  private val alternativeNumberReads: Reads[JsValue] = new Reads[JsValue] {
    override def reads(json: JsValue): JsResult[JsValue] =
      alternative {
        case JsString(s) => Try(BigDecimal(s.trim)).map(JsNumber).getOrElse(JsString(s))
      }(json)
  }

  private val alternativeBooleanReads: Reads[JsValue] = new Reads[JsValue] {
    override def reads(json: JsValue): JsResult[JsValue] =
      alternative {
        case JsString(s) =>
          s.trim.toLowerCase match {
            case s if s == "true" || s == "t" || s == "1"  => JsBoolean(true)
            case s if s == "false" || s == "f" || s == "0" => JsBoolean(false)
            case _                                         => JsString(s)
          }
        case JsNumber(n) if n == 0 => JsBoolean(false)
        case JsNumber(n) if n == 1 => JsBoolean(true)
      }(json)
  }

  implicit val RobustStringReads: Reads[String] = alternativeStringReads andThen Reads.StringReads
  implicit val RobustIntReads: Reads[Int] = alternativeNumberReads andThen Reads.IntReads
  implicit val RobustShortReads: Reads[Short] = alternativeNumberReads andThen Reads.ShortReads
  implicit val RobustByteReads: Reads[Byte] = alternativeNumberReads andThen Reads.ByteReads
  implicit val RobustLongReads: Reads[Long] = alternativeNumberReads andThen Reads.LongReads
  implicit val RobustFloatReads: Reads[Float] = alternativeNumberReads andThen Reads.FloatReads
  implicit val RobustDoubleReads: Reads[Double] = alternativeNumberReads andThen Reads.DoubleReads
  implicit val RobustBooleanReads: Reads[Boolean] = alternativeBooleanReads andThen Reads.BooleanReads
}
