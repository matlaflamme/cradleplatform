#!/bin/bash

#
# Convenience script for running the web server's integration tests.
# Requires docker-compose.exe
#
# Returns a non-zero exit code if the tests fail, returns exit code 0 iff all
# tests pass.
#
# Run with --verbose switch to see logs from all services and not just 'tester'.
#

DIR_BASENAME=$(basename $(pwd))
if [[ $DIR_BASENAME != "cradletest" ]]; then
    echo "Script must be run from 'cradletest' directory"
    exit 1
fi

# Compile web server to ensure we are testing the latest version.
DIR_SAVE=$(pwd)
cd ../web
echo "Compiling web server..."
./mvnw package -DskipTests > /dev/null
if [[ $? != 0 ]]; then
    echo "Compilation Failed: aborting integration tests"
    cd $DIR_SAVE
    exit 1
fi
echo "Compilation Successful!"
cd $DIR_SAVE

# Run integration tests within docker.
if [[ $1 == "--verbose" ]]; then
    docker-compose up --build --force-recreate --abort-on-container-exit --exit-code-from tester
else
    docker-compose -f docker-compose.yml -f docker-compose-silent.yml up --build --force-recreate --abort-on-container-exit --exit-code-from tester
fi
RESULT=$?

# Cleanup containers to have a fresh start the next time tests are run.
docker-compose rm -f

exit $RESULT