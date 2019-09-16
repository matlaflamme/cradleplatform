#!/bin/sh
mvn compile
mvn package
# java -jar target/web-0.0.1-SNAPSHOT.jar

# mvn clean install
docker build -t myorg/myapp .
docker run -p 8080:8080 myorg/myapp

