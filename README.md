# job4j_social_media_api

![Build Status](https://github.com/JenBrainnet/job4j_social_media_api/actions/workflows/maven.yml/badge.svg)

## Project Description

Social Media API is a training REST API for a social media platform.
The application will allow users to register, sign in, create posts, subscribe to other users, send friend requests, and exchange messages.
The activity feed will be based on user subscriptions and will show recent posts from followed users.
The project is developed as part of the Job4j course.

## Technology Stack

- Java 21
- Spring Boot 4.0.6
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- Liquibase
- Maven
- Checkstyle
- Swagger / OpenAPI

## Environment Requirements

- Java 21
- Maven 3.8+
- PostgreSQL 14+

## Project Launch

Create the database:

```sql
CREATE DATABASE social_media;
```

Run the application:

```bash
  mvn spring-boot:run
```
