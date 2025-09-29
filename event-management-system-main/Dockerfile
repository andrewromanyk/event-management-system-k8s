FROM openjdk:24-jdk-slim

COPY ./src /app/src
COPY ./gradlew /app/gradlew
COPY ./build.gradle.kts /app/build.gradle.kts
COPY ./settings.gradle.kts /app/settings.gradle.kts
COPY ./gradle /app/gradle

WORKDIR /app

RUN /bin/bash -c 'chmod +x gradlew; ./gradlew build'

CMD ["java", "-jar", "./build/libs/event-management-micro-0.0.1-SNAPSHOT.jar"]
