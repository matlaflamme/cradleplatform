// Package cli implements the command line interface for the application.
package cli

import (
	"flag"
	"strconv"
)

var (
	host     string
	port     uint
	protocol string
)

// Parse registers an parses the global cli variables defined in this file.
func Parse() {
	flag.StringVar(&host, "host", "localhost", "Hostname to run tests on")
	flag.UintVar(&port, "port", 8080, "Port number to run tests on")
	flag.StringVar(&protocol, "protocol", "http", "Web protocol to use when testing")
	flag.Parse()
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

// URL return the test url by combining the relevent command line args.
func URL() string {
	return protocol + "://" + host + ":" + strconv.Itoa(int(port))
}
