package com.github.vitalsoftware.scalaredox.models.clinicalsummary

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.Identifier
import com.github.vitalsoftware.util.RobustPrimitives
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

import scala.collection.Seq

@jsonDefaults case class Patient(
  Identifiers: Seq[Identifier],
  Demographics: Option[Demographics] = None,
)

object Patient extends RobustPrimitives
