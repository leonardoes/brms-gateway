# Gemini API - Architectural Overview

This document provides a comprehensive overview of the architectural design of the Gemini API, a Spring Boot-based solution.

## Core Components

The solution is primarily composed of two key components:

*   **brms-gateway-globalvida-cotacao**: This is the main Spring Boot application, serving as the entry point for all API requests. It handles incoming traffic, orchestrates the necessary business logic, and communicates with other services.

*   **brms-rules-globalvida-cotacao**: This component contains the core business rules and logic, likely implemented using a Business Rules Management System (BRMS). It's responsible for executing complex business decisions and returning the results to the gateway.

## Project Structure

The project is organized into a multi-module Maven project, with the following key directories:

*   **brms-gateway-globalvida-cotacao**:
    *   `src/main/java`: Contains the main application source code, including controllers, services, and other Java classes.
    *   `src/main/resources`: Houses application configuration files, such as `application.properties` and `log4j2-spring.xml`.
    *   `chart`: Includes Helm charts for deploying the application to a Kubernetes cluster.
    *   `config`: Stores additional configuration files, potentially for different environments.
    *   `target`: This directory is where the compiled application artifacts (e.g., JAR files) are stored.

*   **brms-rules-globalvida-cotacao**:
    *   `chart`: Similar to the gateway, this directory contains Helm charts for deployment.
    *   `config`: Holds configuration files specific to the rules engine.
    *   `brms-GlobalvidaCotacao.jar`: The compiled JAR file containing the business rules.

## Key Technologies

The Gemini API leverages a modern technology stack to deliver a robust and scalable solution:

*   **Spring Boot**: The core framework for building the application, providing features like dependency injection, auto-configuration, and embedded web server support.
*   **Java**: The primary programming language used throughout the project.
*   **Maven**: A powerful build automation tool for managing project dependencies and building the application.
*   **Docker**: The solution is containerized using Docker, enabling consistent and portable deployments across different environments.
*   **Kubernetes**: The application is designed to be deployed and managed in a Kubernetes cluster, providing scalability, resilience, and automated rollouts.
*   **Jenkins & Bitbucket Pipelines**: The project includes configuration files for both Jenkins and Bitbucket Pipelines, indicating a CI/CD (Continuous Integration/Continuous Deployment) approach to building, testing, and deploying the application.

## Deployment

The application is designed for cloud-native deployment, with the following key features:

*   **Containerization**: The use of Docker allows the application to be packaged into a lightweight, portable container.
*   **Orchestration**: Kubernetes is used to orchestrate the deployment, scaling, and management of the application containers.
*   **CI/CD**: The inclusion of Jenkins and Bitbucket Pipelines configuration files suggests a fully automated CI/CD pipeline for seamless deployments.

## Conclusion

The Gemini API is a well-architected, cloud-native solution that leverages modern technologies to deliver a scalable, resilient, and maintainable application. The clear separation of concerns between the gateway and the rules engine, combined with the use of containerization and orchestration, makes it a robust and future-proof solution.
