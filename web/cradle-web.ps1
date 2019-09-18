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

    switch ($LocalCommand) {
        "build" { .\mvnw.cmd }
        "run" {
            .\mvnw.cmd package
            java -jar target\web*.jar
        }
        Default {}
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
