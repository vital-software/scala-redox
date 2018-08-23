package com.github.vitalsoftware.scalaredox.models

import org.joda.time.{ DateTime, LocalDate }
import com.github.vitalsoftware.util.JsonImplicits._
import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * Created by apatzer on 3/17/17.
 */

/**
 * @param Sex Gender of the relative
 * @param DOB Date of Birth of the relative. In YYYY-MM-DD format
 */
@jsonDefaults case class FamilyDemographics(
  Sex: SexType.Value = SexType.defaultValue,
  DOB: Option[LocalDate] = None
)

object FamilyDemographics extends RobustPrimitives

@jsonDefaults case class Relation(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Demographics: FamilyDemographics = FamilyDemographics(),
  IsDeceased: Option[Boolean] = None
) extends Code

object Relation extends RobustPrimitives

/**
 * @param Sex Relation gender
 * @param DOB Date of Birth of the relative. In YYYY-MM-DD format
 */
@jsonDefaults case class RelationDemographics(
  Sex: SexType.Value = SexType.defaultValue,
  DOB: LocalDate
)

object RelationDemographics extends RobustPrimitives

/**
 * Health problem.
 * @see https://phinvads.cdc.gov/vads/ViewValueSet.action?id=71FDBFB5-A277-DE11-9B52-0015173D1785
 *
 * @param Code A code for the particular problem experienced by the relative. SNOMED CT
 * @param Type The general class of the problem. (disease, problem, etc.).
 */
@jsonDefaults case class FamilyHistoryProblem(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Type: BasicCode = BasicCode(),
  DateTime: Option[DateTime] = None,
  AgeAtOnset: Option[String] = None,
  IsCauseOfDeath: Option[Boolean] = None
) extends Code

object FamilyHistoryProblem extends RobustPrimitives

@jsonDefaults case class FamilyHistory(
  Relation: Relation,
  Problems: Seq[FamilyHistoryProblem] = Seq.empty
)

object FamilyHistory extends RobustPrimitives

/**
 * This section contains entries for a patient's relatives and their health problems.
 * @param FamilyHistoryTest Free text form of the family history summary
 * @param FamilyHistory An array of family history observations
 */
@jsonDefaults case class FamilyHistoryMessage(
  FamilyHistoryTest: Option[String] = None,
  FamilyHistory: Seq[FamilyHistory] = Seq.empty
)

object FamilyHistoryMessage extends RobustPrimitives
