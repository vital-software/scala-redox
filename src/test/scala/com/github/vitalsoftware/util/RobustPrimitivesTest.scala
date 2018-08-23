package com.github.vitalsoftware.util

import com.github.vitalsoftware.macros.jsonDefaults
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import play.api.libs.json._

@jsonDefaults case class Primitives(f1: Int = 1, f2: String = "2", f3: Boolean = true, f4: Option[Test])

class RobustPrimitivesTest extends Specification {

  trait TestScope extends RobustPrimitives with Scope

  "RobustPrimitives" should {
    "read as string" in new TestScope {
      Reads.of[String].reads(JsBoolean(true)) mustEqual (JsSuccess("true"))
      Reads.of[String].reads(JsBoolean(false)) mustEqual (JsSuccess("false"))
      Reads.of[String].reads(JsNumber(123)) mustEqual (JsSuccess("123"))
      Reads.of[String].reads(JsNumber(123.456)) mustEqual (JsSuccess("123.456"))

      //      Reads.of[String].reads(Json.arr(1, 2, 3)) mustEqual (JsSuccess("123.456"))
    }

  }
}
