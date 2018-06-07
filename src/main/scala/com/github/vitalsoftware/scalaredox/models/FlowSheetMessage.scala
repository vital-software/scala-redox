package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros.jsonDefaults

/**
 * Created by apatzer on 6/8/18.
 */

/**
 * The Flowsheet data model allows integration of discrete clinical data. Flowsheets data includes patient assessment
 * information on both the inpatient and outpatient side. It also includes vitals and any discrete nursing
 * documentation.
 */
@jsonDefaults case class FlowSheetMessage(
  Meta: Meta,
  Patient: Patient,
  Visit: Option[BasicVisitInfo] = None,
  Observations: Seq[Observation] = Nil
)
