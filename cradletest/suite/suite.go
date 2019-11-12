// Package suite implements the testsuite data structures and functions to
// construct them from XML files.
package suite

import (
	"bytes"
	"cradletest/cli"
	"encoding/xml"
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
)

// TestSuite holds the top-level test data unmarshalled from an XML file.
type TestSuite struct {
	XMLName     xml.Name        `xml:"TestSuite"`
	TestCases   []TestCase      `xml:"TestCase"`
	Batches     []Batch         `xml:"Batch"`
	DefaultAuth *Authentication `xml:"Authentication"`
}

// TestCase holds data for a single test case.
type TestCase struct {
	Name     *string `xml:"name,attr"`
	Request  *Request
	Response *Response
}

// Request holds data for the Request XML tag.
type Request struct {
	Method   *string `xml:"method,attr"`
	URI      *string `xml:"uri,attr"`
	Username *string `xml:"username,attr"`
	Password *string `xml:"password,attr"`
	Body     []byte  `xml:",chardata"`
}

// Authentication holds user credentials.
type Authentication struct {
	Username *string `xml:"username,attr"`
	Password *string `xml:"password,attr"`
}

// BodyReader returns a new io.Reader for a request's body.
//
// Returns nil if the request's body is empty.
func (r Request) BodyReader() io.Reader {
	if len(r.Body) == 0 {
		return nil
	}
	return bytes.NewReader(r.Body)
}

// PrepRequest generates a function which, when called, will send the request
// as outlined in this object to the host configured via the command line
// interface.
//
// It is important to note that this function does not send the request itself,
// but instead generates a go function which can be called later to generate
// the request.
func (r Request) PrepRequest(defaultAuth *Authentication) func() (*http.Response, error) {
	url := cli.URL() + *r.URI
	auth := defaultAuth
	if r.Username != nil {
		if r.Password != nil {
			auth = &Authentication{
				Username: r.Username,
				Password: r.Password,
			}
		} else {
			panic("username supplied with no password")
		}
	}

	request, err := http.NewRequest(*r.Method, url, r.BodyReader())
	if err != nil {
		panic(err)
	}

	if auth != nil {
		request.SetBasicAuth(*auth.Username, *auth.Password)
	}

	if *r.Method == "POST" {
		request.Header.Set("Content-Type", "application/json")
	}

	return func() (*http.Response, error) {
		return http.DefaultClient.Do(request)
	}
}

// Response holds data for the Response XML tag.
type Response struct {
	Status int    `xml:"status,attr"`
	Body   []byte `xml:",chardata"`
}

// BodyReader returns a new io.Reader for a response's body.
//
// Returns nil if the response's body is empty.
func (r Response) BodyReader() io.Reader {
	if len(r.Body) == 0 {
		return nil
	}
	return bytes.NewReader(r.Body)
}

// Batch holds a sequence of test cases which, unlike the cases defined in
// TestSuite, are guarantied to run in sequential order.
type Batch struct {
	TestCases []TestCase `xml:"TestCase"`
}

// FromFile attempts to construct a TestSuite from a given XML file. Returns an
// error if unable to do so.
func FromFile(path string) (*TestSuite, error) {
	data, err := ioutil.ReadFile(path)
	if err != nil {
		return nil, err
	}
	suite := TestSuite{}
	err = xml.Unmarshal(data, &suite)
	if err != nil {
		return nil, err
	}
	return &suite, err
}

// Validate checks the integrity of a TestSuite returning true if the suite is
// valid and false if not. If if the suite fails validation, error messages are
// written with the given writer.
func Validate(ts *TestSuite, out io.Writer) bool {
	for _, tc := range ts.TestCases {
		if !validateTestCase(&tc, out) {
			return false
		}
	}
	for _, batch := range ts.Batches {
		for _, tc := range batch.TestCases {
			if !validateTestCase(&tc, out) {
				return false
			}
		}
	}
	return true
}

func validateTestCase(tc *TestCase, out io.Writer) bool {
	if tc.Request == nil {
		_, _ = fmt.Fprintln(out, "TestCase: missing Request element")
		return false
	}
	if tc.Response == nil {
		_, _ = fmt.Fprintln(out, "TestCase: missing Response element")
		return false
	}
	return true
}
