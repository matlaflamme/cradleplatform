FROM openjdk:11
VOLUME /tmp
COPY target/*.jar app.jar
COPY ./scripts/wait-for-it.sh .
# Wait for db service to setup before launching Spring
#   https://stackoverflow.com/a/54145469
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java","-jar","/app.jar"]
