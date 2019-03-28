package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Code reference (like a foreign key into a SNOMED, ICD-9/10, or other data set)
 */
trait Code {
  def Code: Option[String]
  def CodeSystem: Option[String]
  def CodeSystemName: Option[String]
  def Name: Option[String]
}

@jsonDefaults case class BasicCode(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None
) extends Code

object BasicCode extends RobustPrimitives

@jsonDefaults case class CodeWithText(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Text: Option[String] = None
) extends Code

object CodeWithText extends RobustPrimitives

@jsonDefaults case class CodeWithStatus(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  DateTime: Option[DateTime] = None
) extends Code

object CodeWithStatus extends RobustPrimitives


// Alternative to 'Code' used inconsistently in some data models
trait CodeSet {
  def Code: Option[String]
  def CodeSet: Option[String]
}

@jsonDefaults case class BasicCodeSet(
  Code: Option[String] = None,
  CodeSet: Option[String] = None,
  Description: Option[String] = None,
) extends CodeSet

object BasicCodeSet extends RobustPrimitives

@jsonDefaults case class CodeSetWithName(
  Code: Option[String] = None,
  CodeSet: Option[String] = None,
  Name: Option[String] = None,
  Type: Option[String] = None,
) extends CodeSet

object CodeSetWithName extends RobustPrimitives
