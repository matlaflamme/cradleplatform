// Package cli implements the command line interface for the application.
package cli

import (
	"flag"
	"fmt"
	"os"
	"strconv"
)

var (
	host     string
	port     uint
	protocol string
	mode     string
)

// Parse registers an parses the global cli variables defined in this file.
func Parse() {
	flag.StringVar(&host, "host", "localhost", "Hostname to run tests on")
	flag.UintVar(&port, "port", 8080, "Port number to run tests on")
	flag.StringVar(&protocol, "protocol", "http", `Web protocol: either "http" or "https"`)
	flag.StringVar(&mode, "mode", "serial", `Execution mode: either "serial" or "parallel"`)
	flag.Usage = usage
	flag.Parse()
	validate()
}

// Host is the hostname to run tests on, e.g., localhost.
func Host() string {
	return host
}

// Port is the port number to run tests on, e.g., 8080.
func Port() uint {
	return port
}

// Protocol denotes the web protocol to use when testing, either "http" or "https".
func Protocol() string {
	return protocol
}

// URL returns the test url by combining the relevent command line args.
func URL() string {
	return protocol + "://" + host + ":" + strconv.Itoa(int(port))
}

// Mode returns the runtime mode to use when testing.
func Mode() string {
	return mode
}

// InputFilePath returns the path to the XML file to use as input.
func InputFilePath() string {
	if flag.NArg() == 0 {
		panic("no input file provided")
	}
	return flag.Arg(0)
}

// ref: https://stackoverflow.com/a/31873508
func usage() {
	fmt.Print("Integration testing tool for cradle-rest web application\n\n")
	fmt.Print("\nUsage:\n\n")
	fmt.Println("        cradletest [OPTIONS] <input file>")
	fmt.Print("\nOPTIONS\n\n")
	flag.PrintDefaults()
}

func validate() {
	fail := func() {
		flag.Usage()
		os.Exit(1)
	}

	if flag.NArg() == 0 {
		fail()
	}
	if protocol != "http" && protocol != "https" {
		fail()
	}
	if mode != "serial" && mode != "parallel" {
		fail()
	}
}
