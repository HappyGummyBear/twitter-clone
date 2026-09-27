# Twitter Clone

A backend Twitter/X clone built with **Java and Spring Boot**. The application provides REST APIs for users, tweets, and media uploads, with Amazon S3 used for image and video storage.

## Features

* Create and manage users
* Create, retrieve, and delete tweets
* User-to-tweet relationships
* Image and video uploads
* Amazon S3 presigned URLs
* Request validation
* PostgreSQL persistence

## Technologies

* **Java 17**
* **Spring Boot**
* **Spring Web MVC** — REST APIs
* **Spring Data JPA / Hibernate** — Database access and ORM
* **Jakarta Validation** — Request validation
* **PostgreSQL** — Database
* **Amazon S3** — Media storage
* **Spring Cloud AWS** — AWS integration
* **Lombok** — Boilerplate reduction
* **Gradle** — Build and dependency management
* **JUnit** — Testing

## Architecture

```text
Controller → Service → Repository → PostgreSQL
                    ↓
                Amazon S3
```

Media uploads use **presigned S3 URLs**, allowing clients to upload images and videos directly to S3 without sending the file through the Spring Boot server.

## Running Locally

### Requirements

* Java 17+
* PostgreSQL
* AWS account and S3 bucket

Clone the repository and configure your PostgreSQL and AWS credentials.

Run the application:

```bash
./gradlew bootRun
```

On Windows:

```bash
gradlew.bat bootRun
```

The application runs on:

```text
http://localhost:8080
```
