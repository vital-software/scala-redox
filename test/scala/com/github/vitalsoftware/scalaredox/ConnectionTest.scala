package com.github.vitalsoftware.scalaredox

import org.specs2.mutable.Specification
import play.api.libs.json._

@json case class Person(name: String, age: Int)
@jsonDefaults case class Person2(name: String, age: Int = 7)

class ConnectionTest extends Specification {

  "authorize" should {

    "return a token" in {
      true must be true
    }
  }

  "refresh token" should {

    "obtain a token" in {
      true must be true
    }

    "obtain a new refresh token" in {
      true must be true
    }
  }
}
