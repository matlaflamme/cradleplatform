package main

import (
	"cradletest/cli"
	"cradletest/runtime"
	"cradletest/runtime/serialrt"
	"cradletest/suite"
	"crypto/tls"
	"fmt"
	"net/http"
	"os"
)

func configuredRuntime() runtime.Runtime {
	switch cli.Mode() {
	case "serial":
		return serialrt.New()
	default:
		panic("invlaid mode: " + cli.Mode())
	}
}

func main() {
	// Disable SLL certificate validation globally.
	// 	ref: https://stackoverflow.com/a/12122718
	http.DefaultTransport.(*http.Transport).TLSClientConfig = &tls.Config{
		InsecureSkipVerify: true,
	}

	cli.Parse()
	path := cli.InputFilePath()

	ts, err := suite.FromFile(path)
	if err != nil {
		fmt.Println(err)
	}
	if !suite.Validate(ts, os.Stdout) {
		os.Exit(1)
	}

	env := configuredRuntime()
	results, err := env.Run(*ts)
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
	if !runtime.ProcessResults(results, os.Stdout) {
		os.Exit(1)
	}
}
