FROM maven:3.8.5-openjdk-11 AS build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN mvn package

FROM openjdk:11
COPY --from=build /usr/src/myapp/target/trade-simulation-0.0.1-SNAPSHOT.jar /usr/app/trade-simulation.jar
WORKDIR /usr/app
CMD ["java", "-jar", "trade-simulation.jar"]