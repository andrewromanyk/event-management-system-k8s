FROM gradle:jdk24-corretto AS build

WORKDIR /app

COPY ./src /app/src
COPY ./gradlew /app/gradlew
COPY ./build.gradle /app/build.gradle
COPY ./settings.gradle /app/settings.gradle
COPY ./gradle /app/gradle

RUN chmod +x gradlew && gradle build

FROM openjdk:24-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/event-management-admin-0.0.1-SNAPSHOT.jar /app/event-management-admin-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/event-management-admin-0.0.1-SNAPSHOT.jar"]