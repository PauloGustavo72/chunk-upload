FROM maven:3-jdk-8-alpine

COPY target/chunk-upload-server*.jar chunk-upload-server.jar

ENTRYPOINT [ "java", "-jar", "/chunk-upload-server.jar"]
EXPOSE 8080