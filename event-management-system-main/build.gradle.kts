plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.gorylenko.gradle-git-properties") version "2.5.3"
}

gitProperties {
	dotGitDirectory = file("${project.projectDir}/../.git")
}

group = "ua.edu.ukma"
version = "0.0.1-SNAPSHOT"
description = "Modulith implementation of EventManagement System"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

extra["springModulithVersion"] = "1.4.1"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.modulith:spring-modulith-starter-core")
	implementation("org.springframework.modulith:spring-modulith-starter-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-mail:3.5.5")
	implementation("org.springframework.retry:spring-retry:2.0.12")
	implementation("org.springframework:spring-aop:6.2.11")
	implementation("org.springframework.boot:spring-boot-starter-activemq")

	implementation("io.micrometer:micrometer-registry-prometheus")

	implementation("org.apache.activemq:activemq-broker")

	implementation("com.h2database:h2")
	implementation("org.postgresql:postgresql:42.7.8")

	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

	implementation("org.modelmapper:modelmapper:3.2.4")

	implementation("org.hibernate.validator:hibernate-validator")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("de.codecentric:spring-boot-admin-starter-client:3.5.5")

	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springframework.cloud:spring-cloud-starter-config:4.3.0")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	maxParallelForks = 1
}


