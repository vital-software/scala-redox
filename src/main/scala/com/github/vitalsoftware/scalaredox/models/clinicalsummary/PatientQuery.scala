package com.github.vitalsoftware.scalaredox.models.clinicalsummary

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.{ Department, Meta }
import com.github.vitalsoftware.util.RobustPrimitives
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

/**
 * PATIENT QUERY MODEL
 * @param Meta Message header
 * @param Patient List of IDs and IDTypes for the patient
 * @param Location ID corresponding to the department from which to retrieved the patient chart.
 */
@jsonDefaults case class PatientQuery(
  Meta: Meta,
  Patient: Patient,
  Location: Option[Department] = None,
) extends ClinicalSummaryLike
    with HasClinicalSummaryPatient

object PatientQuery extends RobustPrimitives
