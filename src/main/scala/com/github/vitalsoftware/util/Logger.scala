package com.github.vitalsoftware.util

trait Logger {
  val logger = play.api.Logger(this.getClass)
}

