FROM openjdk:8

COPY target/zxf-java-memory-1.0-SNAPSHOT.jar /my-app.jar

ENTRYPOINT ["java", "-XX:+UseG1GC", "-Xms256M", "-Xmx1024M", "-XshowSettings", "-XX:+PrintFlagsFinal", "-XX:NativeMemoryTracking=detail","-jar", "/my-app.jar"]