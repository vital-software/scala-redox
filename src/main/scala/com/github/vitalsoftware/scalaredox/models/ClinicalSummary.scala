package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * @param Meta Message header
 * @param Patient List of IDs and IDTypes for the patient
 */
@jsonDefaults case class PatientQuery(
  Meta: Meta,
  Patient: Patient
) extends HasPatient

/*
object Visit {
  implicit lazy val jsonFormat = Jsonx.formatCaseClass[Visit]
}
 */
