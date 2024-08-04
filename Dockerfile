FROM maven:3.9.8-eclipse-temurin-21 AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:21-slim

COPY --from=MAVEN_BUILD /build/target/*.jar app.jar

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "app.jar"]