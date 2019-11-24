package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * The Flowsheet data model allows integration of discrete clinical data. Flowsheets data includes patient assessment
 * information on both the inpatient and outpatient side. It also includes vitals and any discrete nursing
 * documentation.
 */
@jsonDefaults case class FlowSheetMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[BasicVisitInfo] = None,
  Observations: Seq[FlowsheetObservation] = Nil
) extends MetaLike
    with HasPatient

object FlowSheetMessage extends RobustPrimitives
