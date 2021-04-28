package com.github.vitalsoftware.scalaredox

import java.nio.file.Files
import org.specs2.mutable.Specification

/**
 * @author andrew.zurn@dexcom.com - 11/1/17.
 */
class UploadTest extends Specification with RedoxTest {
  "alter Uploads" should {
    "post new Uploads" in {
      val file = Files.createTempFile("test-file", ".txt")
      java.nio.file.Files.newOutputStream(file).write("This is a test".getBytes)

      val fut = client.upload(file.toFile)
      val maybe = handleResponse(fut)
      maybe must beSome
      maybe.get.URI must contain("https://blob.redoxengine.com")
    }.pendingUntilFixed
  }
}
