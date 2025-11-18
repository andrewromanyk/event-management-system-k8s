FROM gradle:jdk21-corretto AS build

WORKDIR /app

COPY ./src /app/src
COPY ./build.gradle.kts /app/build.gradle.kts
COPY ./settings.gradle.kts /app/settings.gradle.kts
COPY ./gradle /app/gradle

RUN gradle build -x test

FROM amazoncorretto:21.0.9-al2023-headless

WORKDIR /app

COPY --from=build /app/build/libs/event-management-system-config-0.0.1-SNAPSHOT.jar /app/event-management-config-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/event-management-config-0.0.1-SNAPSHOT.jar"]