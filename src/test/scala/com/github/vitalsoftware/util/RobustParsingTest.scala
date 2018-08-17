package com.github.vitalsoftware.util

import com.github.vitalsoftware.macros.{ json, jsonDefaults }
import org.specs2.mutable.Specification
import play.api.libs.json._

@json case class Person(name: String, age: Int, gender: Option[String])
@jsonDefaults case class Person2(name: String, age: Int = 7, gender: Option[String] = None)
@jsonDefaults case class Test(f1: Int = 1, f2: String = "2", f3: Boolean = true, f4: Option[Test])
@jsonDefaults case class TestWithNoFallback(f1: String, f2: Int = 99)
@jsonDefaults case class Container(data: List[TestWithNoFallback] = List(TestWithNoFallback("test", 100)))

class RobustParsingTest extends Specification {

  "robustParsing" should {
    "make invalid option values None" in {
      val json = Json.obj(
        "name" -> "Victor Hugo",
        "age" -> 46,
        "gender" -> true
      )

      val (errors, result) = RobustParsing.robustParsing(Person.jsonAnnotationFormat.reads, json)
      result must beSome(Person("Victor Hugo", 46, None))
      errors must beSome[JsError]

      val (errors2, result2) = RobustParsing.robustParsing(Person2.jsonAnnotationFormat.reads, json)
      result2 must beSome(Person2("Victor Hugo", 46))
      errors2 must beSome[JsError]
    }

    "make invalid values with defaults fallback to the default" in {
      val json = Json.obj(
        "name" -> "Victor Hugo",
        "age" -> "non age"
      )

      val (errors, result) = RobustParsing.robustParsing(Person2.jsonAnnotationFormat.reads, json)
      result must beSome(Person2("Victor Hugo", 7))
      errors must beSome[JsError]
    }

    "throw on invalid values which are not optional or default" in {
      val json = Json.obj(
        "name" -> "Victor Hugo",
        "age" -> "non age"
      )

      val (errors, result) = RobustParsing.robustParsing(Person.jsonAnnotationFormat.reads, json)

      result must beNone
      errors must beSome[JsError]
      errors.get.errors.head._1.path.head.asInstanceOf[KeyPathNode].key mustEqual ("age")
      errors.get.errors.head._2.head.message must contain("error.expected.jsnumber")
    }

    "multiple defaults must get replaced" in {
      val json = Json.obj("f1" -> "str", "f2" -> false, "f3" -> 3, "f4" -> "not test")
      val (errors, result) = RobustParsing.robustParsing(Test.jsonAnnotationFormat, json)

      result must beSome(Test(f4 = None))
      errors must beSome[JsError]
      errors.get.errors.map(_._1.toJsonString) must contain(allOf("obj.f1", "obj.f2", "obj.f3", "obj.f4"))
    }

    "Replace deep nested values" in {
      val json = Json.obj("f4" -> Json.obj("f3" -> "not boolean"))
      val (errors, result) = RobustParsing.robustParsing(Test.jsonAnnotationFormat, json)

      result must beSome(Test(f4 = Some(Test(f4 = None))))
      errors must beSome[JsError]
      errors.get.errors.map(_._1.toJsonString) must contain(allOf("obj.f4.f3"))
    }

    "recursively fix errors on arrays" in {

      val json = Json.arr(
        Json.obj("f1" -> "arr 1", "f2" -> "a"), // fail on f2 as its a string
        Json.obj("f1" -> "arr 2", "f2" -> 2),
        Json.obj("f1" -> "arr 3", "f2" -> "c"),
        Json.obj("f1" -> "arr 4", "f2" -> "d"),
        Json.obj("f1" -> "arr 5", "f2" -> 5)
      )

      val (errors, result) = RobustParsing.robustParsing(implicitly[Reads[List[TestWithNoFallback]]], json)
      errors must beSome[JsError]
      result must beSome(List(TestWithNoFallback("arr 1", 99), TestWithNoFallback("arr 2", 2), TestWithNoFallback("arr 3", 99), TestWithNoFallback("arr 4", 99), TestWithNoFallback("arr 5", 5)))
      errors.get.errors.map(_._1.toJsonString) must contain(allOf("obj[0].f2", "obj[2].f2", "obj[3].f2"))
    }

    "recursively fix errors on arrays in containers" in {

      val json = Json.obj(
        "data" -> Json.arr(
          Json.obj("f1" -> "arr 1", "f2" -> "a"), // fail on f2 as its a string
          Json.obj("f1" -> "arr 2", "f2" -> 2),
          Json.obj("f1" -> "arr 3", "f2" -> "c"),
          Json.obj("f1" -> "arr 4", "f2" -> "d"),
          Json.obj("f1" -> "arr 5", "f2" -> 5)
        )
      )

      val (errors, result) = RobustParsing.robustParsing(implicitly[Reads[Container]], json)
      errors must beSome[JsError]
      result must beSome(Container(List(TestWithNoFallback("arr 1", 99), TestWithNoFallback("arr 2", 2), TestWithNoFallback("arr 3", 99), TestWithNoFallback("arr 4", 99), TestWithNoFallback("arr 5", 5))))
      errors.get.errors.map(_._1.toJsonString) must contain(allOf("obj.data[0].f2", "obj.data[2].f2", "obj.data[3].f2"))
    }

    "fail on arrays un-recoverable failures in arrays" in {

      val json = Json.arr(
        Json.obj("f1" -> "arr 1", "f2" -> "a"),
        Json.obj("f1" -> 234, "f2" -> 2), // fail on f1 as its a string
        Json.obj("f1" -> "arr 3", "f2" -> "c"),
        Json.obj("f1" -> "arr 4", "f2" -> "d"),
        Json.obj("f1" -> "arr 5", "f2" -> 5)
      )

      val (errors, result) = RobustParsing.robustParsing(implicitly[Reads[List[TestWithNoFallback]]], json)
      errors must beSome[JsError]
      result must beNone
    }

    "recover on arrays with default values" in {

      val json = Json.obj("data" -> Json.arr(
        Json.obj("f1" -> "arr 1", "f2" -> "a"),
        Json.obj("f1" -> 234, "f2" -> 2), // fail on f1 as its a string
        Json.obj("f1" -> "arr 3", "f2" -> "c"),
      ))

      val (errors, result) = RobustParsing.robustParsing(implicitly[Reads[Container]], json)
      errors must beSome[JsError]
      result must beSome(Container(List(TestWithNoFallback("test", 100))))
    }

    "fail on arrays of primitives" in {
      val json = Json.arr(1, "a", 2, "b")

      val (errors, result) = RobustParsing.robustParsing(Reads.list[Int], json)
      errors must beSome[JsError]
      result must beNone
    }
  }
}
