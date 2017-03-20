package com.github.vitalsoftware.scalaredox.models

import com.kifi.macros._

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
@json case class Result(
  Code: String,
  CodeSystem: String,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  Status: Option[String] = None,
  Observations: Seq[Observation] = Seq.empty
) extends Code with Status

/**
  * This section contains results from laboratories, imaging procedures, and other procedures.
  * @param ResultText Free text form of the results summary
  * @param Results Array of test results for the patient. This can include laboratory results, imaging results, and procedure Results[].
  */
@json case class ResultsMessage(
  ResultText: Option[String] = None,
  Results: Seq[Result] = Seq.empty
)
