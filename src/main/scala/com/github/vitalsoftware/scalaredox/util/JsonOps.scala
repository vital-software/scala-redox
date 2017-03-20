package com.github.vitalsoftware.scalaredox.util

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import scala.language.implicitConversions
import scala.language.postfixOps

/**
  * Alter operations available on Play-JSON Reads[T] & Writes[T]
  *
  * Inspired by http://kailuowang.blogspot.co.nz/2013/11/addremove-fields-to-plays-default-case.html
  *
  * Created by apatzer on 3/17/17.
  */
class OWritesOps[A](writes: OWrites[A]) {
  def addField[T: Writes](fieldName: String, field: A => T): OWrites[A] =
    (writes ~ (__ \ fieldName).write[T])((a: A) => (a, field(a)))

  def removeField(fieldName: String): OWrites[A] = OWrites { a: A =>
    val transformer = (__ \ fieldName).json.prune
    Json.toJson(a)(writes).validate(transformer).get
  }

  def removeFields(fieldName: String*): OWrites[A] = OWrites { a: A =>
    val transformer = fieldName.tail.foldLeft((__ \ fieldName.head).json.prune){ case (z, field) =>
      (__ \ field).json.prune and z reduce }
    Json.toJson(a)(writes).validate(transformer).get
  }
}

object OWritesOps {
  implicit def from[A](writes: OWrites[A]): OWritesOps[A] = new OWritesOps(writes)
}

class OReadsOps[A](reads: Reads[A]) {
  def validate(set: Set[A]): Reads[A] =
    reads.filter(JsonValidationError("set.notFoundIn", set)){ a: A => set.contains(a) }
}

object OReadsOps {
  implicit def from[A](reads: Reads[A]): OReadsOps[A] = new OReadsOps(reads)
}