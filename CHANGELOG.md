# Changelog

## [Unreleased]

## [0.100] - 2018-06-08

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
