package com.github.vitalsoftware.util

import com.github.vitalsoftware.util.JsonImplicits.JsValueExtensions
import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification
import play.api.libs.json._

class JsonOpsTest extends Specification {
  def reduceEmpty(value: String, expected: String): MatchResult[String] =
    Json.parse(value).reduceEmptySubtrees.toString must beEqualTo(Json.parse(expected).toString)

  def reduceNull(value: String, expected: String): MatchResult[String] =
    Json.parse(value).reduceNullSubtrees.toString must beEqualTo(Json.parse(expected).toString)

  "JsonOps" should {
    "JsValue.reduceNullSubtrees" in {
      "null literal" in reduceNull("null", "null")
      "empty object" in reduceNull("{}", "null")
      "empty array" in reduceNull("[]", "[]")
      "array of arrays" in reduceNull("[[], []]", "[[], []]")
      "mixed empty array" in reduceNull("[null, []]", "[[]]")

      "object with an empty array property and one non-empty" in reduceNull(
        """
          |{
          |  "someEmpty": {
          |    "emptyInner": {
          |      "innerInner": null
          |    },
          |    "emptyArrayInner": [null]
          |  },
          |  "someDefined": "value",
          |  "someEmptyArray": [[], {"emptyInner": null}, null]
          |}
        """.stripMargin,
        """
          |{
          |  "someEmpty": {
          |    "emptyInner": null,
          |    "emptyArrayInner": []
          |  },
          |  "someDefined": "value",
          |  "someEmptyArray": [[]]
          |}
        """.stripMargin
      )
    }

    "JsValue.reduceEmptySubtrees" in {
      "null literal" in reduceEmpty("null", "null")
      "empty object" in reduceEmpty("{}", "null")
      "empty array" in reduceEmpty("[]", "[]")
      "array of arrays" in reduceEmpty("[[], []]", "[]")
      "mixed empty array" in reduceEmpty("[null, []]", "[]")

      "object with only empty values" in reduceEmpty(
        """
          |{
          |  "allEmpty": null
          |}
        """.stripMargin,
        "null"
      )

      "object with one non-empty property" in reduceEmpty(
        """
          |{
          |  "someEmpty": {
          |    "emptyInner": null
          |  },
          |  "someDefined": "value"
          |}
        """.stripMargin,
        """
          |{
          |  "someEmpty": null,
          |  "someDefined": "value"
          |}
        """.stripMargin
      )

      "object with an empty array property" in reduceEmpty(
        """
          |{
          |  "someEmpty": {
          |    "emptyInner": null,
          |    "emptyArrayInner": []
          |  },
          |  "someEmptyArray": []
          |}
        """.stripMargin,
        "null"
      )

      "object with an empty array property and one non-empty" in reduceEmpty(
        """
          |{
          |  "someEmpty": {
          |    "emptyInner": {
          |      "innerInner": null
          |    },
          |    "emptyArrayInner": [null]
          |  },
          |  "someDefined": "value",
          |  "someEmptyArray": [[], {"emptyInner": null}, null]
          |}
        """.stripMargin,
        """
          |{
          |  "someEmpty": null,
          |  "someDefined": "value",
          |  "someEmptyArray": []
          |}
        """.stripMargin
      )
    }
  }
}
