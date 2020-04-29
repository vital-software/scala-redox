package com.github.vitalsoftware.scalaredox.models

trait HasVisit {
  def Visit: Option[VisitLike]
}

trait HasVisitInfo extends HasVisit {
  def Visit: Option[VisitInfo]
}
