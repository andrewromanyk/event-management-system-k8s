FROM gradle:jdk24-corretto AS build

WORKDIR /app

COPY ./src /app/src
COPY ./build.gradle /app/build.gradle
COPY ./settings.gradle /app/settings.gradle
COPY ./gradle /app/gradle

RUN gradle build

FROM amazoncorretto:24.0.2-alpine3.22

WORKDIR /app

COPY --from=build /app/build/libs/event-management-admin-0.0.1-SNAPSHOT.jar /app/event-management-admin-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/event-management-admin-0.0.1-SNAPSHOT.jar"]