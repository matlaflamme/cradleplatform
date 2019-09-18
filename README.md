# CMPT 373 Fall 2019 | Group Uranus

## Table of Contents

- [Project Introduction](#project-introduction)
- [Directory Structure](#directory-structure)
- [Build Directions](#build-directions)
- [Run Instructions](#run-instructions)
- [Teams](#teams)
  - [Server](#server)
  - [Android Application](#android-application)
  - [Web Application](#web-application)
- [References](#references)

## Project Introduction

We are Group Uranus part of [CMPT 373](https://opencoursehub.cs.sfu.ca/bfraser/grav-cms/cmpt373/2019-7/project).  
We are developing a software system to be deployed alongside the [CRADLE VSA](http://cradletrial.com/) medical diagnostic device.

## Directory Structure

## Build Directions

> **Important**: When opening the project in IntelliJ, open the `cradlerest/web` directory and not the `cradlerest` root directory itself.

Docker is required to build and run the server application. Docker Desktop can be downloaded from [here](https://www.docker.com/products/docker-desktop), you will need to create a free Docker Hub account to be able to download it. If you have trouble installing Docker Desktop, message Jeremy or Jon and we can help you install it.

If you do not want to install Docker, then it is possible to run the server locally without it (though it is not recommended). If you would like to do this, then message Jeremy and we can go through the setup process together.

Build scripts for both Unix and Windows are provided in the `web` directory (`cradle-web.py` and `cradle-web.ps1` respectively). The following example script commands assume you have Docker installed and that Docker Desktop is running.

> **Important** (Windows Only): If you have the MySQL service running, you may not be able to launch the application with Docker due to MySQL's port already being in use. If this is the case, simply stop the MySQL service and try again.

Build and run the server application:

```
# Unix
$ ./cradle-web.py docker run

# Windows
PS> .\cradle-web.ps1 docker run
```

This command, first compiles the application, packages it into a `jar`, then deploys the application using Docker. If everything worked correctly, then the server should be running in the background (note that your terminal isn't locked waiting for the application to end). The web application is accessible via `http://localhost:8080`.

To stop a running application:

```
# Unix
$ ./cradle-web.py docker stop

# Windows
PS> .\cradle-web.ps1 docker stop
```

To start a stopped application:

```
# Unix
$ ./cradle-web.py docker start

# Windows
PS> .\cradle-web.ps1 docker start
```

To get a fresh start (**this will delete the database and all its data!**):
```
# Unix
$ ./cradle-web.py docker purge

# Windows
PS> .\cradle-web.ps1 docker purge
```

### Advanced

For console level access to a running database use the following command (note that we may update the admin password to something not so silly).

```
docker exec -it web_db_1 mysql -u admin -psuper-strong-password
```

## Teams

### Server

Responsible for developing the server, REST APIs, etc.

- Jeremy
- Jackson
- Jon

### Android Application

Responsible for developing the [Android application](https://csil-git1.cs.surrey.sfu.ca/373-20197-Uranus/cradlemobile).

- Kyle
- Quince

### Web Application

Responsible for developing the web view.

- Linh
- Mathieu
- Parmis

## References

- [Brian Fraser's Android Application Repo](https://csil-git1.cs.surrey.sfu.ca/bfraser/cradlevhtapp-shared)
- [Requirements Document](docs/REQUIREMENTS.md)