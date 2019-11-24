package com.github.vitalsoftware.helpers

import org.specs2.specification.AfterAll
import play.api
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{ DefaultAwaitTimeout, FutureAwaits, Injecting }
import play.api.{ Configuration, Environment, Mode }

/**
 * Global trait version of WithApplication provided by Play
 *
 * The WithApplication provided by Play is implemented as a Context object
 * so it does not support nested tests. This means it shuts down and starts
 * up the application for every individual test case.
 *
 * This trait is implemented on the test class/object as a whole, and
 * uses a single application instance for all the individual test cases within.
 */
trait WithApplication extends AfterAll with DefaultAwaitTimeout with FutureAwaits with Injecting {
  lazy val app: api.Application = builder.build()

  override def afterAll(): Unit = {
    await(app.stop())
    ()
  }

  protected def builder: GuiceApplicationBuilder =
    GuiceApplicationBuilder()
      .in(Mode.Test)
      .configure(Configuration.load(Environment.simple()))
}
