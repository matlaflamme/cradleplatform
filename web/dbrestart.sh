#! /bin/bash
if [[ "$OSTYPE" -eq "linux-gnu" || "$OSTYPE" -eq "darwin" ]]; then
    ./cradle-web.py local stop-db
    wait
    ./cradle-web.py local purge-db
    wait
    ./cradle-web.py local start-db
    wait

    echo "db stopped, purged and restarted"

elif [[ "$OSTYPE" -eq "cygwin" || "$OSTYPE" -eq "msys" ]]; then
    ./cradle-web.ps1 local stop-db
    wait
    ./cradle-web.ps1 local purge-db
    wait
    ./cradle-web.ps1 local start-db
    wait

    echo "db stopped, purged and restarted"

else
    echo "Your OS is not supported by this script"
fi

