#!/usr/bin/env python3

import os
import subprocess
import sys


usage = """\
USAGE: cradle-web.py COMMAND

Build script for CRADLE web application

Commands:
  docker    Build or run using Docker
  help      Displays help information
  local     Build or run locally
"""


docker_usage = """\
USAGE: cradle-web.py docker COMMAND

Docker Commands:
  logs      View stdout for servies
  pause     Pause services
  ps        View active services
  purge     Deletes containers, DATABASE WILL BE LOST
  start     Start services
  stop      Stop services
  restart   Restart paused services
  run       Build and run web application
"""


local_usage = """\
USAGE: cradle-web.py local COMMAND

Local Commands:
  build     Build the application
  run       Build and run the application
  start-db  Starts MySQL server using Docker
  stop-db   Stops MysSQL server
  purge-db  Deletes MySQL container, DATABASE WILL BE LOST
"""


def msg_and_exit(msg, err_code = 1):
    print(msg)
    sys.exit(err_code)


def exit_if_empty(args, msg, err_code = 1):
    if len(args) == 0:
        msg_and_exit(msg, err_code)


def exec_cmd(cmd_args):
    proc = subprocess.Popen(cmd_args, shell=False)
    proc.communicate()


def docker_logs():
    exec_cmd(["docker-compose", "logs"])


def docker_pause():
    exec_cmd(["docker-compose", "pause"])


def docker_ps():
    exec_cmd(["docker-compose", "ps"])


def docker_purge():
    print("WARNING: you will lose any data stored in the database")
    answer = input("Do you wish to continue? [y/n] ")
    if answer != "y":
        print("Aborted")
        return
    exec_cmd(["docker-compose", "down"])


def docker_clean():
    print("WARNING: you will lose any data stored in the database")
    answer = input("Do you wish to continue? [y/n] ")
    if answer != "y":
        print("Aborted")
        return
    exec_cmd(["docker", "system", "prune", "-f"])
    exec_cmd(["docker", "volume", "prune", "-f"])

def docker_start():
    exec_cmd(["docker-compose", "start"])
    print("It may take a minute for the server to start up")


def docker_stop():
    exec_cmd(["docker-compose", "stop"])


def docker_restart():
    exec_cmd(["docker-compose", "restart"])


def docker_run():
    # build jar
    exec_cmd(["./mvnw", "package", "-DskipTests"])
    # deploy to docker
    exec_cmd(["docker-compose", "up", "--build", "-d"])
    print("It may take a minute for the server to start up")

def docker_debug():
    # build jar
    exec_cmd(["./mvnw", "package", "-DskipTests"])
    # deploy to docker
    exec_cmd(["docker-compose", "up", "--build"])

def cmd_docker(argv):
    exit_if_empty(argv, docker_usage)
    commands = {
        "logs": docker_logs,
        "pause": docker_pause,
        "ps": docker_ps,
        "purge": docker_purge,
        "clean": docker_clean,
        "start": docker_start,
        "stop": docker_stop,
        "restart": docker_restart,
        "run": docker_run,
        "debug":docker_debug
    }
    cmd = commands.get(argv[0])
    if cmd == None:
        msg_and_exit(docker_logs)
    cmd()


def local_build():
    exec_cmd(["./mvnw", "package"])


def local_run():
    exec_cmd(["./mvnw", "package"])
    exec_cmd(["java", "-jar", "web*.jar"])


mysql_container_name = "local_web_db_1"

def local_start_db():
    exec_cmd([
        "docker", 
        "run",
        "--name=" + mysql_container_name,
        "-p", "3306:3306",
        "--mount",
        "type=bind,src=" + os.getcwd() + "/scripts/db-initializer-scripts,dst=/docker-entrypoint-initdb.d",
        "-d",
        "mysql/mysql-server"])


def local_stop_db():
    exec_cmd(["docker", "stop", mysql_container_name])


def local_purge_db():
    exec_cmd(["docker", "rm", mysql_container_name])


def cmd_local(argv):
    exit_if_empty(argv, local_usage)
    commands = {
        "build": local_build,
        "run": local_run,
        "start-db": local_start_db,
        "stop-db": local_stop_db,
        "purge-db": local_purge_db
    }
    cmd = commands.get(argv[0])
    if cmd == None:
        msg_and_exit(local_usage)
    cmd()


def cmd_help(argv):
    print(usage)


if __name__ == "__main__":
    if sys.argv[0] != "./cradle-web.py":
        print("This script must be run from the same directory that it is located in")
        sys.exit(1)
    commands = {
        "docker": cmd_docker,
        "help": cmd_help,
        "local": cmd_local
    }
    argv = sys.argv
    if len(argv) == 1:
        cmd_help(argv)
        sys.exit(1)
    cmd = commands.get(sys.argv[1])
    if cmd == None:
        print("%s is not a valid command" % sys.argv[1])
        cmd_help(argv[2:])
        sys.exit(1)
    cmd(argv[2:])
