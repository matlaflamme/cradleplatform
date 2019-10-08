package jsonutil

import (
	"bytes"
	"encoding/json"
	"io"
)

// PrettyFormat decodes some JSON data and returns it as a JSON string in a
// nice and readable format.
func PrettyFormat(j io.Reader) (*string, error) {
	var obj interface{}
	decoder := json.NewDecoder(j)
	if err := decoder.Decode(&obj); err != nil {
		return nil, err
	}

	buffer := new(bytes.Buffer)
	encoder := json.NewEncoder(buffer)
	encoder.SetIndent("", "  ")
	if err := encoder.Encode(obj); err != nil {
		return nil, err
	}

	result := buffer.String()
	return &result, nil
}
