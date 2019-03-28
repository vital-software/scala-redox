# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- **Breaking**: Renamed `CodeSet` to `Codeset` (note the lower-case `s`) to fix
  mappings from JSON, converted it into a trait, with implementations
  `BasicCodeset` and `CodesetWithName`
- **Breaking**: Converted `Observation` into a trait, with implementations
  `ProcedureObservation`, `ResultObservation`, `VitalSignObservation` and
  `FlowsheetObservation`
  - This was done to cope with the flowsheet observation not having a
    `CodeSystem`

## [5.0.0] - 2019-03-25

### Changed

- **Breaking**: Made Demographics.dob optional as it's not reliable in
  `PatientUpdate` messages.

## [4.0.0] - 2019-03-22

### Changed

- **Breaking**: Made `Contact` and `BasicPerson`'s `FirstName` and `LastName`
  optional (In Redox, these are only "possible" fields)

## [3.0.0] - 2019-01-28

### Fixed

- Added the ability to handle multiple credentials for multiple sources.

  Each source in Redox has a unique pair of key/secret credentials. If a client
  has multiple sources, this would have required creating multiple RedoxClients,
  one for each source.

  However, `RedoxClient` constructed an internal `WsClient` and an
  Akka schedule to manage the access-refresh Token lifecycle. This consumed
  unnecessary resources as each client would have created its own thread pool.

### Changed

- **Breaking**: `RedoxClient` is completely redesigned to take in an external
  Http client. A secondary constructor is added for easier migration. However,
  usage is only advisable if application has a single source .
- Marked one property on the ReceiveController protected

### Added

- `RedoxTokenManager` to manage Redox tokens for multiple sources.
- Added `HttpClient` interface to allow using different http client under the
  hood.
- `ClientConfig` object standardizes the configuration values that's needed
  to be passed into RedoxClient.

## [2.1.0] - 2019-01-18

### Added

- Added a controller helper trait for responding to Redox endpoint challenges
  and validating received webhook messages

## [2.0.5] - 2018-12-12

### Fixed

- Robust parsing of non-array paths

## [2.0.1 - 2.0.4] - 2018-10-31

### Fixed

- Fixed compiler warning on non-exhaustive match

## [2.0.0] - 2018-10-31

### Removed

- Remove cross-compile code for 2.11 release as there are multiple
  incompatibilities.

## [1.6.0 - 1.6.2] - 2018-09-24

- Fixed `Locale` validation errors by introducing a strict `Language` type.

### Changed

`Patient.Demographics.Language` is changed from java.util.Local to `Language`

## [1.5.1] - 2018-09-18

### Added

- Added `CanceledEvent` field to Meta

## [1.5.0] - 2018-09-18

### Changed

- Made `VisitInfo.Patient` class an enum value

## [1.4.6] - 2018-09-04

### Added

- Added "Formatted Text" as a Order ValueType
- Handled "ST" and "RT" Order priority value via a mapping

## [1.4.5] - 2018-08-24

### Removed

- Removed HasDefaultEnum json parsing

## [1.4.4] - 2018-08-23

### Changed

- Changed Language field from String to java.util.Locale

## [1.4.3] - 2018-08-23

### Added

- Added robust parsing for primitive types.

## [1.4.2] - 2018-08-19

### Fixed

- Fixed robust parsing issue with json arrays.

## [1.4.1] - 2018-08-16

### Fixed

- Fixed bug where robust parsing fails to recover from deep errors

## [1.4.0] - 2018-08-13

### Changed

- Changes signature of `webhook` to return both potential error and results. `(Option[JsError], Option[AnyRef])`

### Removed

- Removed support for scala 2.11 (Backdated)

## [1.3.0] - 2018-08-10

- With the upgrade of `json-annotations` library. We will now assign default values to message fields
whenever parsing of that filed fails.

### Changed

- Failing to parse an `Enumeration` with `HasDefaultReads` will now fallback to a default value
and a failure being logged. (except for `SexType`, `DataModel`, `RedoxEventTypes` and `CommonVitalTypes`)

## [1.0.1 - 1.1.0] - 2018-06-28

### Changed

- Releases are now performed by CI automatically
- Cross-build Scala versions updated to the latest in each of 2.11.x/2.12.x

### Added

- Optional reducer argument to `RedoxClient.webhook`, similar to the one on the `RedoxClient` constructor

## [0.104] - 2018-06-12

### Added

- Added a constructor argument to `RedoxClient`, `reducer` which allows specifying how to reduce e.g. empty
  parts of a response before it is converted into model instances. Defaults to the current behavior, which is
  to only prune parts of a response that are strictly null.
- Added `reduceEmptySubtrees` as a method to `JsValue` (via implicits), in addition to the existing
  `reduceNullSubtrees` that prunes null objects - the difference is that the "empty" variant also considers
  arrays to be empty if they or their contents are (recursively).

## [0.103] - 2018-06-11

### Added

- Added a `HasVisitInfo` trait to mark models with a `Visit: Option[VisitInfo]` property.

## [0.100 - 0.102] - 2018-06-11

### Added

- Initial and webhook support for the `Flowsheet` data model.

## [0.99] - 2018-06-01

### Changed

- Messages were previously matched to a data type using the `Meta.DataModel` field only. However, this doesn't completely
  determine the shape of the message; for example, a message with `DataModel == Order` may not have an `Order` key,
  because `Meta.EventType` might be `GroupedOrders` (i.e. it has an `Orders` key, not an `Order` key).

  We now take both the `Meta.DataModel` and `Meta.EventType` into account when deciding how to parse a message.

- `Patient.Demographics.Sex` now defaults to a value of `Unknown`. This was previously a required value, but Redox only
  define it as "reliable" and not "required" (we've seen messages in-the-wild with `null` for this property.)

## [0.98] - 2018-05-28

### Changed

- `Insurance.Plan` changed from `InsurancePlan` to `Option[InsurancePlan]`
  - That's because the plan may be a "null subtree" (all properties null), in which case it is not guaranteed to be set
- Null subtree algorithm removes nulls from arrays
  - Previously, if null-subtree objects were wrapped in an array, the subtree removal would replace the objects with JsNull
    values, but leave them in the array (causing a later error), e.g.

    ```json
    "Orders": [
      {
        "Diagnoses": [
          {
            "Code": null,
            "Codeset": null,
            "Name": null,
            "Type": null
          }
        ]
      }
    ]
    ```

    previously became

    ```json
    "Orders": [
      {
        "Diagnoses": [null]
      }
    ]
    ```

    but will now become

    ```json
    "Orders": [
      {
        "Diagnoses": []
      }
    ]
    ```

## [0.97] - 2018-05-28

### Changed

- Version appearing in README is automatically updated on release

### Added

- Added sbt-release plugin
- Added a CHANGELOG

## [0.96] - 2018-05-28

### Changed

- `Order.ClinicalInfo.Code` changed from `String` to `Option[String]`
- `Order.Provider.NPI` changed from `String` to `Option[String]`



[Unreleased]: https://github.com/vital-software/scala-redox/compare/5.0.0...HEAD
[5.0.0]: https://github.com/vital-software/scala-redox/compare/4.0.0...5.0.0
[4.0.0]: https://github.com/vital-software/scala-redox/compare/3.0.0...4.0.0
[3.0.0]: https://github.com/vital-software/scala-redox/compare/2.1.0...3.0.0
[2.1.0]: https://github.com/vital-software/scala-redox/compare/2.0.5...2.1.0
[2.0.5]: https://github.com/vital-software/scala-redox/compare/2.0.4...2.0.5
[2.0.1 - 2.0.4]: https://github.com/vital-software/scala-redox/compare/2.0.0...2.0.4
[2.0.0]: https://github.com/vital-software/scala-redox/compare/1.6.2...2.0.0
[1.6.0 - 1.6.2]: https://github.com/vital-software/scala-redox/compare/1.5.1...1.6.2
[1.5.1]: https://github.com/vital-software/scala-redox/compare/1.5.0...1.5.1
[1.5.0]: https://github.com/vital-software/scala-redox/compare/1.4.6...1.5.0
[1.4.6]: https://github.com/vital-software/scala-redox/compare/1.4.5...1.4.6
[1.4.5]: https://github.com/vital-software/scala-redox/compare/1.4.4...1.4.5
[1.4.4]: https://github.com/vital-software/scala-redox/compare/1.4.3...1.4.4
[1.4.3]: https://github.com/vital-software/scala-redox/compare/1.4.2...1.4.3
[1.4.2]: https://github.com/vital-software/scala-redox/compare/1.4.1...1.4.2
[1.4.1]: https://github.com/vital-software/scala-redox/compare/1.4.0...1.4.1
[1.4.0]: https://github.com/vital-software/scala-redox/compare/1.3.0...1.4.0
[1.3.0]: https://github.com/vital-software/scala-redox/compare/1.1.0...1.3.0
[1.0.1 - 1.1.0]: https://github.com/vital-software/scala-redox/compare/0.104...1.1.0
[0.104]: https://github.com/vital-software/scala-redox/compare/0.103...0.104
[0.103]: https://github.com/vital-software/scala-redox/compare/0.102...0.103
[0.100 - 0.102]: https://github.com/vital-software/scala-redox/compare/0.99...0.102
[0.99]: https://github.com/vital-software/scala-redox/compare/0.98...0.99
[0.98]: https://github.com/vital-software/scala-redox/compare/0.97...0.98
[0.97]: https://github.com/vital-software/scala-redox/compare/0.96...0.97
[0.96]: https://github.com/vital-software/scala-redox/releases/tag/0.96
