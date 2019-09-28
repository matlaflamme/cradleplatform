package main

import (
	"cradletest/cli"
	"cradletest/runtime"
	"cradletest/runtime/serialrt"
	"cradletest/suite"
	"fmt"
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
	cli.Parse()
	path := cli.InputFilePath()

	ts, err := suite.FromFile(path)
	if err != nil {
		fmt.Println(err)
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
