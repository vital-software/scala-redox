package com.github.vitalsoftware.util

import org.joda.time.format.DateTimeFormatter
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.language.implicitConversions
import scala.language.postfixOps
import scala.util.Try

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

object JsonImplicits {
  implicit val jodaISODateReads: Reads[org.joda.time.DateTime] = new Reads[org.joda.time.DateTime] {
    import org.joda.time.DateTime

    val df: DateTimeFormatter = org.joda.time.format.ISODateTimeFormat.dateTime()
    val backup: DateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd")

    def reads(json: JsValue): JsResult[DateTime] = json match {
      case JsNumber(d) => JsSuccess(new DateTime(d.toLong))
      case JsString(s) => parseDate(s) match {
        case Some(d) => JsSuccess(d)
        case None => JsError(Seq(JsPath() -> Seq(JsonValidationError("validate.error.expected.date.isoformat", "ISO8601"))))
      }
      case _ => JsError(Seq(JsPath() -> Seq(JsonValidationError("validate.error.expected.date"))))
    }

    private def parseDate(input: String): Option[DateTime] = {
      Try(DateTime.parse(input, df)).orElse(Try(DateTime.parse(input, backup))).toOption
    }

  }

  implicit val jodaISODateWrites: Writes[org.joda.time.DateTime] = new Writes[org.joda.time.DateTime] {
    def writes(d: org.joda.time.DateTime): JsValue = JsString(d.toString())
  }

  implicit val jodaISO8601Format = Format(jodaISODateReads, jodaISODateWrites)

  // Will read ISO8061 and "yyyy-MM-dd" format
  implicit val jodaDateFormat = Format(jodaISODateReads, Writes.jodaDateWrites("yyyy-MM-dd"))
}