package com.github.vitalsoftware.scalaredox.models

import com.github.vitalsoftware.macros._
import com.github.vitalsoftware.util.RobustPrimitives

/**
 * @author andrew.zurn@dexcom.com - 11/1/17.
 */
@jsonDefaults case class Upload(URI: String)

object Upload extends RobustPrimitives
