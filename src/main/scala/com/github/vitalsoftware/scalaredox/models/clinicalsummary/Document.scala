package com.github.vitalsoftware.scalaredox.models.clinicalsummary

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.{ BasicCode, DocumentExtension, HasVisitInfo, Provider, VisitInfo }
import com.github.vitalsoftware.util.RobustPrimitives
import org.joda.time.DateTime
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

/**
 * About a clinical summary document
 *
 * @param ID Your application's ID for the document
 * @param Author Provider responsible for this document
 * @param Visit If the document is tied to a visit
 * @param Locale The language of the document.
 * @param Title The title of the document.
 * @param DateTime The creation/publishing date/time of the document.
 * @param Type The type of document (CCD, progress note, etc.)
 * @param TypeCode A code describing the type of document.
 */
@jsonDefaults case class Document(
  Author: Option[Provider] = None,
  ID: Option[String] = None,
  Locale: Option[String] = None,
  Title: Option[String] = None,
  DateTime: Option[DateTime] = None,
  Type: Option[String] = None,
  TypeCode: Option[BasicCode] = None,
  Visit: Option[VisitInfo] = None,
  Extensions: Option[DocumentExtension] = None,
) extends HasVisitInfo

object Document extends RobustPrimitives
