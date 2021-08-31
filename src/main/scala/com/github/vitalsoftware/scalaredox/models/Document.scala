package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

/**
 *
 * @param ID Your application's ID for the document
 * @param Author Provider responsible for this document
 * @param Visit If the document is tied to a visit
 * @param Locale The language of the document.
 * @param Title The title of the document.
 * @param DateTime The creation/publishing date/time of the document.
 * @param Type The type of document (CCD, progress note, etc.)
 */
@jsonDefaults case class Document(
  ID: String,
  Author: Option[Provider] = None, // Todo Provider.Type is not present!
  Visit: Option[VisitInfo] = None,
  Locale: String = java.util.Locale.ROOT.toString, // TODO java.util.Locale
  Title: String,
  DateTime: DateTime,
  Type: String,
) extends HasVisitInfo

object Document extends RobustPrimitives

/**
 * Information about the patient and where the summary came from
 *
 * @param Document An object containing metadata about the document being pushed to the destination.
 * @param Patient Patient
 */
@jsonDefaults case class Header(
  Document: Document,
  Patient: Patient
) extends HasPatient

object Header extends RobustPrimitives
