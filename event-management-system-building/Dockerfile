FROM gradle:jdk24-corretto AS build

WORKDIR /app/main

COPY ./.git /app/.git
COPY ./event-management-system-building/src /app/main/src
COPY ./event-management-system-building/build.gradle.kts /app/main/build.gradle.kts
COPY ./event-management-system-building/settings.gradle.kts /app/main/settings.gradle.kts
COPY ./event-management-system-building/gradle /app/main/gradle
COPY ./event-management-system-building/certs /app/main/certs

RUN gradle build

FROM amazoncorretto:24.0.2-alpine3.22

WORKDIR /app

COPY --from=build /app/main/build/libs/event-management-building-0.0.1-SNAPSHOT.jar /app/event-management-build-0.0.1-SNAPSHOT.jar
COPY --from=build /app/main/certs /app/certs

CMD ["java", "-jar", "/app/event-management-build-0.0.1-SNAPSHOT.jar"]

#  /d/naukma/4/Spring/modules (main)
# $ docker build -t andrewromanyk/spring-micro-build:3.0.0 . -f ./event-management-system-building/Dockerfile