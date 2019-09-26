# CradleREST

## Getting Started

* Install [Docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/)
* Install [Docker Compose](https://linuxize.com/post/how-to-install-and-use-docker-compose-on-ubuntu-18-04/)
* Install MySQL
  * [Windows Installer](https://dev.mysql.com/downloads/installer/)
  * macOS with Homebrew: `brew install mysql`
  * Ubuntu: `sudo apt install mysql` (probably)
  
### Creating a Local Database

Ensure that the MySql server daemon is running, on Unix systems (and possible Windows as well)
this can be done with the following command

```
$ mysql.server start
``` 

Log into MySQL with the default admin user using a terminal or CMD/Powershell on Windows

```
$ mysql -u root --skip-password
```

Create the database needed by the server

```
mysql> CREATE DATABASE cradlerest;
```

And that's it, the server manages the database schema and data.

> **Important**: the server will crash if you try and enter data manually into the database
>for some reason, so only use the REST API to create and delete data.

### Build and run locally

```
./mvnw package
java -jar target/[package name].jar
```
 or just use intelliJ
 
 usn: user1
 
 pwd: password