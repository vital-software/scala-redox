# Changelog

## [Unreleased]

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
