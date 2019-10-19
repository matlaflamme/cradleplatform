#!/bin/sh

docker network rm ci-network 2> /dev/null
docker network prune -f
docker container prune -f

docker network create -d bridge ci-network 
docker build -f ci-scripts/Dockerfile-db . -t 'cradlerest/db:latest'
docker run --privileged --name db --network ci-network -d 'cradlerest/db:latest'
docker build --no-cache -f ci-scripts/Dockerfile-web . -t 'cradlerest/web:latest'
docker run --name web --network ci-network -d 'cradlerest/web:latest'
docker build -f cradletest/Dockerfile . -t 'cradlerest/tester:latest'
docker run --name tester --network ci-network 'cradlerest/tester:latest'
EXIT_CODE=$?

docker container stop db > /dev/null
docker container rm db > /dev/null
docker container stop web > /dev/null
docker container rm web > /dev/null
docker network rm ci-network > /dev/null

exit $EXIT_CODE
