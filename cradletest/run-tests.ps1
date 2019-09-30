#
# Convenience script for running the web server's integration tests.
# Requires docker-compose.exe
#
# Returns a non-zero exit code if the tests fail, returns exit code 0 iff all
# tests pass.
#
# Run with -Verbose switch to see logs from all services and not just 'tester'.
#

[CmdletBinding()]
param ()

if ((Split-Path -Leaf -Path $PWD) -ne "cradletest") {
    Write-Error "Script must be run from 'cradletest' directory"
    exit 1
}

# Compile web server to ensure we are testing the latest version.
Push-Location ..\web
Write-Host "Compiling web server..."
.\mvnw.cmd package -DskipTests | Out-Null
if ($LastExitCode -ne 0) {
    Write-Error "Compilation Failed: aborting integration tests"
    Pop-Location
    exit 1
}
Write-Host "Compilation Successful!"
Pop-Location


# Run integration tests within docker.
if ($PSCmdlet.MyInvocation.BoundParameters["Verbose"].IsPresent) {
    # ^ get verbose switch: https://www.mobzystems.com/blog/getting-the-verbose-switch-setting-in-powershell
    docker-compose.exe up --build --force-recreate --abort-on-container-exit --exit-code-from tester    
} else {
    docker-compose.exe -f docker-compose.yml -f docker-compose-silent.yml up --build --force-recreate --abort-on-container-exit --exit-code-from tester
}
$Result = $LastExitCode

# Cleanup containers to have a fresh start the next time tests are run.
docker-compose.exe rm -f

exit $Result
