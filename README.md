# Scala Redox [![Build status](https://badge.buildkite.com/b0bbb5518c0cee6021cd4f31c5ac97537aa0a2a17bf88bee2b.svg)](https://buildkite.com/vital/scala-redox) [![Test Coverage](https://api.codeclimate.com/v1/badges/c4eaeef835cead5f5717/test_coverage)](https://codeclimate.com/github/vital-software/scala-redox/test_coverage) [![Maintainability](https://api.codeclimate.com/v1/badges/c4eaeef835cead5f5717/maintainability)](https://codeclimate.com/github/vital-software/scala-redox/maintainability)

Scala rest-client (Java compatible) for sending and receiving messages from the Redox healthcare APIs. Messages are sent
and received asynchronously, returning Future[RedoxResponse[T]] objects that are either a type T or a RedoxErrorResponse
hat can include Json validation/serialization issues or an HTTP status like 403 or 404.

## Usage

See [tests](https://github.com/vital-software/scala-redox/tree/master/src/test/scala/com/github/vitalsoftware/scalaredox) for usage examples.

You will need to set the environment variables `REDOX_API_SECRET` and `REDOX_API_KEY` in order to run the tests locally.

```
      val json: String =
        """
          |{
          |	"Meta": {
          |		"DataModel": "Clinical Summary",
          |		"EventType": "Query",
          |		"EventDateTime": "2017-03-14T19:35:06.047Z",
          |		"Test": true,
          |		"Destinations": [
          |			{
          |				"ID": "ef9e7448-7f65-4432-aa96-059647e9b357",
          |				"Name": "Clinical Summary Endpoint"
          |			}
          |		]
          |	},
          |	"Patient": {
          |		"Identifiers": [
          |			{
          |				"ID": "0000000001",
          |				"IDType": "MR"
          |			},
          |			{
          |				"ID": "e167267c-16c9-4fe3-96ae-9cff5703e90a",
          |				"IDType": "EHRID"
          |			},
          |			{
          |				"ID": "a1d4ee8aba494ca^^^&1.3.6.1.4.1.21367.2005.13.20.1000&ISO",
          |				"IDType": "NIST"
          |			}
          |		]
          |	}
          |}
        """.stripMargin

      val query = Json.fromJson[ClinicalSummaryQuery](Json.parse(json)).get
      val fut = client.get[PatientQuery, ClinicalSummary](query)
      fut.map { clinicalSummary =>
        clinicalSummary.VitalSigns.foreach(vs => ...do something with vitals...)
      }
```

You can of course create the ClinicalSummaryQuery object in Scala as well.

# Installation

```scala
resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")
libraryDependencies += "com.github.vital-software" %% "scala-redox" % "8.0.3"
```

Tested with Scala 2.11.11

## Architecture

Data-models available at [Redox](https://developer.redoxengine.com/) have been transcribed into Scala case classes.

- Json serialization/deserialization using Play-Json gives you the full
  object graph: e.g. `ClinicalSummary.Medications.map(med => med.Route.Code)`
- Strongly typed. String's in ISO 8601 format for example are converted into joda.DateTime
- Non-required fields are Option[]

## Dependencies

- [Play Webservices (WS) standalone HTTP client](https://github.com/playframework/play-ws)
- [Play-Json 2.7.x](https://github.com/playframework/play-json)
- Vital's own Scala annotation macros for easy Json.format[T]
- [Units of Measurement for ISO/UCUM measurements](https://github.com/unitsofmeasurement/uom-systems)

## Future Work

Writing objects to Redox is still "hard", meaning you often need to know the Code and CodSystem used. We may bind a
`CodeProvider` trait that can be extended to provide codes to objects in a flexible way, such that your system
does not need to know whether a code is Snomed CT or LOINC.

The same applies to adding units of measurement, with UCUM and ISO measurements integrated into the system.

It's also possible these will be split into separate repos to keep the rest-client clean.

## Notes

An attempt was made to reconcile Redox's own internal data-structures, such that the "Provider" for example is the same
object in Encounter.Providers and Document.Author. This occasionally required adding optional fields that are present
in one model but not another. For example in models.Observation which is used by models.Result and models.Procedures:

```
  TargetSite: Option[BasicCode] = None,  // Used by Procedures only
  Interpretation: Option[String] = None  // Used by Result only
```

A different approach was taken by the PHP redox client listed below.

## Links

- [Redox PHP client from RoundingWell](https://github.com/RoundingWellOS/redox-php)
