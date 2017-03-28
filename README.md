# scala-redox

Scala rest-client (Java compatible) for sending and receiving messages from the Redox healthcare APIs. Messages are sent
and received asynchronously, returning Future[RedoxResponse[T]] objects that are either a type T or a RedoxErrorResponse 
hat can include Json validation/serialization issues or an HTTP status like 403 or 404.

## Usage
See [tests](https://github.com/vital-software/scala-redox/tree/master/src/test/scala/com/github/vitalsoftware/scalaredox) for usage examples.

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
      val fut = client.queryClinicalSummary(query)
      fut.map { clinicalSummary =>

        clinicalSummary.VitalSigns.foreach(vs => ...do something with vitals...)
      }
```

You can of course create the ClinicalSummaryQuery object in Scala as well.

## Architecture
Data-models available at [Redox](https://developer.redoxengine.com/) have been transcribed into Scala case classes.

 - Json serialization/deserialization using Play-Json gives you the full
   object graph: e.g. `ClinicalSummary.Medications.map(med => med.Route.Code)`
 - Strongly typed. String's in ISO 8601 format for example are converted into joda.DateTime
 - Non-required fields are Option[]

## Dependencies
 - [Play Webservices (WS) standalone HTTP client](https://github.com/playframework/play-ws)
 - [Play-Json 2.6.x](https://github.com/playframework/play-json)
 - Vital's own Scala annotation macros for easy Json.format[T]
 - [Units of Measurement for ISO/UCUM measurements](https://github.com/unitsofmeasurement/uom-systems)

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