#
# CRADLE Web PowerShell Build Script
#
# If you are unable to run this script, try the following command:
#   set-executionpolicy remotesigned
# see: https://superuser.com/a/106363

param(
    [Parameter(Position=0, Mandatory=$false)]
    [string] $Command,
    [Parameter(Position=1, Mandatory=$false)]
    [string] $SubCommand
)

function Invoke-Docker {
    param (
        [Parameter(Position=0, Mandatory=$false)]
        [string] $DockerCommand
    )
    
    switch ($DockerCommand) {
        "logs" { docker-compose.exe logs }
        "pause" { docker-compose.exe pause }
        "ps" { docker-compose.exe ps }
        "purge" {
            Write-Host "WARNING: you will lose any data stored in the databse"
            $answer = Read-Host -Prompt "Do you still wish to continue? [y/n]"
            if ($answer -ne "y") {
                Write-Host "Aborted"
                return
            }
            docker-compose.exe down
        }
        "start" { docker-compose.exe start }
        "stop" { docker-compose.exe stop }
        "restart" { docker-compose.exe restart }
        "run" {
            .\mvnw.cmd package -DskipTests
            docker-compose.exe up --build -d
        }
        Default {
            Write-Host "USAGE: cradle-web.py docker COMMAND

Docker Commands:
    logs      View stdout for servies
    pause     Pause services
    ps        View active services
    purge     Deletes containers, DATABASE WILL BE LOST
    start     Start services
    stop      Stop services
    restart   Restart paused services
    run       Build and run web application
            "
        }
    }
}

function Invoke-Local {
    param (
        [Parameter(Position=0, Mandatory=$false)]
        [string] $LocalCommand
    )

    $MySQLContainerName = "local_web_db_1"
    switch ($LocalCommand) {
        "build" { .\mvnw.cmd package }
        "run" {
            .\mvnw.cmd package
            java -jar target\web*.jar
        }
        "start-db" {
            docker.exe run --name $MySQLContainerName -p "3306:3306" --mount "type=bind,src=$PWD\scripts\db-initializer-scripts,dst=/docker-entrypoint-initdb.d" -d "mysql/mysql-server"
        }
        "stop-db" {
            docker.exe stop $MySQLContainerName
        }
        "purge-db" {
            docker.exe rm $MySQLContainerName
        }
        Default {
            Write-Host "USAGE: cradle-web.py local COMMAND

Local Commands:
  build     Build the application
  run       Build and run the application
  start-db  Starts MySQL server using Docker
  stop-db   Stops MysSQL server
  purge-db  Deletes MySQL container, DATABASE WILL BE LOST"
        }
    }
}

switch ($Command) {
    "docker" { Invoke-Docker $SubCommand }
    "local" { Invoke-Local $SubCommand }
    Default {
        Write-Host "USAGE: cradle-web.py COMMAND

Build script for CRADLE web application

Commands:
    docker    Build or run using Docker
    help      Displays help information
    local     Build or run locally
        "
    }
}
