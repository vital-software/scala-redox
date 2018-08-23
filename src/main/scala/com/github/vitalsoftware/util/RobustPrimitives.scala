package com.github.vitalsoftware.util

import play.api.libs.json._

trait RobustPrimitives {

  private def alternative(partial: PartialFunction[JsValue, JsValue])(json: JsValue) = JsSuccess((partial.applyOrElse(json, (i: JsValue) => i)))

  private val alternativeStringReads: Reads[JsValue] = new Reads[JsValue] {
    override def reads(json: JsValue): JsResult[JsValue] = alternative {
      case JsNumber(n)  => JsString(n.toString)
      case JsBoolean(b) => JsString(b.toString)
    }(json)
  }

  private val alternativeNumberReads: Reads[JsValue] = new Reads[JsValue] {
    override def reads(json: JsValue): JsResult[JsValue] = alternative {
      case JsString(s) => JsNumber(BigDecimal(s))
    }(json)
  }

  implicit val RobustStringReads: Reads[String] = alternativeStringReads andThen Reads.StringReads
  implicit val RobustIntReads: Reads[Int] = alternativeNumberReads andThen Reads.IntReads
  implicit val RobustShortReads: Reads[Short] = alternativeNumberReads andThen Reads.ShortReads
  implicit val RobustByteReads: Reads[Byte] = alternativeNumberReads andThen Reads.ByteReads
  implicit val RobustLongReads: Reads[Long] = alternativeNumberReads andThen Reads.LongReads
  implicit val RobustFloatReads: Reads[Float] = alternativeNumberReads andThen Reads.FloatReads
  implicit val RobustDoubleReads: Reads[Double] = alternativeNumberReads andThen Reads.DoubleReads
}
