# Use OpenJDK instead of basic Java as the java:8 image contains a memory leak affecting this application.
FROM openjdk:8

ARG APP_JAR
ADD ${APP_JAR} app.jar

# Expose default Tomcat port
EXPOSE 8080

# Run Java application. Set memory limits to stay well within the desired 512m maximum.
ENTRYPOINT [ "java", "-jar", "app.jar", "-XX:MaxRAM=384m", "-Xms64m", "-Xmx256m"]
