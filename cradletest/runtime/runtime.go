// Package runtime implements a generic runtime interface for executing a
// test suite.
package runtime

import (
	"cradletest/suite"
	"fmt"
	"io"
)

// Result holds information about the result of a single testcase.
type Result struct {
	Name    string
	Passed  bool
	Message string
}

// Runtime defines the interface for a test suite execution runtime.
//
// Defining Runtime as an interface allows us to easily switch between a serial
// or parallel runtime environment for testsuites.
type Runtime interface {

	// Run executes a TestSuite returning a list of results.
	//
	// Returns an error only if there is some internal error when executing the
	// test suite, not if a test fails.
	Run(suite.TestSuite) ([]Result, error)
}

// ProcessResults writes a textual description of the results to some writer
// returning true if all tests pass and false otherwise.
func ProcessResults(results []Result, w io.Writer) bool {
	if results == nil {
		panic("results is nil")
	}

	resultCount := len(results)
	failCount := 0

	for _, r := range results {
		fmt.Fprintf(w, "Test '%s'... ", r.Name)
		if r.Passed {
			fmt.Fprintln(w, "passed")
		} else {
			fmt.Fprintln(w, "FAILED")
			fmt.Fprintln(w, r.Message)
			failCount++
		}
		fmt.Fprintln(w)
	}

	if failCount != 0 {
		fmt.Fprintf(w, "FAILED %d/%d tests\n", failCount, resultCount)
		return false
	}

	fmt.Fprintln(w, "Passed all tests")
	return true
}
