FROM gradle:jdk24-corretto AS build

WORKDIR /app/main

COPY ./.git /app/.git
COPY ./event-management-system-main/src /app/main/src
COPY ./event-management-system-main/build.gradle.kts /app/main/build.gradle.kts
COPY ./event-management-system-main/settings.gradle.kts /app/main/settings.gradle.kts
COPY ./event-management-system-main/gradle /app/main/gradle

RUN gradle build

FROM openjdk:24-jdk-slim

WORKDIR /app

COPY --from=build /app/main/build/libs/event-management-main-0.0.1-SNAPSHOT.jar /app/event-management-main-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/event-management-main-0.0.1-SNAPSHOT.jar"]
