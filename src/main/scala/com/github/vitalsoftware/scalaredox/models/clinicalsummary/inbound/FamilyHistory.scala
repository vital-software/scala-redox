package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.util.JsonImplicits.jodaISO8601Format
import com.github.vitalsoftware.scalaredox.models.{ FamilyHistoryProblem, Relation }
import com.github.vitalsoftware.util.RobustPrimitives

@jsonDefaults case class FamilyHistory(
  Relation: Option[Relation] = None,
  Problems: Seq[FamilyHistoryProblem] = Seq.empty
)

object FamilyHistory extends RobustPrimitives
