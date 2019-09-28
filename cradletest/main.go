package main

import (
	"cradletest/cli"
	"fmt"
)

func main() {
	cli.Parse()
	fmt.Println("host:", cli.Host())
	fmt.Println("port:", cli.Port())
	fmt.Println("url: ", cli.URL())
}
