# Integration Tests And You!

## Running Tests

Running integration tests is simple (hopefully)! All it takes is a single
command run from the `/cradleplatform/cradletest` directory.

On Unix:

```
./run_tests.sh
```

On Windows (with PowerShell):

```
.\run_tests.ps1
```

You can also run the script with `--verbose` on Unix or `-Verbose` on Windows
to see the console output of the web server. This may help with debugging 
failing tests.

## Writing New Tests

Adding integration tests involves editing the `testsuite.xml` file.

### The `TestCase` Element

Each test is defined by a `TestCase` XML element. Each `TestCase` tag may
contain an optional `name` attribute which will help identify the test.

Each `TestCase` element must contain one `Request` and one `Response` element.
The `Request` element defines the HTTP request which will be made to the server
for the test. The `Response` element defines what HTTP response is expected from
the server. If the actual response doesn't match the `Response` element, the 
test fails.

Example `TestCase` element:

``` xml
<TestCase name="Request For Non-existent Patient">
  <Request method="GET" uri="/api/patient/X/info"/>
  <Response status="404">
    <![CDATA[
    {
      "code":404,
      "error":"Not Found",
      "message":"entity with id 'X' not found"
    }
    ]]>
  </Response>
</TestCase>
```

### The `Request` Element

Each `Request` tag must have a `method` attribute which defines the HTTP method
to use for the request. The only supported values at this time are `"GET"` and
`"POST"`.

Along with `method`, each `Request` tag must also contain a `uri` attribute
which defines the path to send the request to. The path should not contain the
hostname. For example: `"/api/patient/all"` would be a valid `uir` attribute.

If a request's method is `POST`, the request should contain a body. The HTTP body
is defined by the data between the opening and closing `Request` tags.

For example:

``` xml
<Request method="POST" uri="/api/patient/">
  <!-- body goes here -->
</Request>
```

Normally, bodies are enclosed in `<![CDATA[ ]]>` tags to tell the XML parser that
everything between there should be treated as raw text and not XML.

Example `POST` `Request` element:

``` xml
<Request method="POST" uri="/api/patient">
  <![CDATA[
  {
    "id":"1002",
    "name":"Not Important To This Test",
    "villageNumber":"0",
    "dateOfBirth":"2000-01-01",
    "sex":1,
    "pregnant":true
  }
  ]]>
</Request>
```

Example `GET` `Request` element:

``` xml
<Request method="GET" uri="/api/patient/X/info"/>
<!-- no body is required for GET methods so we can use a self closing tag -->
```

### The `Response` Element

The `Response` element defines how the HTTP response from the web server should
be for the test to pass. It takes a single mandatory attribute `status` which
denotes the HTTP response status code (e.g., `"200"`, `"404"`, etc.).

A `Response` element may also contain an optional body which will be matched
against the body of the HTTP response. The bodies must be equivalent JSON for
the test to pass.

Example `Response` element with no body:

``` xml
<Response status="200"/>
```

Example `Response` element with a body:

``` xml
<Response status="200">
  <![CDATA[
  {
    "id":"001",
    "name":"Harumi Youko",
    "villageNumber":"1",
    "dateOfBirth":"1995-12-25",
    "sex":1,
    "gestationalAge":16,
    "medicalHistory":null,
    "drugHistory":null,
    "otherSymptoms":null,
    "pregnant":true
  }
  ]]>
</Response>
```

## Limitations

The following is a list of what the test framework cannot currently do. Updates will be
made in later iterations which will add some of these features.

* Non-JSON request/response bodies
* Authentication
* General assertions (e.g., check if response is a JSON array with `n` elements)
* `DELETE` and other HTTP methods
* Running tests concurrently
* Setup and teardown tests (i.e., tests which are guarantied to run before/after all others)
