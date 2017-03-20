package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.kifi.macros._

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * Information about the patient and where the summary came from
  *
  * @param Document An object containing metadata about the document being pushed to the destination.
  * @param Patient Patient
  */
@json case class Header( // Todo: Optional vs required
  Document: Document,
  Patient: Patient
)

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
@json case class Document(
  ID: String,
  Author: Provider, // Todo Provider.Type is not present!
  Visit: Option[Visit] = None,
  Locale: String, // TODO java.util.Locale
  Title: String,
  DateTime: DateTime,
  Type: String
)
