# event-management-system-k8s

A modular Spring Boot microservice application implementing an event management system (domain, APIs and UI). 
The repository contains each service as a separate module and includes a ready Kubernetes deployment (kuber.yaml) and monitoring provisioning (Prometheus + Grafana).
This project is a rewrite of a Monolithic [`andrewromanyk/event-management-system`](https://github.com/andrewromanyk/event-management-system)

## Modules
- [`event-management-system-main`](/event-management-system-main)  
  The core backend microservice (domain and public API). Contains the Java/Spring sources, Gradle build, and Dockerfile — this is the main application module other services depend on.

- [`event-management-system-building`](/event-management-system-building)  
  The "building" microservice that implements event-building related business logic and APIs.

- [`event-management-system-config`](/event-management-system-config)  
  Centralized configuration server (Spring Cloud Config) for serving per-service configuration at runtime.

- [`event-management-eureka`](/event-management-eureka)  
  Service discovery (Eureka) used by the microservices to discover and communicate with each other.

- [`event-management-system-gateway`](/event-management-system-gateway)  
  API gateway / edge service that routes external requests into the microservice mesh.

- [`event-management-system-admin`](/event-management-system-admin)  
  Spring Boot Admin instance for management and monitoring of registered service instances.

- [`event-management-system-views`](/event-management-system-views)  
  Frontend / views module that provides the user UI and consumes backend APIs.

## Other repository pieces
- [`kuber.yaml`](/kuber.yaml)  
  A single-file Kubernetes manifest that deploys Postgres, each microservice,
  Prometheus, Grafana, ConfigMaps for monitoring, and LoadBalancer Services for external access.
  It also wires common env vars (`SPRING_DATASOURCE_*`, service URLs, actuator/prometheus endpoints) used by the Spring apps.

- [`grafana-config.json`](/grafana-config.json)  
  Pre-provisioned Grafana dashboard definitions (JVM, DB connections, CPU, network, HikariCP metrics) used by the Grafana deployment.

## Images referenced in kuber.yaml
> *Note: versions may differ*
- andrewromanyk/spring-micro-build (spring-micro-build)
- andrewromanyk/spring-micro-main (spring-micro-main)
- andrewromanyk/spring-micro-admin (spring-micro-admin)
- postgres (db)
- prom/prometheus (prometheus)
- grafana/grafana (grafana)
