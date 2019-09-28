// Package suite implements the testsuite data structures and functions to
// construct them from XML files.
package suite

import (
	"bytes"
	"encoding/xml"
	"io"
	"io/ioutil"
)

// TestSuite holds the top-level test data unmarshalled from an XML file.
type TestSuite struct {
	XMLName   xml.Name   `xml:"TestSuite"`
	TestCases []TestCase `xml:"TestCase"`
	Batches   []Batch    `xml:"Batch"`
}

// TestCase holds data for a single test case.
type TestCase struct {
	Request  Request
	Response Response
}

// Request holds data for the Request XML tag.
type Request struct {
	Method string `xml:"method,attr"`
	URI    string `xml:"uri,attr"`
	Body   []byte `xml:",chardata"`
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
