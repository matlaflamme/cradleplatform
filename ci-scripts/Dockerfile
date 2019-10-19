# Compile Project
FROM maven:latest
COPY web/* /
RUN mvn compile

# Run Project
FROM openjdk:11
VOLUME /tmp
COPY --from=0 /target/*.jar .
CMD ["./wait-for-it.sh", "db:3306", "--", "java","-jar","/app.jar"]
