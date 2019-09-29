# CMPT 373 Fall 2019 | Group Uranus

## Table of Contents

- [Project Introduction](#project-introduction)
- [Directory Structure](#directory-structure)
- [Build Directions](#build-directions)
- [Run Instructions](#run-instructions)
- [Deployment Instructions](#deployment-instructions)
- [Teams](#teams)
  - [Server](#server)
  - [Android Application](#android-application)
  - [Web Application](#web-application)
- [References](#references)

## Project Introduction

We are Group Uranus part of [CMPT 373](https://opencoursehub.cs.sfu.ca/bfraser/grav-cms/cmpt373/2019-7/project).  
We are developing a software system to be deployed alongside the [CRADLE VSA](http://cradletrial.com/) medical diagnostic device.

## Directory Structure

```
├── docs
│   ├── api
│   └── nametag
│       └── Meeting\ notes
├── web
│   ├── res
│   ├── scripts
│   │   └── db-initializer-scripts
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── cradlerest
│   │   │   │           └── web
│   │   │   │               ├── controller
│   │   │   │               │   └── error
│   │   │   │               ├── model
│   │   │   │               │   └── builder
│   │   │   │               ├── service
│   │   │   │               │   ├── config
│   │   │   │               │   ├── repository
│   │   │   │               │   └── storage
│   │   │   │               └── util
│   │   │   └── resources
│   │   │       ├── static
│   │   │       │   └── img
│   │   │       └── templates
│   │   └── test
│   │       ├── java
│   │       │   └── com
│   │       │       └── cradlerest
│   │       │           └── web
│   │       └── resources
│   ├── target
│   │   ├── classes
│   │   │   ├── META-INF
│   │   │   ├── com
│   │   │   │   └── cradlerest
│   │   │   │       └── web
│   │   │   │           ├── controller
│   │   │   │           │   └── error
│   │   │   │           ├── model
│   │   │   │           │   └── builder
│   │   │   │           ├── service
│   │   │   │           │   ├── config
│   │   │   │           │   ├── repository
│   │   │   │           │   └── storage
│   │   │   │           └── util
│   │   │   ├── static
│   │   │   │   └── img
│   │   │   └── templates
│   │   ├── generated-sources
│   │   │   └── annotations
│   │   ├── generated-test-sources
│   │   │   └── test-annotations
│   │   └── test-classes
│   │       └── com
│   │           └── cradlerest
│   │               └── web
│   └── upload-dir
└── webapp
    ├── css
    ├── img
    └── js
```
```
Please update directory structure if you change it.
Using tree command in bash
``` tree -d ```
```


## Build Directions

> **Important**: When opening the project in IntelliJ, open the `cradlerest/web` directory and not the `cradlerest` root directory itself.

Docker is required to build and run the server application. Docker Desktop can be downloaded from [here](https://www.docker.com/products/docker-desktop), you will need to create a free Docker Hub account to be able to download it. If you have trouble installing Docker Desktop, message Jeremy or Jon and we can help you install it.

If you do not want to install Docker, then it is possible to run the server locally without it (though it is not recommended). If you would like to do this, then message Jeremy and we can go through the setup process together.

Build scripts for both Unix and Windows are provided in the `web` directory (`cradle-web.py` and `cradle-web.ps1` respectively). The following example script commands assume you have Docker installed and that Docker Desktop is running.

> **Important** (Windows Only): If you have the MySQL service running, you may not be able to launch the application with Docker due to MySQL's port already being in use. If this is the case, simply stop the MySQL service and try again.

## Running the Web Application in IntelliJ

The following configuration steps must be done to allow the web application to run properly in IntelliJ.

1. Start the MySQL server with Docker, the database server must be running for the application to work (it can be stopped by using the `stop-db` command instead of `start-db`).

```
# Unix
$ ./cradle-web.py local start-db

# Windows
PS> .\cradle-web.ps1 local start-db
```

2. Configure environment variables in IntelliJ

* Go to: Run > Edit Configurations...
* With "WebApplication" selected on the left hand side, copy the bellow text into the "Environment Variables: " text box on the right.

```
DB_HOST=localhost;DB_PORT=3306;DB_USER=admin;DB_PASSWORD=super-strong-password
```

* Click "OK" then build and run the application though IntelliJ like normal.

## Running the Web Application in Docker 

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

If the server was started without docker-compose, use `local_web_db_1` for the container name.

## Deployment Instructions

The live production web application can be accessed at:

```
http://cmpt373.csil.sfu.ca:8087
```

Administrative access to our VM is available via SSH using your sfu computing id:

```
$ ssh -p 1007 <sfu computing id>@cmpt373.csil.sfu.ca
```

The production clone of our repository is located under the system root at:

```
/prod/cradleplatform
```

The project folder and its contents are owned by `root` but should be fully 
accessible (read/write/execute) by everyone.

If attempting to build the application for the first time on the VM you may need 
to create yourself a home directory as there aren't any for our users. Maven 
requires a home directory to place cache files an such. So, if you're logging in 
for the first time, run the following to create a home directory for yourself:

```
$ sudo mkdir /home/$USER
$ sudo chown $USER:users /home/$USER
```

Once you have a home directory, you can use our build script to compile and run 
the application with Docker just like on your local machine:

```
# From /prod/cradleplatform/web
$ ./cradle-web.py docker run
```

I'm not sure how the system will respond if multiple users try and run the web 
application at the same time (it probably won't work). So please ensure that the 
server is not already running before starting it up.

### Deployment Setup History

In case something goes horribly wrong and the server is rebuilt from scratch, 
the following are a list of commands which were used to get the server up and 
running.

``` bash
#
# The following are the commands needed to setup the VM to run the webserver
# They only need to be run once when initializing a fresh OS install
#

# Clone repository
sudo mkdir /prod
sudo chmod a+wrx /prod
cd /prod
git clone https://csil-git1.cs.surrey.sfu.ca/373-20197-Uranus/cradleplatform.git
cd /prod/cradleplatform
sudo chown -R root:users  .
sudo chmod -R -g+wrx .

# Add user to docker group (I've done this for all of us)
sudo usermod -aG docker $UESR

# Install Java and Maven
sudo apt install openjdk-11-jdk
sudo apt install maven

# Docker was already installed, but just in case
sudo apt install docker

# Install Docker Compose
sudo apt install docker-compose

# Create Home Directory 
sudo mkdir /home/$USER
sudo chown $USER:users /home/$USER

# Test compile
cd /prod/cradleplatform/web
./mvnw package

# Ensure other users can access the build artifact directory
cd /prod/cradleplatform
sudo chown -R root:users .
sudo chmod -R -g+wrx .

# Run web application
cd /prod/cradleplatform/web
./cradlerest docker run
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