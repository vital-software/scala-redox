package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.scalaredox.models.BasicCode
import com.github.vitalsoftware.scalaredox.models.Code
import com.github.vitalsoftware.util.RobustPrimitives

/**
 *
 * @param StartDate When the problem was noticed. ISO 8601 Format
 * @param EndDate When the problem stopped (if it is not current). ISO 8601 Format
 * @param Code The code for the problem. . SNOMED-CT Code. Limited to terms descending from the Clinical Findings (404684003) or Situation with Explicit Context (243796009)
 * @param Category What type of problem this is (complaint, diagnosis, symptom, etc.)
 * HealthStatus The effect of the problem on the patient (chronically ill, in remission, etc.). SNOMED-CT
 * @param Status The current state of the problem (active, inactive, resolved). HITSPProblemStatus
 */
@jsonDefaults case class Problem(
  StartDate: Option[String] = None,
  EndDate: Option[String] = None,
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Category: BasicCode = BasicCode(),
  HealthStatus: BasicCode = BasicCode(),
  Status: Option[BasicCode] = None,
  Comment: Option[String] = None,
) extends Code

object Problem extends RobustPrimitives

/**
 * This section contains the patient's past and current relevant medical problems.
 *
 * @param ProblemsText Free text form of the problems summary
 * @param Problems An array of all of patient relevant problems, current and historical.
 */
@jsonDefaults case class ProblemsMessage(
  ProblemsText: Option[String] = None,
  Problems: Seq[Problem] = Seq.empty
)

object ProblemsMessage extends RobustPrimitives
