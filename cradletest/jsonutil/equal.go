// Package jsonutil implements utility functions for JSON objects.
package jsonutil

import (
	"encoding/json"
	"io"
	"reflect"
)

// source: https://stackoverflow.com/a/32409106

// Equal compares the JSON from two Readers.
func Equal(a, b io.Reader) (bool, error) {
	var j, j2 interface{}
	d := json.NewDecoder(a)
	if err := d.Decode(&j); err != nil {
		return false, err
	}
	d = json.NewDecoder(b)
	if err := d.Decode(&j2); err != nil {
		return false, err
	}
	return reflect.DeepEqual(j2, j), nil
}
