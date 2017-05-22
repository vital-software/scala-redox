package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/17/17.
  */

/**
  * Result from laboratories, imaging procedures, and other procedures.
  *
  * @param Code The test performed and resulted. LOINC for Lab - SNOMED CT otherwise
  * @param Status The status of the test (In Progress, Final)
  * @param Observations A list of corresponding observations for the test (result components)
  */
@jsonDefaults case class Result(
  Code: String,
  CodeSystem: String,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  Observations: Seq[Observation] = Seq.empty
) extends Code with Status

/**
  * Results messages communicate results of diagnostic tests such as labs, radiology imaging, etc.
  */
@jsonDefaults case class ResultsMessage(
  Meta: Meta,
  Patient: Patient,
  Orders: Seq[Order] = Seq.empty,
  Visit: Option[VisitInfo] = None
) extends MetaLike
