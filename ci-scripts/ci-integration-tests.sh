#!/bin/sh

docker network rm ci-network 2> /dev/null
docker network prune -f
docker container prune -f

docker network create -d bridge ci-network 

printf 'Building database container... '
docker build -f ci-scripts/Dockerfile-db . -t 'cradlerest/db:latest' > /dev/null
echo 'done'

printf 'Starting database container... '
DB_CONTAINER=$(docker run --privileged --name db --network ci-network -d 'cradlerest/db:latest')
echo 'done'

printf 'Building web application container... '
docker build -f ci-scripts/Dockerfile-web . -t 'cradlerest/web:latest' > /dev/null
echo 'done'

printf 'Starting web application container... '
WEB_CONTAINER=$(docker run --name web --network ci-network -d 'cradlerest/web:latest')
echo 'done'

printf 'Building tester application... '
docker build -f cradletest/Dockerfile . -t 'cradlerest/tester:latest' > /dev/null
echo 'done'

printf 'Running tester application...\n\n'
docker run --name tester --network ci-network 'cradlerest/tester:latest'
EXIT_CODE=$?

if [[ $EXIT_CODE != 0 ]]; then
    docker logs $WEB_CONTAINER
fi

docker container stop db > /dev/null
docker container rm db > /dev/null
docker container stop web > /dev/null
docker container rm web > /dev/null
docker network rm ci-network > /dev/null

exit $EXIT_CODE
