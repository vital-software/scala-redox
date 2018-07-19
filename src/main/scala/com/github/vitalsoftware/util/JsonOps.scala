package com.github.vitalsoftware.util

import org.joda.time.DateTimeZone
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
    val transformer = fieldName.tail.foldLeft((__ \ fieldName.head).json.prune) {
      case (z, field) =>
        (__ \ field).json.prune and z reduce
    }
    Json.toJson(a)(writes).validate(transformer).get
  }
}

object OWritesOps {
  implicit def from[A](writes: OWrites[A]): OWritesOps[A] = new OWritesOps(writes)
}

class OReadsOps[A](reads: Reads[A]) {
  def validate(set: Set[A]): Reads[A] =
    reads.filter(JsonValidationError("set.notFoundIn", set)) { a: A => set.contains(a) }
}

object OReadsOps {
  implicit def from[A](reads: Reads[A]): OReadsOps[A] = new OReadsOps(reads)
}

trait JsonImplicits {
  implicit val jodaISODateReads: Reads[org.joda.time.DateTime] = new Reads[org.joda.time.DateTime] {
    import org.joda.time.DateTime

    val isoFormatter: DateTimeFormatter = org.joda.time.format.ISODateTimeFormat.dateTime()
    val dateDashFormatter: DateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd").withZone(DateTimeZone.UTC)
    val dateNoDashFormatter: DateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern("yyyyMMdd").withZone(DateTimeZone.UTC)

    def reads(json: JsValue): JsResult[DateTime] = json match {
      case JsNumber(d) => JsSuccess(new DateTime(d.toLong))
      case JsString(s) => parseDate(s) match {
        case Some(d) => JsSuccess(d)
        case None    => JsError(Seq(JsPath() -> Seq(JsonValidationError("validate.error.expected.date.isoformat", "ISO8601"))))
      }
      case _ => JsError(Seq(JsPath() -> Seq(JsonValidationError("validate.error.expected.date"))))
    }

    private def parseDate(input: String): Option[DateTime] = {
      Try(DateTime.parse(input, isoFormatter))
        .orElse(Try(DateTime.parse(input, dateDashFormatter)))
        .orElse(Try(DateTime.parse(input, dateNoDashFormatter)))
        .toOption
    }
  }

  implicit val jodaISODateWrites: Writes[org.joda.time.DateTime] = new Writes[org.joda.time.DateTime] {
    def writes(d: org.joda.time.DateTime): JsValue = JsString(d.toString())
  }

  implicit val jodaISO8601Format = Format(jodaISODateReads, jodaISODateWrites)

  // Will read ISO8061 and "yyyy-MM-dd" format
  implicit val jodaLocalDateFormat = Format(JodaReads.DefaultJodaLocalDateReads, JodaWrites.DefaultJodaLocalDateWrites)

  implicit class JsValueExtensions(jsValue: JsValue) {
    def reduceNullSubtrees: JsValue = reduceNullSubtreesImpl(jsValue)
    def reduceEmptySubtrees: JsValue = reduceEmptySubtreesImpl(jsValue)

    private def reduceNullSubtreesImpl(jv: JsValue): JsValue = {
      jv match {
        case JsObject(o) =>
          if (o.valuesIterator.forall(_ == JsNull)) {
            JsNull
          } else {
            JsObject(o.map { case (s, childVal) => s -> reduceNullSubtreesImpl(childVal) })
          }
        case JsArray(ts) =>
          JsArray(ts.map(reduceNullSubtreesImpl).filter(_ match {
            case JsNull => false
            case _      => true
          }))
        case _ =>
          jv
      }
    }

    private def reduceEmptySubtreesImpl(jv: JsValue): JsValue = {
      jv match {
        case JsObject(o) => if (isEmpty(jv)) JsNull else JsObject(o.mapValues(reduceEmptySubtreesImpl))
        case JsArray(a)  => if (isEmpty(jv)) JsArray.empty else JsArray(a.map(reduceEmptySubtreesImpl))
        case _           => jv
      }
    }

    private def isEmpty(jv: JsValue): Boolean = jv match {
      case JsNull      => true
      case JsArray(a)  => a.forall(isEmpty)
      case JsObject(o) => o.valuesIterator.forall(isEmpty)
      case _           => false
    }
  }
}

object JsonImplicits extends JsonImplicits

/** Defines a fall black for enumeration value when it fails to parse */
trait HasDefault { this: Enumeration =>
  def defaultValue: Value
}

trait LowPriorityBaseEnumReads { self: Enumeration =>
  implicit lazy val baseEnumReads: Reads[self.Value] = Reads.enumNameReads(self).asInstanceOf[Reads[self.Value]]
}

trait HasDefaultReads extends LowPriorityBaseEnumReads with HasDefault with Logger { self: Enumeration with HasDefault =>
  implicit lazy val defaultReads: Reads[Value] = new Reads[Value] {
    override def reads(json: JsValue): JsResult[Value] = baseEnumReads.reads(json)
      .recover {
        case err: JsError => {
          logger.error("Failed to parse enum value", JsResult.Exception(err))
          self.defaultValue
        }
      }
  }
}

