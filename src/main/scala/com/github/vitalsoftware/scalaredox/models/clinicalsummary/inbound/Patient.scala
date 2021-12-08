package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults

import scala.collection.Seq

@jsonDefaults case class Patient(
  Identifiers: Seq[Identifier],
  Demographics: Option[Demographics] = None,
)


