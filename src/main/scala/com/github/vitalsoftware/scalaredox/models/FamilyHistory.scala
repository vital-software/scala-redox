package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * This section contains entries for a patient's relatives and their health problems.
  * @param FamilyHistoryTest Free text form of the family history summary
  * @param FamilyHistory An array of family history observations
  */
case class FamilyHistoryMessage(
  FamilyHistoryTest: Option[String] = None,
  FamilyHistory: Seq[FamilyHistory] = Seq.empty
)

case class FamilyHistory(
Relation: Relation,
Problems: Seq[Problem] = Seq.empty
                        )

case class Relation(
  Code: String,
  CodeSystem: String,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  demographics: Demographics
) extends Code

/**
  * @param Sex Relation gender
  * @param DOB Date of Birth of the relative. In YYYY-MM-DD format
  */
case class RelationDemographics(
  Sex: Sex.Value,
  DOB: DateTime
)

/**
  *
  * Health problem.
  * @see https://phinvads.cdc.gov/vads/ViewValueSet.action?id=71FDBFB5-A277-DE11-9B52-0015173D1785
  *
  * @param Code A code for the particular problem experienced by the relative. SNOMED CT
  * @param Type The general class of the problem. (disease, problem, etc.).
  */
case class Problem(
  Code: String, // Todo seems duplicative of 'Type'
  CodeSystem: String,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Type: BasicCode,
  DateTime: Option[DateTime] = None,
  AgeAtOnset: Option[String] = None,
  IsCauseOfDeath: Option[Boolean] = None
) extends Code