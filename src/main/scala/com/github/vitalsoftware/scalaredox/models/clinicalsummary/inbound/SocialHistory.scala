package com.github.vitalsoftware.scalaredox.models.clinicalsummary.inbound

import com.github.vitalsoftware.macros.jsonDefaults
import com.github.vitalsoftware.scalaredox.models.BasicCode
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * @param Code A code for the observation (exercise, alcohol intake, etc.) . SNOMED CT
 * @param Value The coded observed value for the code
 * @param ValueText The observed value for the code
 */
@jsonDefaults case class SocialHistoryObservation(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  StartDate: Option[String] = None,
  EndDate: Option[String] = None,
  Value: Option[BasicCode] = None,
  ValueText: Option[String] = None
)

object SocialHistoryObservation extends RobustPrimitives

/**
 * @param StartDate When the pregnancy started. ISO 8601 Format
 * @param EndDate When the pregnancy ended. ISO 8601 Format
 * @param EstimatedDelivery Estimate delivery date if pregnancy is still active.
 */
@jsonDefaults case class Pregnancy(
  StartDate: Option[String] = None,
  EndDate: Option[String] = None,
  EstimatedDelivery: Option[String] = None
)

object Pregnancy extends RobustPrimitives

/**
 * @param Code A code indicating the status (current smoker, never smoker, snuff user, etc.). Contains all values descending from the SNOMED CT® 365980008 tobacco use and exposure - finding hierarchy
 * @param StartDate Start date of status
 * @param EndDate Date status ended. If this is null, the status is current.
 */
@jsonDefaults case class TobaccoUse(
  Code: Option[String] = None,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  StartDate: Option[String] = None,
  EndDate: Option[String] = None
)

object TobaccoUse extends RobustPrimitives

@jsonDefaults case class SocialHistory(
  Observations: Seq[SocialHistoryObservation] = Seq.empty,
  Pregnancy: Seq[Pregnancy] = Seq.empty,
  TobaccoUse: Seq[TobaccoUse] = Seq.empty
)

object SocialHistory extends RobustPrimitives
