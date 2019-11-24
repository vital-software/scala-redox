package com.github.vitalsoftware.util

import com.github.vitalsoftware.macros.jsonDefaults
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import play.api.libs.json._

@jsonDefaults case class Primitives(f1: Int, f2: String, f3: Boolean, f4: Option[Int])
object Primitives extends RobustPrimitives

class RobustPrimitivesTest extends Specification {
  trait TestScope extends RobustPrimitives with Scope

  "RobustPrimitives" should {
    "read as string" in new TestScope {
      Reads.of[String].reads(JsBoolean(true)) mustEqual (JsSuccess("true"))
      Reads.of[String].reads(JsBoolean(false)) mustEqual (JsSuccess("false"))
      Reads.of[String].reads(JsNumber(123)) mustEqual (JsSuccess("123"))
      Reads.of[String].reads(JsNumber(123.456)) mustEqual (JsSuccess("123.456"))
      Reads.of[String].reads(JsString("test")) mustEqual (JsSuccess("test"))

      val invalid = Reads.of[String].reads(Json.arr(1, 2, 3))
      invalid must beAnInstanceOf[JsError]
      invalid.asInstanceOf[JsError].errors.flatMap(_._2.map(_.message)) must contain("error.expected.jsstring")
    }

    "read as number" in new TestScope {
      Reads.of[Int].reads(JsString("123")) mustEqual (JsSuccess(123))
      Reads.of[Short].reads(JsString("123")) mustEqual (JsSuccess(123.toShort))
      Reads.of[Long].reads(JsString("123")) mustEqual (JsSuccess(123L))
      Reads.of[Float].reads(JsString("123.456")) mustEqual (JsSuccess(123.456f))
      Reads.of[Double].reads(JsString("123.456")) mustEqual (JsSuccess(123.456d))
      Reads.of[Byte].reads(JsString("4")) mustEqual (JsSuccess(4.toByte))
      Reads.of[Int].reads(JsNumber(123)) mustEqual (JsSuccess(123))

      val invalidBoolean = Reads.of[Int].reads(JsBoolean(true))
      invalidBoolean must beAnInstanceOf[JsError]
      invalidBoolean.asInstanceOf[JsError].errors.flatMap(_._2.map(_.message)) must contain("error.expected.jsnumber")

      val invalidString = Reads.of[Int].reads(JsString("123L"))
      invalidString must beAnInstanceOf[JsError]
      invalidString.asInstanceOf[JsError].errors.flatMap(_._2.map(_.message)) must contain("error.expected.jsnumber")
    }

    "read as boolean" in new TestScope {
      Reads.of[Boolean].reads(JsString("true")) mustEqual (JsSuccess(true))
      Reads.of[Boolean].reads(JsString("t")) mustEqual (JsSuccess(true))
      Reads.of[Boolean].reads(JsString("1")) mustEqual (JsSuccess(true))
      Reads.of[Boolean].reads(JsString("false")) mustEqual (JsSuccess(false))
      Reads.of[Boolean].reads(JsString("f")) mustEqual (JsSuccess(false))
      Reads.of[Boolean].reads(JsString("0")) mustEqual (JsSuccess(false))

      val invalidString = Reads.of[Boolean].reads(JsString("2"))
      invalidString must beAnInstanceOf[JsError]
      invalidString.asInstanceOf[JsError].errors.flatMap(_._2.map(_.message)) must contain("error.expected.jsboolean")

      val invalidInt = Reads.of[Boolean].reads(JsNumber(2))
      invalidInt must beAnInstanceOf[JsError]
      invalidInt.asInstanceOf[JsError].errors.flatMap(_._2.map(_.message)) must contain("error.expected.jsboolean")
    }

    "read object with primitives" in {
      val json = Json.obj("f1" -> "123", "f2" -> 123, "f3" -> "true", "f4" -> "456")
      val result = Reads.of[Primitives].reads(json)
      result mustEqual (JsSuccess(Primitives(123, "123", true, Some(456))))
    }

    "fail on object with unparsable primitives" in {
      val json = Json.obj("f1" -> "a", "f2" -> 123, "f3" -> "true")
      val result = Reads.of[Primitives].reads(json)
      result must beAnInstanceOf[JsError]
      result.asInstanceOf[JsError].errors.flatMap(_._2.map(_.message)) must contain("error.expected.jsnumber")
    }
  }
}
