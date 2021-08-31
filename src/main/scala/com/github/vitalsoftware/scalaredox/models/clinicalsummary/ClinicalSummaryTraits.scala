package com.github.vitalsoftware.scalaredox.models.clinicalsummary

import com.github.vitalsoftware.scalaredox.models._

trait ClinicalSummaryLike {
  def Meta: Meta
}

trait HasClinicalSummaryPatient {
  def Patient: Patient
}
