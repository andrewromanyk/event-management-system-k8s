FROM gradle:jdk21-corretto AS build

WORKDIR /app

COPY ./src /app/src
COPY ./build.gradle /app/build.gradle
COPY ./settings.gradle /app/settings.gradle
COPY ./gradle /app/gradle

RUN gradle build

FROM amazoncorretto:21.0.9-al2023-headless

WORKDIR /app

COPY --from=build /app/build/libs/event-management-system-views-0.0.1-SNAPSHOT.jar /app/event-management-views-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/event-management-views-0.0.1-SNAPSHOT.jar"]