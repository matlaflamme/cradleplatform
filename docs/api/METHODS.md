# API Methods

This file outlines the various API methods exposed by the web framework. For
information about the entities returned by these methods see
[ENTITIES.md](ENTITIES.md).

> Note: Any method marked with "Not yet implemented" is an active API method
> which may be called, but it will always return a `NotImplementedException`.


* [Referral Methods](#referral-methods)
    * [`GET /api/referral/all`]() - Get all referrals
    * [`GET /api/referral/{healthCentreName}/all`]() - Get all referrals referred to a health centre (name)
    * [`POST /api/referral/send`]() - Sends a new referral. Valid patientId, vht name and health centre required.
    * ```json
      {
          "patientName": "VV",
          "patientId": "48121900518406",
          "patientAge": 15,
          "gestationAge": 60,
          "systolic": 25,
          "diastolic": 20,
          "heartRate": 30,
          "readingColour": 1,
          "symptoms": "[Blurred vision, Abdominal pain]",
          "isPregnant": false,
          "readingTimestamp": "2019-10-24 00:24:21.022",
          "referralTimestamp": "2019-10-24 00:27:14.317",
          "healthCentre": "Ne.",
          "VHT": "vht",
          "comments": ""
      }  
      ```


## Contents

* [Patient Methods](#patient-methods)
    * [`GET /api/patient/{id}`](#get-apipatientid)
    * [`GET /api/patient/{id}/info`](#get-apipatientidinfo)
    * [`GET /api/patient/{id}/readings`](#get-apipatientidreadings)
    * [`POST /api/patient`](#post-apipatient)
    * [`POST /api/patient/{id}/addMedications`](#post-apipatientmedications)
    * [`POST /api/patient/{id}/addMedication`](#post-apipatientmedication)
    * [`GET /api/patient/{id}/getMedications`](#get-apipatientmedications)
    * [`DELETE /api/patient/{id}/deleteMedication/{medId}`](#delete-apipatientmedication)
    * [`POST /api/reading`](#post-apireading)
    * [`GET /api/reading/{id}`](#get-apireadingid)
    * [`POST /api/reading/save`](#post-apireadingsave)
    * [`GET /api/hc/{id}/patients`](#get-apihcidpatients)
    * [`GET /api/user/{id}/patients`](#get-apiuseridpatients)
    * ['POST /api/referral/{id}/resolve'](#post-apireferralidresolve)
    * [`GET /api/user/{id}/readings`](#post-apiuseridreadings)
    * [`POST /api/referral/{id}/diagnosis`](#post-apireferraliddiagnosis)
* [User Methods](#user-methods)
    * [`GET /api/user/all`](get-apiuserall)
    * [`GET /api/user/{id}`](get-apiuserid)
    * [`POST /api/user/add`](post-apiuseradd)
    * [`DELETE /api/user/{id}`](delete-apiuserid)
    * [`POST /api/user/{username}/change-active`](post-apiuserusernamechange-active)
    * [`POST /api/user/{username}/set-health-centre`](post-apiuserusernameset-health-centre)
    * [`POST /api/user/{username}/remove-health-centre`](post-apiuserusernameremove-health-centre)
    * [`GET /api/user/{username}/health-centre`](get-apiuserusernamehealth-centre)

## Patient Methods

### `GET /api/patient/{id}`

Returns the whole profile for a patient with a given `id`: including lists of [reading views](#ENTITIES.md#reading-view).

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

Returns a JSON array of [reading](ENTITIES.md#reading) entities. If there are no
patients in the system, an empty array is returned.


### `GET /api/patient/all_with_latest_reading`

Returns an array of all patients registered in the system paired with their
latest reading. If a patient has no readings, then the `reading` field for that
pair will be `null`.

#### Returns

Returns a JSON array of [patient](ENTITIES.md#patient), [reading](ENTITIES.md#reading)
pairs (see example).

If there are no patients in the system, return an empty JSON array (i.e., `[]`).

#### Errors

None

#### Example

``` json
[
    {
        "patient": {
            "id": "001",
            "name": "Harumi Youko",
            "villageNumber": "1",
            "birthYear": 1995,
            "sex": 1,
            "medicalHistory": null,
            "drugHistory": null,
            "lastUpdated": "2019-09-20 13:12:32"
        },
        "reading": {
            "id": 3,
            "patientId": "001",
            "systolic": 130,
            "diastolic": 100,
            "heartRate": 80,
            "gestationalAge": null,
            "colour": 2,
            "timestamp": "2019-09-24 05:31:34",
            "pregnant": false,
            "otherSymptoms": null,
        }
    },
    {
        "patient": {
            "id": "002",
            "name": "Hikari Tachibana",
            "villageNumber": "1",
            "birthYear": 2002,
            "sex": 1,
            "medicalHistory": null,
            "drugHistory": null,
            "lastUpdated": "2019-09-20 13:12:32"
        },
        "reading": null
    }
]
```


### `GET /api/patient/{id}/readings`

Returns only the readings for the patient with a given `id`.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `string` | Patient Identifier |

#### Returns

Returns a JSON array of [reading view](ENTITIES.md#reading-view) entities. If unable to
find a patient with the requested `id`, or if there are no readings for the
requested patient, an empty array is returned.

#### Errors

Returns an `EntityNotFound` (404) exception if no patient with the given `id` is found
in the database.


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



###  `POST /api/patient/{id}/addMedications`

Inserts a list of new medications into the database


#### Request Body

* Format: `JSON`
  * i.e., `Content-Type: application/json` in the request header
  | Field | Type | Mandatory | Description |
|:-:|:-:|:-:|:-|

| `any` | `list` | `yes` | A list of an object containing the following fields |

| `medication` | `string` | `yes` | The name of the medication the patient is on |
| `doseage` | `string` | `yes` | The quantity of medication the patient recieves per dose |
| `usageFrequency` | `string` | `yes` | How often the Pateint recieves doses |

###  `POST /api/patient/{id}/addMedication`

Inserts a new medication into the database


#### Request Body

* Format: `JSON`
  * i.e., `Content-Type: application/json` in the request header
  | Field | Type | Mandatory | Description |
|:-:|:-:|:-:|:-|
| `medication` | `string` | `yes` | The name of the medication the patient is on |
| `doseage` | `string` | `yes` | The quantity of medication the patient recieves per dose |
| `usageFrequency` | `string` | `yes` | How often the Pateint recieves doses |


###  `GET /api/patient/{id}/getMedications`]

Recieves the lit of medications a patient is on

#### Return

A list of an objects of the following type

* Format: `JSON`
  * i.e., `Content-Type: application/json` in the request header
  | Field | Type | Mandatory | Description |
|:-:|:-:|:-:|:-|
| `medication` | `string` | `yes` | The name of the medication the patient is on |
| `doseage` | `string` | `yes` | The quantity of medication the patient recieves per dose |
| `usageFrequency` | `string` | `yes` | How often the Pateint recieves doses |



###   `DELETE /api/patient/{id}/deleteMedication/{medId}`]

Deletes the specified medication on a patient
#### Return

A copy of the object deleted

* Format: `JSON`
  * i.e., `Content-Type: application/json` in the request header
  | Field | Type | Mandatory | Description |
|:-:|:-:|:-:|:-|
| `medication` | `string` | `yes` | The name of the medication the patient is on |
| `doseage` | `string` | `yes` | The quantity of medication the patient recieves per dose |
| `usageFrequency` | `string` | `yes` | How often the Pateint recieves doses |



### `POST /api/patient/reading`

> **DEPRECIATED**: Use `/api/reading/save` instead.

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


### `GET /api/reading/{id}`

Returns a [reading view](ENTITIES.md#reading-view) object for a reading with a given identifier.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `number` | Reading Identifier |

#### Returns

A [reading view](ENTITIES.md#reading-view) entity for the reading with a given id.

#### Errors

Returns a 404-NotFound exception if unable to find a reading with the specified id.


### `POST /api/reading/save`

> Replaces `POST /api/patient/reading`.

Inserts a new, or updates an existing, reading in the database.

Whether the method updates or inserts a new item depends on the request's `id` field.
**To insert a new reading, the `id` field must be `null`** as the database automatically
creates new ids only when the field is `null`. To update an existing reading, the `id`
field should hold the `id` value of the reading to update.

A patient with the given `patientId` field **must** already exist in the system before
a new reading can be created for them.

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
| `symptoms` | `[string]` | `yes` | A list of [symptoms](ENTITIES.md#symptom) for this reading |


### `GET /api/hc/{id}/patients`

Returns the list of all patients, and their latest readings, referred to a given health center.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `number` | Health Center Identifier |

#### Returns

A list of patients and their latest readings. Returns an empty array in the event that `id` is invalid.


### `GET /api/user/{id}/patients`

Returns the list of all patients, and their latest readings, who have a reading created by a given user.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `number` | User Identifier |

#### Returns

A list of patients and their latest readings. Returns an empty array in the event that `id` is invalid.


### `GET /api/user/{id}/readings`

A list of all readings created by the user with a given id.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `number` | User Identifier |

#### Returns

A list of [reading views](ENTITIES.md#reading-view). Returns an empty array in the event that `id` is invalid.

### `POST /api/referral/new`

Create a referral for a patient

| Field | Type | Mandatory | Description |
|:-:|:-:|:-:|:-|
| `id` | `number` | `no` | Reading primary key, auto-generated if no present |
| `patentId` | `string` | `yes` | Id of the patient that this reading is for |
| `readingId` | `int` | `yes` | Id of the reading |
| `healthCentreId` | `int` | `yes` | Id of the healthcentre being referred to |
| `timestamp` | `string` | `no` | Time of the reading, in format "yyyy-MM-dd HH:mm:ss" (24h clock) |

```json
{
	"patientId":"001",
	"readingId":1,
	"healthCentreId":1
}
```

### `POST /api/referral/{id}/resolve`

Resolves the referral


### `POST /api/referral/{id}/diagnosis`

Create a diagnosis for a patient

### Fields

| Field | Type | Nullable | Description |
|:-:|:-:|:-:|:-|
| `patientId` | `string` | `false` | Patient Id |
| `description` | `string` | `false` | Description of diagnosis |


### Example

```json
{
	"description": "this is a description",
	"patientId":"001",
}
```

## User Methods

### `GET /api/user/all`

Returns a list of all users registered in the system.


### `GET /api/user/{id}`

Returns information about the user with a given `id`.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `number` | User Identifier |

#### Errors

Returns a 404-NotFound error if unable to find a user with a given `id`.


### `POST /api/user/add`

Creates a new user.

#### Example Request

```json
{
    "username": "john",
    "password": "doe",
    "roles:": "ROLE_HEALTHWORKER"
}  
```

#### Errors

Returns a 409-Conflict error if a user with the username already exists.


### `DELETE /api/user/{id}`

Deletes the user with a given `id`.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `id` | `number` | User Identifier |

#### Errors

Returns a 404-NotFound error if unable to find a user with the given `id`.


### `POST /api/user/{username}/change-active`

Toggles a user's `active` state.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `username` | `string` | A Username |

#### Errors

Returns a 404-NotFound error if unable to find a user with the given `username`.


### `POST /api/user/{username}/set-health-centre`

Affiliates the user with a given `username` with a requested health centre.
A user may only be affilated with a single health centre. This method may also
be used to update a user's affiliation to a new health centre. Use `remove-health-centre`
to remove an affiliation all together.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `username` | `string` | A Username |

#### Query Parameters

| Variable | Type | Mandatory | Description |
|:-:|:-:|:-:|:-|
| `hcid` | `number` | `true` | A Health Centre Identifier |

#### Errors

Returns a 404-NotFound error if unable to find the requested user or health centre.

#### Example Query

```
POST: /api/user/health/set-health-centre?hcid=2
```


### `POST /api/user/{username}/remove-health-centre`

Removes a health centre affiliation from a given user.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `username` | `string` | A Username |


#### Errors

Returns a 404-NotFound error if unable to find the requested user.


### `GET /api/user/{username}/health-centre`

Queries the health centre affiliation for a given user. This information is also
returned by `GET: /api/user/{id}` so this endpoint is redundant and is mainly
meant to ease testing.

#### Path Variables

| Variable | Type | Description |
|:-:|:-:|:-|
| `username` | `string` | A Username |

#### Returns

Returns a JSON object like the following with a single `id` field for the health centre id.

``` json
{
    "id": 2
}
```

#### Errors

Returns a 404-NotFound error if unable to find the requested user.


### `POST /api/user/check-password`

Checks that a plain text password, sent in the request body matches the password
of the requesting user.

#### Request Body

``` json
{
    "password": "<password>"
}
```

Where `<password>` is the password to check against the current one.

#### Returns

A JSON boolean (i.e., literal `true` or `false`), **not a string** denoting wether
the sent string matches the current password or not.


### `POST /api/user/update-password`

Changes the password for the requesting user to one supplied in the request body.

#### Request Body

``` json
{
    "password": "<password>"
}
```

Where `<password>` is the new password for the user.

#### Errors

Throws an exception if the new password does not meet complexity requirements.
