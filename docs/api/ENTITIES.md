# API Entities

This file outlines the structure of the various objects returned by API methods.
For an overview of the API methods themselves, see [METHODS.md](METHODS.md).

## Contents

* [Exception](#exception)
* [Patient](#patient)
* [Patient Profile](#patient-profile)
* [Reading](#reading)

## Exception

A common exception type is used for all API methods. In the event of an error, 
an object of this type will be sent back as a response.

The `error` field is unique to this entity meaning that a simple check for the
existence of an `error` field on a JSON object returned by an API method is 
sufficient to determine if the response is an error or not.

### Fields

| Field | Type | Nullable | Description |
|:-:|:-:|:-:|:-|
| `code` | `number` | `false` | The HTTP status code for the error |
| `error` | `string` | `false` | A HTTP reason phrase describing the error |
| `message` | `string` | `true` | A description of the error |

### Example

```json
{
    "code": 404,
    "error": "Not Found",
    "message": "entity with id '4' not found"
}
```

## Patient

Holds direct information about a single patient. This does not include things 
like readings or symptoms which have a many-to-one relationship with a patient 
entity, and, as such, must be separate entities.

### Fields

| Field | Type | Nullable | Description |
|:-:|:-:|:-:|:-|
| `id` | `string` | `false` | Unique identifier for the patient |
| `villageNumber` | `number` | `false` | Numerical identifier for the village in which the patient lives |
| `zoneNumber` | `number` | `false` | Numerical identifier for the zone in which the patient lives |
| `initials` | `string` | `false` | Patient's initials |
| `age` | `number` | `false` | Patient's age in years |
| `sex` | `number` | `false` | Sex of the patient as an enumerated value `{0=Male, 1=Female, 2=Unknown}` |
| `pregnant` | `boolean` | `false` | Is the patient pregnant? |
| `gestationalAge` | `number` | `true` | Gestational age of the patient **in weeks** |
| `medicalHistory` | `string` | `true` | Text describing the patient's medical history |
| `drugHistory` | `string` | `true` | Text describing the patient's drug history |
| `otherSymptoms` | `string` | `true` | Text describing any other symptoms that the patient may have |

### Example

```json
{
    "id": "001",
    "villageNumber": 1,
    "initials": "AB",
    "age": 30,
    "sex": 1,
    "gestationalAge": 16,
    "medicalHistory": null,
    "drugHistory": null,
    "otherSymptoms": null,
    "pregnant": true
}
```


## Patient Profile

Holds all information related to a patient: including lists of readings and 
symptoms. To put simply, this entity is the same as the [patient](#patient)
entity with extra `readings` and `symptoms` fields.

The reading values are ordered by timestamp in descending order meaning that
the most recent reading will be the first item in the array.

> Note: symptoms not yet implemented

### Fields

| Field | Type | Nullable | Description |
|:-:|:-:|:-:|:-|
| `id` | `string` | `false` | Unique identifier for the patient |
| `villageNumber` | `number` | `false` | Numerical identifier for the village in which the patient lives |
| `zoneNumber` | `number` | `false` | Numerical identifier for the zone in which the patient lives |
| `initials` | `string` | `false` | Patient's initials |
| `age` | `number` | `false` | Patient's age in years |
| `sex` | `number` | `false` | Sex of the patient as an enumerated value `{0=Male, 1=Female, 2=Unknown}` |
| `pregnant` | `boolean` | `false` | Is the patient pregnant? |
| `gestationalAge` | `number` | `true` | Gestational age of the patient **in weeks** |
| `medicalHistory` | `string` | `true` | Text describing the patient's medical history |
| `drugHistory` | `string` | `true` | Text describing the patient's drug history |
| `otherSymptoms` | `string` | `true` | Text describing any other symptoms that the patient may have |
| `readings` | `array` | `false` | An array of [Reading](#reading) entities
| `symptoms` | `array` | `false` | An array of [Symptom](#symptom) entities (not yet implemented)

### Example

```json
{
    "id": "001",
    "villageNumber": 1,
    "zoneNumber": 5,
    "initials": "AB",
    "age": 25,
    "sex": 1,
    "gestationalAge": 16,
    "medicalHistory": null,
    "drugHistory": null,
    "otherSymptoms": null,
    "pregnant": true,
    "readings": [
        {
            "id": 3,
            "systolic": 130,
            "diastolic": 100,
            "heartRate": 80,
            "colour": 2,
            "timestamp": "2019-09-24 12:31:34"
        },
        {
            "id": 2,
            "systolic": 130,
            "diastolic": 90,
            "heartRate": 80,
            "colour": 1,
            "timestamp": "2019-09-22 06:37:00"
        },
        {
            "id": 1,
            "systolic": 100,
            "diastolic": 80,
            "heartRate": 74,
            "colour": 0,
            "timestamp": "2019-09-20 20:12:32"
        }
    ],
    "symptoms": []
}
```

## Reading

Holds information about a single CRADLE reading.

### Fields

| Field | Type | Nullable | Description |
|:-:|:-:|:-:|:-|
| `id` | `number` | `false` | Unique reading identifier |
| `systolic` | `number` | `false` | Systolic (top number) reading |
| `diastolic` | `number` | `false` | Diastolic (bottom number) reading |
| `heartRate` | `number` | `false` | Heart rate reading |
| `colour` | `number` | `false` | CRADLE colour as an enumerated value `{0=GREEN, 1=YELLOW, 2=RED}` |
| `timestamp`| `string` | `false` | Timestamp of the reading in the format "yyyy-MM-dd HH:mm:ss" (24 hour clock) |
| `miscellaneousDetails` | `string` | `true` | Other details that a VHT may consider relevant during a reading |

### Example

```json
{
    "id": 1,
    "systolic": 100,
    "diastolic": 80,
    "heartRate": 74,
    "colour": 0,
    "timestamp": "2019-09-20 20:12:32",
    "miscellaneousDetails": null
}
```
