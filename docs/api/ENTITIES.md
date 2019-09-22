# API Entities

This file outlines the structure of the various objects returned by API methods. For an overview of the API methods themselves, see [METHODS.md](METHODS.md).

## Contents

* [Exception](#exception)

## Exception

A common exception type is used for all API methods. In the event of an error, an object of this type will be sent back as a response.

The `error` field is unique to this entity meaning that a simple check for the existence of an `error` field on a JSON object returned by an API method is sufficient to determine if the response is an error or not.

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
