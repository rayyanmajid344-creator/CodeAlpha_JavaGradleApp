# CodeAlpha Java Gradle App

A Task Manager REST API built with Java 25, Gradle, and Javalin, with a CI/CD pipeline using GitHub Actions.
Built for the CodeAlpha DevOps Internship (Task 3).

## Features
- REST API: `GET /tasks`, `POST /tasks`, `GET /health`
- Dependencies managed with a Gradle version catalog (`gradle/libs.versions.toml`)
- Unit tests with JUnit 5
- CI/CD: every push is built, tested, and packaged automatically by GitHub Actions

## Run locally
```
./gradlew build    # compile and run tests
./gradlew run      # start the server on http://localhost:7070
```

## Add a task
```
curl -X POST http://localhost:7070/tasks -H "Content-Type: application/json" -d '{"title":"My task"}'
```