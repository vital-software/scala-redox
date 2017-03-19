package com.github.vitalsoftware.scalaredox.models

import org.joda.time.DateTime

/**
  * Created by apatzer on 3/20/17.
  */

/**
  * This section contains information such as tobacco use, pregnancies, and generic social behavior observations.
  * @param SocialHistoryText Free text form of the social history summary
  * @param SocialHistory Generic observations about the patient's social hisotry that don't fall into the smoking or pregnancy categories.
  */
case class SocialHistoryMessage(
  SocialHistoryText: Option[String] = None,
  SocialHistory: SocialHistory
)

case class SocialHistory(
  Observations: Seq[SocialHistoryObservation] = Seq.empty,
  Pregnancy: Seq[Pregnancy] = Seq.empty,
  TobaccoUse: Seq[TobaccoUse] = Seq.empty
)

/**
  * @param Code A code for the observation (exercise, alcohol intake, etc.) . SNOMED CT
  * @param Value The coded observed value for the code
  * @param ValueText The observed value for the code
  */
case class SocialHistoryObservation(
  Code: String,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  StartDate: DateTime,
  EndDate: Option[DateTime] = None,
  Value: Option[BasicCode] = None,
  ValueText: Option[String] = None
) extends Code with DateRange

/**
  * @param StartDate When the pregnancy started. ISO 8601 Format
  * @param EndDate When the pregnancy ended. ISO 8601 Format
  * @param EstimatedDelivery Estimate delivery date if pregnancy is still active.
  */
case class Pregnancy(
  StartDate: DateTime,
  EndDate: Option[DateTime] = None,
  EstimatedDelivery: Option[String] = None
) extends DateRange

/**
  * @param Code A code indicating the status (current smoker, never smoker, snuff user, etc.). Contains all values descending from the SNOMED CT® 365980008 tobacco use and exposure - finding hierarchy
  * @param StartDate Start date of status
  * @param EndDate Date status ended. If this is null, the status is current.
  */
case class TobaccoUse(
  Code: String,
  CodeSystem: Option[String] = None,
  CodeSystemName: Option[String] = None,
  Name: Option[String] = None,
  StartDate: DateTime,
  EndDate: Option[DateTime] = None
) extends Code with DateRange