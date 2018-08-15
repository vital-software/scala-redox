package com.github.vitalsoftware.scalaredox.client

import com.github.vitalsoftware.macros.{ json, jsonDefaults }
import org.specs2.mutable.Specification
import play.api.libs.json._

@json case class Person(name: String, age: Int, gender: Option[String])
@jsonDefaults case class Person2(name: String, age: Int = 7, gender: Option[String] = None)
@jsonDefaults case class Test(f1: Int = 1, f2: String = "2", f3: Boolean = true, f4: Option[Test])

class RedoxClientTest extends Specification {

  "robustParsing" should {
    "make invalid option values None" in {
      val json = Json.obj(
        "name" -> "Victor Hugo",
        "age" -> 46,
        "gender" -> true
      )

      val (errors, result) = RedoxClient.robustParsing(Person.jsonAnnotationFormat.reads, json)
      result must beSome(Person("Victor Hugo", 46, None))
      errors must beSome[JsError]

      val (errors2, result2) = RedoxClient.robustParsing(Person2.jsonAnnotationFormat.reads, json)
      result2 must beSome(Person2("Victor Hugo", 46))
      errors2 must beSome[JsError]
    }

    "make invalid values with defaults fallback to the default" in {
      val json = Json.obj(
        "name" -> "Victor Hugo",
        "age" -> "non age"
      )

      val (errors, result) = RedoxClient.robustParsing(Person2.jsonAnnotationFormat.reads, json)
      result must beSome(Person2("Victor Hugo", 7))
      errors must beSome[JsError]
    }

    "throw on invalid values which are not optional or default" in {
      val json = Json.obj(
        "name" -> "Victor Hugo",
        "age" -> "non age"
      )

      val (errors, result) = RedoxClient.robustParsing(Person.jsonAnnotationFormat.reads, json)

      result must beNone
      errors must beSome[JsError]
      errors.get.errors.head._1.path.head.asInstanceOf[KeyPathNode].key mustEqual ("age")
      errors.get.errors.head._2.head.message must contain("error.expected.jsnumber")
    }

    "multiple defaults must get replaced" in {
      val json = Json.obj("f1" -> "str", "f2" -> false, "f3" -> 3, "f4" -> "not test")
      val (errors, result) = RedoxClient.robustParsing(Test.jsonAnnotationFormat, json)

      result must beSome(Test(f4 = None))
      errors must beSome[JsError]
      errors.get.errors.map(_._1.toJsonString) must contain(allOf("obj.f1", "obj.f2", "obj.f3", "obj.f4"))
    }

    "Replace deep nested values" in {
      val json = Json.obj("f4" -> Json.obj("f3" -> "not boolean"))
      val (errors, result) = RedoxClient.robustParsing(Test.jsonAnnotationFormat, json)

      result must beSome(Test(f4 = Some(Test(f4 = None))))
      errors must beSome[JsError]
      errors.get.errors.map(_._1.toJsonString) must contain(allOf("obj.f4.f3"))
    }
  }
}
