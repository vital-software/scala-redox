package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.util.JsonImplicits._
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.HasDefaultReads
import org.joda.time.DateTime
import play.api.libs.json.{ Format, Reads, Writes }

/**
 * Created by apatzer on 3/23/17.
 */

object NoteContentTypes extends Enumeration with HasDefaultReads {
  val PlainText = Value("Plain Text")
  val RichText = Value("Rich Text")
  val Base64 = Value("Base64 Encoded")

  val defaultValue = PlainText
  implicit lazy val jsonFormat: Format[NoteContentTypes.Value] = Format(defaultReads, Writes.enumNameWrites)
}

/**
 * @param ID ID of the order assigned by the placing system
 * @param Name Name of the order assigned by the placing system
 */
@jsonDefaults case class NoteOrder(
  ID: String,
  Name: Option[String] = None
)

/**
 *
 * @param ID The ID of the discrete note component. A report ID, or documentation field ID
 * @param Name The name of the discrete note component. e.g. 'Severity'
 * @param Value The text of the note component. Plain text or RTF
 * @param Comments Additional comments for the discrete note field
 */
@jsonDefaults case class NoteComponent(
  ID: Option[String] = None,
  Name: Option[String] = None,
  Value: Option[String] = None,
  Comments: Option[String] = None
)

@jsonDefaults case class Note(
  ContentType: NoteContentTypes.Value,
  FileName: Option[String] = None,
  FileContents: Option[String] = None,
  Components: Seq[NoteComponent] = Seq.empty,
  DocumentType: String,
  DocumentID: String,
  ServiceDateTime: Option[DateTime] = None,
  DocumentationDateTime: Option[DateTime] = None,
  Provider: Provider,
  Status: Option[String] = None,
  Authenticator: Option[Provider] = None,
  Notifications: Seq[Provider] = Seq.empty
)

/**
 * @param Visit Requires only VisitNumber + VisitDateTime
 */
@jsonDefaults case class NoteMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[VisitInfo] = None,
  Note: Note,
  Orders: Seq[NoteOrder] = Seq.empty
) extends MetaLike with HasPatient with HasVisitInfo
