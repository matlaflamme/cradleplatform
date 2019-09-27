#! /bin/bash

./cradle-web.py local stop-db
wait
./cradle-web.py local purge-db
wait
./cradle-web.py local start-db
wait

echo "db stopped, purged and restarted"

