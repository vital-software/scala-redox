package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.scalaredox.models.Meta

trait ClinicalSummaryLike {
  def Meta: Meta
}

trait HasClinicalSummaryPatient {
  def Patient: Patient
}
