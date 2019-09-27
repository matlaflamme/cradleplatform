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

Inserts a new patient entity into the database.

> Not yet implemented

### `POST /api/reading`

Inserts a new reading entity into the database.

> Not yet implemented
