# Changelog

## [Unreleased]

- Add "Formatted Text" as a Order ValueType
- Handle "ST" and "RT" Order priority value via a mapping

## [1.4.5] - 2018-08-24

- Remove HasDefaultEnum json parsing

## [1.4.4] - 2018-08-23

- Change Language field from String to java.util.Locale

## [1.4.3] - 2018-08-23

- Added robust parsing for primitive types.

## [1.4.2] - 2018-08-19

- Fix robust parsing issue with json arrays.

## [1.4.1] - 2018-08-16

- Fix bug where robust parsing fails to recover from deep errors

## [1.4.0] - 2018-08-13

- Changes signature of `webhook` to return both potential error and results. `(Option[JsError], Option[AnyRef])`

## [1.3.0] - 2018-08-10

- With the upgrade of `json-annotations` library. We will now assign default values to message fields
whenever parsing of that filed fails.

### Changed

- Failing to parse an `Enumeration` with `HasDefaultReads` will now fallback to a default value
and a failure being logged. (except for `SexType`, `DataModel`, `RedoxEventTypes` and `CommonVitalTypes`)

## [1.1.0] - 2018-06-28

## [1.0.2] - 2018-06-28

## [1.0.1] - 2018-06-18

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

## [0.100] - [0.102] - 2018-06-11

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
