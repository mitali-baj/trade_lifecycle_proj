FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app-3.3.1.jar
ENTRYPOINT ["java","-jar","/app-3.3.1.jar"]
