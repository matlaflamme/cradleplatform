// Package serialrt implements a serial runtime environment.
//
// The serial runtime environment executes tests one at a time, in sequential
// order. This runtime environment may be necessary to use if a parallel one
// overloads the server with too many requests at once.
package serialrt

import (
	"cradletest/jsonutil"
	"cradletest/runtime"
	"cradletest/suite"
	"fmt"
	"strings"
)

// New constructs a new serial runtime environment.
func New() runtime.Runtime {
	return serialEnvironment{}
}

type serialEnvironment struct{}

// Run executes a TestSuite serially.
func (env serialEnvironment) Run(ts suite.TestSuite) ([]runtime.Result, error) {
	n := len(ts.TestCases)
	for _, b := range ts.Batches {
		n += len(b.TestCases)
	}

	results := make([]runtime.Result, n)

	i := 0
	exec := func(t suite.TestCase) error {
		r, err := runTest(t)
		if err != nil {
			return err
		}
		results[i] = *r
		i++
		return nil
	}

	for _, t := range ts.TestCases {
		if err := exec(t); err != nil {
			return nil, err
		}
	}
	for _, batch := range ts.Batches {
		for _, t := range batch.TestCases {
			if err := exec(t); err != nil {
				return nil, err
			}
		}
	}
	return results, nil
}

func runTest(t suite.TestCase) (result *runtime.Result, err error) {
	fn := t.Request.PrepRequest()

	// execute request
	resp, err := fn()
	if err != nil {
		return nil, err
	}

	result = &runtime.Result{}
	if t.Name != nil {
		result.Name = *t.Name
	} else {
		result.Name = fmt.Sprintf("%s: %s - expecting response code %d", *t.Request.Method, *t.Request.URI, t.Response.Status)
	}
	result.Passed = false

	// check response status code
	status := resp.StatusCode
	if status != t.Response.Status {
		result.Message = fmt.Sprintf("status code mismatch: expected %d, got %d", t.Response.Status, status)
		return
	}

	// check response body - if response tag in test suite has a body
	if len(t.Response.Body) != 0 {
		body := resp.Body

		// `body` is a ReadCloser meaning that is can only be read once. Since
		// we also want to display the body if there is an error we must first
		// store its contents in a buffer as passing the ReadCloser to
		// jsonutil.Equal will close the reader.
		var bodyContents *string
		bodyContents, err = jsonutil.PrettyFormat(body)
		if err != nil {
			result.Message = fmt.Sprintf("Unable to read body: %v", err)
			return
		}

		var rt bool
		rt, err = jsonutil.Equal(strings.NewReader(*bodyContents), t.Response.BodyReader())
		if err != nil {
			result.Message = fmt.Sprintf("JSON format error: %v", err)
			return
		}
		if !rt {
			expectedBytes, e := jsonutil.PrettyFormat(t.Response.BodyReader())
			if e != nil {
				return nil, e
			}
			result.Message = fmt.Sprintf(
				"body mismatch\n--- actual ---\n%s\n\n--- expected ---\n%s\n",
				*bodyContents,
				*expectedBytes,
			)
			return
		}
	}

	result.Passed = true
	return
}
