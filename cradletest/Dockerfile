#
# NOTE: The context for this dockerfile must be the parent directory (..)
#

FROM golang:latest
WORKDIR /app

# Download dependencies
#   Currently there is none, but in case some are added later
COPY ./cradletest/go.mod .
RUN go mod download

# Copy source code and compile
COPY ./cradletest .
RUN go build -o cradletest .

# Copy wait-for-it script
COPY ./web/scripts/wait-for-it.sh .

# Run testsuite.xml
CMD ["./wait-for-it.sh", "-t", "30", "-q", "web:8080", "--", \
    "./cradletest", "-host", "web", "testsuite.xml"]
