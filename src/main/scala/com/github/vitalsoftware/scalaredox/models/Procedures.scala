package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros._

/**
  * Created by apatzer on 3/17/17.
  */

/**
  *
  * @param Observations These are procedures that are more observational in nature, such as an EEG or EKG.
  * @param Procedures The procedure that was performed. SNOMED CT
  * @param Services These are procedures that are service-oriented in nature, such as a dressing change, or feeding a patient.
  */
@json case class Procedures(
  Observations: Seq[Observation] = Seq.empty,
  Procedures: Seq[CodeWithStatus] = Seq.empty,
  Services: Seq[CodeWithStatus] = Seq.empty
)

/**
  * This section documents three types of things: diagnostic procedures, procedures that change the body, and services performed by clinical staff.
  *
  * @param ProceduresText Free text form of the procedures summary
  * @param Procedures A general grouper for all things that CDA considers procedures.
  */
@json case class ProceduresMessage(
  ProceduresText: Option[String] = None,
  Procedures: Procedures
)