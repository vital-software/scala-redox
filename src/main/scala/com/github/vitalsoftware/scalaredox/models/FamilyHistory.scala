package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.kifi.macros._

/**
  * Created by apatzer on 3/17/17.
  */

@json case class Relation(
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
@json case class RelationDemographics(
  Sex: Gender.Value,
  DOB: DateTime
)

/**
  * Health problem.
  * @see https://phinvads.cdc.gov/vads/ViewValueSet.action?id=71FDBFB5-A277-DE11-9B52-0015173D1785
  *
  * @param Code A code for the particular problem experienced by the relative. SNOMED CT
  * @param Type The general class of the problem. (disease, problem, etc.).
  */
@json case class FamilyHistoryProblem(
  Code: String,
  CodeSystem: String,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Type: BasicCode,
  DateTime: Option[DateTime] = None,
  AgeAtOnset: Option[String] = None,
  IsCauseOfDeath: Option[Boolean] = None
) extends Code

@json case class FamilyHistory(
  Relation: Relation,
  Problems: Seq[FamilyHistoryProblem] = Seq.empty
)

/**
  * This section contains entries for a patient's relatives and their health problems.
  * @param FamilyHistoryTest Free text form of the family history summary
  * @param FamilyHistory An array of family history observations
  */
@json case class FamilyHistoryMessage(
  FamilyHistoryTest: Option[String] = None,
  FamilyHistory: Seq[FamilyHistory] = Seq.empty
)
