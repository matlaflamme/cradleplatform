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
| `villageNumber` | `string` | `false` | Numerical identifier for the village in which the patient lives |
| `zoneNumber` | `string` | `false` | Numerical identifier for the zone in which the patient lives |
| `name` | `string` | `false` | Patient's initials |
| `birthYear` | `number` | `false` | Patient's birth year |
| `sex` | `number` | `false` | Sex of the patient as an enumerated value `{0=Male, 1=Female, 2=Unknown}` |
| `medicalHistory` | `string` | `true` | Text describing the patient's medical history |
| `drugHistory` | `string` | `true` | Text describing the patient's drug history |
| `lastUpdated` | `string` | `false` | Timestamp of when the patient info was last updated in the format "yyyy-MM-dd HH:mm:ss" (24 hour clock) |
| `generalNotes` | `string` | `true` | General notes about the patient |

### Example

```json
{
    "id": "001",
    "villageNumber": "1",
    "zoneNumber": "5",
    "name": "AB",
    "age": 1990,
    "sex": 1,
    "medicalHistory": null,
    "drugHistory": null,
    "lastUpdated": "2019-09-20 20:12:32",
    "generalNotes": null
}
```


## Patient Profile

Aggregate information about a patient.

The reading values are ordered by timestamp in descending order meaning that
the most recent reading will be the first item in the array.

### Fields

| Field | Type | Nullable | Description |
|:-:|:-:|:-:|:-|
| `id` | `string` | `false` | Unique identifier for the patient |
| `villageNumber` | `string` | `false` | Numerical identifier for the village in which the patient lives |
| `zoneNumber` | `string` | `false` | Numerical identifier for the zone in which the patient lives |
| `name` | `string` | `false` | Patient's initials |
| `birthYear` | `number` | `false` | Patient's birth year |
| `sex` | `number` | `false` | Sex of the patient as an enumerated value `{0=Male, 1=Female, 2=Unknown}` |
| `medicalHistory` | `string` | `true` | Text describing the patient's medical history |
| `drugHistory` | `string` | `true` | Text describing the patient's drug history |
| `lastUpdated` | `string` | `false` | Timestamp of when the patient info was last updated in the format "yyyy-MM-dd HH:mm:ss" (24 hour clock) |
| `generalNotes` | `string` | `true` | General notes about the patient |
| `readings` | `array` | `false` | An array of [ReadingView](#reading-view) entities

### Example

```json
{
    "id": "001",
    "villageNumber": "1",
    "zoneNumber": "5",
    "name": "AB",
    "birthYear": 1990,
    "sex": 1,
    "medicalHistory": null,
    "drugHistory": null,
    "lastUpdated": "2019-09-20 20:12:32",
    "generalNotes": null,
    "readings": [
        {
            "id": 3,
            "systolic": 130,
            "diastolic": 100,
            "heartRate": 80,
            "colour": 2,
            "pregnant": true,
            "gestationalAge": 24,
            "timestamp": "2019-09-24 12:31:34",
            "symptoms": [],
            "readingNotes": null
        },
        {
            "id": 2,
            "systolic": 130,
            "diastolic": 90,
            "heartRate": 80,
            "pregnant": true,
            "gestationalAge": 22,
            "colour": 1,
            "timestamp": "2019-09-22 06:37:00",
            "symptoms": [
                "Headache",
                "Unwell"
            ],
            "readingNotes": "Doesn't feel very good."
        },
        {
            "id": 1,
            "systolic": 100,
            "diastolic": 80,
            "heartRate": 74,
            "pregnant": true,
            "gestationalAge": 20,
            "colour": 0,
            "timestamp": "2019-09-20 20:12:32",
            "symptoms": [
                "Bleeding"
            ],
            "readingNotes": null
        }
    ]
}
```

## Reading

> **DEPRECIATED ON FRONTEND**: Use [Reading View](#reading-view) instead.
>
> This entity is only meant for communication between the service and database layers.

Holds information about a single CRADLE reading.

### Fields

| Field | Type | Nullable | Description |
|:-:|:-:|:-:|:-|
| `id` | `number` | `false` | Unique reading identifier |
| `systolic` | `number` | `false` | Systolic (top number) reading |
| `diastolic` | `number` | `false` | Diastolic (bottom number) reading |
| `heartRate` | `number` | `false` | Heart rate reading |
| `colour` | `number` | `false` | CRADLE colour as an enumerated value `{0=GREEN, 1=YELLOW_DOWN, 2=YELLOW_UP, 3=RED_DOWN,3=RED_UP}` |
| `pregnant` | `boolean` | `false` | Is the patient pregnant? |
| `gestationalAge` | `number` | `true` | Gestational age of the patient **in days** |
| `timestamp`| `string` | `false` | Timestamp of the reading in the format "yyyy-MM-dd HH:mm:ss" (24 hour clock) |
| `otherSymptoms` | `string` | `true` | Other symptoms that the patient may have |
| `readingNotes` | `string` | `true` | Comments from the VHT on the reading |

### Example

```json
{
    "id": 1,
    "systolic": 100,
    "diastolic": 80,
    "heartRate": 74,
    "colour": 0,
    "pregnant": true,
    "gestationalAge": 16,
    "timestamp": "2019-09-20 20:12:32",
    "otherSymptoms": null,
    "readingNotes": null
}
```

## Reading View

Aggregates information about a reading from multiple sources into a single object.

> Note to backend developers: controllers and the frontend interact with `ReadingView` objects when
> dealing with readings. Whereas the `Reading` class is meant for interacting directly with the database.
> The database has no concept of a `ReadingView` as it is a composite view of multiple tables.

### Fields

| Field | Type | Nullable | Description |
|:-:|:-:|:-:|:-|
| `id` | `number` | `false` | Unique reading identifier |
| `systolic` | `number` | `false` | Systolic (top number) reading |
| `diastolic` | `number` | `false` | Diastolic (bottom number) reading |
| `heartRate` | `number` | `false` | Heart rate reading |
| `colour` | `number` | `false` | CRADLE colour as an enumerated value `{0=GREEN, 1=YELLOW_DOWN, 2=YELLOW_UP, 3=RED_DOWN,3=RED_UP}` |
| `pregnant` | `boolean` | `false` | Is the patient pregnant? |
| `gestationalAge` | `number` | `true` | Gestational age of the patient **in days** |
| `timestamp`| `string` | `false` | Timestamp of the reading in the format "yyyy-MM-dd HH:mm:ss" (24 hour clock) |
| `otherSymptoms` | `string` | `true` | Other symptoms that the patient may have |
| `readingNotes` | `string` | `true` | Comments from the VHT on the reading |
| `symptoms` | `[string]` | `false` | A list of [symptoms](#symptom) for this reading |


### Example

```json
{
    "id": 1,
    "systolic": 100,
    "diastolic": 80,
    "heartRate": 74,
    "colour": 0,
    "pregnant": true,
    "gestationalAge": 16,
    "timestamp": "2019-09-20 20:12:32",
    "otherSymptoms": null,
    "readingNotes": null,
    "symptoms": [
        "Headache",
        "Blurred Vision",
        "Unwell"
    ]
}
```

## Symptom

An enumeration of common symptoms which can be attached to readings. Values are pulled directly from the Android application.

Serialized as a simple JSON string.

### Values

> Note: All values are **case-insensitive**.

* `"Headache"`
* `"Blurred Vision"`
* `"Abdominal Pain"`
* `"Bleeding"`
* `"Feverish"`
* `"Unwell"`
