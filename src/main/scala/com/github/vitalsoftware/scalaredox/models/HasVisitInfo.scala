package com.github.vitalsoftware.scalaredox.models

trait HasVisit {
  def Visit: Option[VisitLike]
}

trait HasVisitInfo {
  def Visit: Option[VisitInfo]
}
