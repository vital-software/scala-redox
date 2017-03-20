package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime
import com.kifi.macros._

/**
  * Created by apatzer on 3/17/17.
  */

/**
  *
  * @param StartDate When the problem was noticed. ISO 8601 Format
  * @param EndDate When the problem stopped (if it is not current). ISO 8601 Format
  * @param Code The code for the problem. . SNOMED-CT Code. Limited to terms descending from the Clinical Findings (404684003) or Situation with Explicit Context (243796009)
  * @param Category What type of problem this is (complaint, diagnosis, symptom, etc.)
  * @param HealthStatus The effect of the problem on the patient (chronically ill, in remission, etc.). SNOMED-CT
  * @param Status The current state of the problem (active, inactive, resolved). HITSPProblemStatus
  */
@json case class Problem(
  StartDate: Option[DateTime] = None,
  EndDate: Option[DateTime] = None,
  Code: String,
  CodeSystem: String,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Category: BasicCode,
  HealthStatus: Option[BasicCode] = None,
  Status: Option[BasicCode] = None
) extends Code

/**
  * This section contains the patient's past and current relevant medical problems.
  *
  * @param ProblemsText Free text form of the problems summary
  * @param Problems An array of all of patient relevant problems, current and historical.
  */
@json case class ProblemsMessage(
  ProblemsText: Option[String] = None,
  Problems: Seq[Problem] = Seq.empty
)
