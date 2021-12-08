package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.Provider
import com.github.vitalsoftware.scalaredox.models.clinicalsummary.Document

/**
 * Information about the clinical summary's patient and where the summary came from
 *
 * @param DirectAddressFrom The sender's Direct address, if one or both sides are using Direct messaging.
 * @param DirectAddressTo The recipient's Direct address, if one or both sides are using Direct messaging.
 * @param Document An object containing metadata about the document being pushed to the destination.
 * @param Patient Patient
 * @param PCP Provider
 */
@jsonDefaults case class Header(
  DirectAddressFrom: Option[String] = None,
  DirectAddressTo: Option[String] = None,
  Document: Document,
  Patient: Patient,
  PCP: Option[Provider] = None,
) extends HasClinicalSummaryPatient


