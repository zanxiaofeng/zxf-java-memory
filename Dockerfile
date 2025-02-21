FROM openjdk:17-jdk-alpine

COPY target/zxf-java-memory-1.0-SNAPSHOT.jar /my-app.jar

ENTRYPOINT ["java", "-jar", "/my-app.jar"]