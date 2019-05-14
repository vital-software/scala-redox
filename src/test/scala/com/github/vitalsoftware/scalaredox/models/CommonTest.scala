package com.github.vitalsoftware.scalaredox.models

import org.specs2.matcher.Matcher
import org.specs2.mutable.Specification
import play.api.libs.json.{ JsError, JsResult, JsString, JsSuccess }

class CommonTest extends Specification {

  def beLanguageOf(l: String): Matcher[JsResult[Language]] = (s: JsResult[Language]) => {
    s must beAnInstanceOf[JsSuccess[Language]]
    s.get.toString mustEqual (l)
  }

  "Language" should {
    "read valid language locale" in {
      JsString("en").validate[Language] must beLanguageOf("eng")

      JsString("eng").validate[Language] must beLanguageOf("eng")

      JsString("fr").validate[Language] must beLanguageOf("fra")
    }

    "read 'Other' language locale" in {
      JsString("Other").validate[Language] must beLanguageOf("")
      JsString("Unknown").validate[Language] must beLanguageOf("")
    }

    "read invalid language locale" in {
      val unknown = JsString("invalid").validate[Language]
      unknown must beAnInstanceOf[JsError]
    }
  }
}
