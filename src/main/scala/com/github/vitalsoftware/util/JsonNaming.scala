package com.github.vitalsoftware.util

import play.api.libs.json.{ JsonNaming => Naming }

object JsonNaming {
  /**
   * For each class property, use the kebab case equivalent
   * to name its column (e.g. fooBar -> foo-bar).
   *
   * Based on play.api.libs.json.JsonNaming.SnakeCase
   */
  object KebabCase extends Naming {
    override val toString = "KebabCase"

    def apply(property: String): String = {
      val length = property.length
      val result = new StringBuilder(length * 2)
      var resultLength = 0
      var wasPrevTranslated = false
      for (i <- 0 until length) {
        var c = property.charAt(i)
        if (i > 0 || i != '-') {
          if (Character.isUpperCase(c)) {
            // append a hyphen if the previous result wasn't translated
            if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '-') {
              result.append('-')
              resultLength += 1
            }
            c = Character.toLowerCase(c)
            wasPrevTranslated = true
          } else {
            wasPrevTranslated = false
          }
          result.append(c)
          resultLength += 1
        }
      }

      // builds the final string
      result.toString()
    }
  }
}
