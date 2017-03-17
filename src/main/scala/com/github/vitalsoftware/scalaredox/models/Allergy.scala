package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * describes any medication allergies, food allergies, or reactions to other substances
  * (such as latex, iodine, tape adhesives). At a minimum, it should list currently active
  * and relevant historical allergies and adverse reactions.
  */
case class AllergiesMessage(
  AllergyText: Option[String] = None,
  Allergies: Seq[Allergy] = Seq.empty
)

/**
  *
  * @param Type A code for the type of allergy intolerance this is (food, drug, etc.). Allergy/Adverse Event Type Value Set
  * @param Substance The substance that the causes the alergy/intolerance. Brand names and generics will be coded in RxNorm. Drug classes use NDF-RT, and foods use UNII
  * @param Reaction A code for the reaction caused by the allergy (dissiness, hives ,etc.). SNOMED CT
  * @param Severity A code for the severity of the reaction (moderate, severe, etc.). SNOMED CT
  * @param Status The current status of the Allergy (active, historic, etc.). SNOMED CT (Active, Inactive, Resolved)
  * @param StartDate When the allergy was first noted. ISO 8601 Format
  * @param EndDate When the allergy was no longer a problem (if applicable). ISO 8601 Format
  * @param Comment Free text comment about the allergy.
  */
case class Allergy(
  Type: BasicCode,
  Substance: BasicCode,
  Reaction: Seq[CodeWithText] = Seq.empty,
  Severity: BasicCode,
  Status: BasicCode,
  StartDate: Option[DateTime] = None,
  EndDate: Option[DateTime] = None,
  Comment: Option[String] = None
)
