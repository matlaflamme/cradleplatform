# API Methods

This file outlines the various API methods exposed by the web framework. For 
information about the entities returned by these methods see 
[ENTITIES.md](ENTITIES.md).

> Note: Any method marked with "Not yet implemented" is an active API method 
> which may be called, but it will always return a `NotImplementedException`.

## Contents

* [Patient Methods](#patient-methods)
    * [`GET /api/patient/{id}`](#get-apipatientid)
    * [`GET /api/patient/{id}/info`](#get-apipatientidinfo)
    * [`GET /api/patient/{id}/readings`](#get-apipatientidreadings)
    * [`POST /api/patient`](#post-apipatient)
    * [`POST /api/reading`](#post-apireading)

## Patient Methods

### `GET /api/patient/{id}`

Returns the whole profile for a patient with a given `id`: including lists of readings and symptoms.

> Note: Symptoms have not yet been implemented

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `string` | Patient Identifier |

#### Returns

Upon successful request, returns a [patient profile](ENTITIES.md#patient-profile) entity.

#### Errors

Returns an `EntityNotFound` (404) exception if no patient with path variable 
`id` can be found in the database.


### `GET /api/patient/{id}/info`

Returns only the direct information for the patient with a given `id`. 
This does not include things like a the list or readings or symptoms.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `string` | Patient Identifier |

#### Returns

Upon successful request, returns a [patient](ENTITIES.md#patient) entity.

#### Errors

Returns an `EntityNotFound` (404) exception if no patient with path variable 
`id` can be found in the database.


### `GET /api/patient/all`

Returns an array of all patients registered in the system.

#### Returns

Returns a JSON array of [reading](ENTITIES.md#patient) entities. If there are no
patients in the system, an empty array is returned.


### `GET /api/patient/{id}/readings`

Returns only the readings for the patient with a given `id`.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `string` | Patient Identifier |

#### Returns

Returns a JSON array of [reading](ENTITIES.md#reading) entities. If unable to
find a patient with the requested `id`, or if there are no readings for the 
requested patient, an empty array is returned.

#### Errors

None


### `POST /api/patient`

Inserts a new patient entity into the database, or updates an existing one.

Whether the method updates or creates a new patient depends on the `id` field of
the request body. If a patient with the request `id` already exists, its data
will be overwritten with the data in the request body (assuming the request body
is valid).

#### Request Body

* Format: `JSON`
  * i.e., `Content-Type: application/json` in the request header

| Field | Type | Mandatory | Description |
|:-:|:-:|:-:|:-|
| `id` | `string` | `yes` | Unique identifier for the patient |
| `name` | `string` | `yes` | Patient's name |
| `villageNumber` | `string` | `yes` | Village number for the patient |
| `dateOfBirth` | `string` | `yes` | Patient's birth date, in format "yyyy-MM-dd" |
| `sex` | `number` | `yes` | Patient's sex, enumerated: {male=0, female=1, unkown=2} |
| `medicalHistory` | `string` | `no` | Patient's medical history |
| `drugHistory` | `string` | `no` | Patient's drug history |
| `otherSympotoms` | `string` | `no` | Any other symptoms the patient has |
| `lastUpdated` | `string` | `false` | Timestamp of when the patient info was last updated in the format "yyyy-MM-dd HH:mm:ss" (24 hour clock) |

### `POST /api/reading`

Inserts a new reading entity into the database.

Inserts a new reading entity into the database, or updates an existing one.

Like with patients, whether the method updates or creates a new patient depends
on the `id` field of the request. If a reading with the request `id` is present,
its data will be overwritten with the date in the request body (assuming the
request body is valid).

The `patientId` field has a foreign key constraint with `patient.id` meaning that
a patient with the given `patientId` must exist in the system before the reading
can be added.

| Field | Type | Mandatory | Description |
|:-:|:-:|:-:|:-|
| `id` | `number` | `no` | Reading primary key, auto-generated if no present |
| `patentId` | `string` | `yes` | Id of the patient that this reading is for |
| `systolic` | `number` | `yes` | Systolic reading value |
| `diastolic` | `number` | `yes` | Diastolic reading value |
| `heartRate` | `number` | `yes` | Heart rate reading value |
| `pregnant` | `boolean` | If `sex` != Male (`0`) | Is the patient pregnant? |
| `gestationalAge` | `number` | If `pregnant` == `true` | Gestational age of the patient in days|
| `colour` | `number` | `yes` | CRADLE reading colour, enumerated: {green=0, yellow_down=1, yellow_up=2,red_down=3, red_up=4} |
| `timestamp` | `string` | `yes` | Time of the reading, in format "yyyy-MM-dd HH:mm:ss" (24h clock) |
