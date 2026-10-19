# Student Management REST API

A RESTful API built with Java and Spring Boot to manage student records, with authentication, role-based authorization, input validation, and database persistence.

This project demonstrates core backend development concepts, including layered architecture, REST API design, Spring Data JPA, Spring Security, DTOs, exception handling, and automated testing.

## 🚀 Features

* **Student CRUD Operations:** Create, retrieve, update, and delete student records.
* **Search and Filtering:** Search students by name and filter by course.
* **Pagination and Sorting:** Retrieve student records in pages and sort results.
* **Input Validation:** Validate incoming request data using Jakarta Bean Validation.
* **DTOs:** Separate API request and response objects from database entities.
* **Centralized Exception Handling:** Return appropriate error responses for validation failures, duplicate email addresses, and missing student records.
* **Authentication:** Authenticate users using Spring Security and HTTP Basic authentication.
* **Role-Based Authorization:** Restrict student creation, updating, and deletion to administrators.
* **Password Hashing:** Store user passwords using BCrypt.
* **Automated Tests:** Test service-layer behavior using JUnit and Mockito.

## 🛠️ Technology Stack

| Technology              | Purpose                                    |
| ----------------------- | ------------------------------------------ |
| Java 17                 | Programming language                       |
| Spring Boot 3.4.0       | Application framework                      |
| Spring Web              | REST API development                       |
| Spring Data JPA         | Database access and persistence            |
| Spring Security         | Authentication and authorization           |
| MySQL                   | Relational database                        |
| Maven                   | Dependency management and build automation |
| Jakarta Bean Validation | Request validation                         |
| JUnit 5                 | Automated testing                          |
| Mockito                 | Mocking dependencies during unit tests     |

## 🏗️ Project Architecture

The application follows a layered architecture to separate responsibilities.

```text
src/main/java/com/len/student_app/
│
├── config/
│   └── SecurityConfig.java
│
├── controller/
│   ├── AuthController.java
│   └── StudentController.java
│
├── dto/
│   ├── RegisterRequestDTO.java
│   ├── StudentRequestDTO.java
│   └── StudentResponseDTO.java
│
├── entity/
│   ├── AppUser.java
│   └── Student.java
│
├── exception/
│   ├── DuplicateEmailException.java
│   ├── GlobalExceptionHandler.java
│   └── StudentNotFoundException.java
│
├── repository/
│   ├── AppUserRepository.java
│   └── StudentRepository.java
│
├── service/
│   ├── AuthService.java
│   ├── CustomUserDetailsService.java
│   └── StudentService.java
│
└── StudentAppApplication.java
```

### Layer Responsibilities

* **Controller:** Handles HTTP requests and responses.
* **Service:** Contains business logic and coordinates operations.
* **Repository:** Interacts with the database using Spring Data JPA.
* **Entity:** Represents database records.
* **DTO:** Defines the data accepted and returned by the API.
* **Security Configuration:** Defines authentication and access-control rules.
* **Global Exception Handler:** Centralizes handling of application exceptions.

## 🔐 Security

The application uses Spring Security with HTTP Basic authentication.

### Roles

| Role    | Permissions                                          |
| ------- | ---------------------------------------------------- |
| `USER`  | Retrieve student records                             |
| `ADMIN` | Retrieve, create, update, and delete student records |

### Security Implementation

* Public access to the user registration endpoint.
* BCrypt password hashing.
* Custom user authentication through email.
* Role-based access restrictions for student operations.
* Registration assigns the `USER` role rather than allowing users to select their own role.

Protected endpoints require valid authentication credentials. Operations restricted to administrators also require the appropriate role.

## 📡 API Endpoints

The default base URL is:

`http://localhost:8080`

### Authentication

| Method | Endpoint         | Description         | Access |
| ------ | ---------------- | ------------------- | ------ |
| POST   | `/auth/register` | Register a new user | Public |

Registration accepts the user's registration details and assigns the default `USER` role.

### Student Management

| Method | Endpoint         | Description                       | Access      |
| ------ | ---------------- | --------------------------------- | ----------- |
| POST   | `/students`      | Create a student                  | ADMIN       |
| GET    | `/students`      | Retrieve students with pagination | USER, ADMIN |
| GET    | `/students/{id}` | Retrieve a student by ID          | USER, ADMIN |
| PUT    | `/students/{id}` | Update a student                  | ADMIN       |
| DELETE | `/students/{id}` | Delete a student                  | ADMIN       |

### Search and Filtering

| Method | Endpoint                                      | Description                        | Access      |
| ------ | --------------------------------------------- | ---------------------------------- | ----------- |
| GET    | `/students/course/{course}`                   | Find students by course            | USER, ADMIN |
| GET    | `/students/search?name=rah`                   | Search students by name            | USER, ADMIN |
| GET    | `/students/search/filter?name=ra&course=Java` | Filter students by name and course | USER, ADMIN |

### Pagination and Sorting Examples

Retrieve the first page with two records:

```http
GET /students?page=0&size=2
```

Sort students by name in ascending order:

```http
GET /students?page=0&size=5&sort=name,asc
```

Page numbering starts at `0`.

## ⚙️ Getting Started

### Prerequisites

Install or configure the following:

* Java Development Kit (JDK) 17
* MySQL Server
* Git

Maven is included through the Maven Wrapper, so a separate Maven installation is not required.

### 1. Clone the Repository

```bash
git clone https://github.com/LenenCodes/student-management-api.git
```

Navigate into the project directory:

```bash
cd student-management-api
```

### 2. Create the Database

Open MySQL and execute:

```sql
CREATE DATABASE student_management;
```

The application uses Hibernate with the configured JPA schema-generation setting to manage database tables.

### 3. Configure Environment Variables

The application reads its database configuration from environment variables.

The default configuration in `application.properties` is:

```properties
spring.application.name=student-app

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/student_management}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Configure the following environment variables:

| Variable      | Purpose                                                                   |
| ------------- | ------------------------------------------------------------------------- |
| `DB_URL`      | MySQL connection URL; defaults to the local `student_management` database |
| `DB_USERNAME` | MySQL username; defaults to `root`                                        |
| `DB_PASSWORD` | MySQL password                                                            |

For Windows Command Prompt, configure the variables in the terminal before starting the application:

```bat
set DB_URL=jdbc:mysql://localhost:3306/student_management
set DB_USERNAME=root
set DB_PASSWORD=your_local_database_password
```

Replace the example password with your own local MySQL password. These commands apply to the current terminal session.

**Security note:** Never commit real database passwords or other secrets to GitHub.

### 4. Run the Application

From the project root, execute:

**Windows:**

```bat
mvnw.cmd spring-boot:run
```

The application should start at:

`http://localhost:8080`

### 5. Test the API

Use Postman or another HTTP client to test the endpoints.

A suggested testing sequence:

1. Register a user using `POST /auth/register`.
2. Authenticate using the registered email and password.
3. Retrieve student records using `GET /students`.
4. Verify that a regular user cannot create, update, or delete student records.
5. Authenticate with an administrator account and test the restricted operations.

Administrator credentials must be configured securely for your environment.

## 🧪 Running Automated Tests

Execute the test suite using the Maven Wrapper:

**Windows:**

```bat
mvnw.cmd test
```

The project includes service-layer unit tests using JUnit 5 and Mockito.

These tests help verify business logic without requiring every test to interact with the actual database.

## 📚 Concepts Demonstrated

This project provides practical experience with:

* RESTful API development
* Dependency injection
* Controller-service-repository architecture
* Spring Data JPA and database persistence
* Entity-to-DTO conversion
* Request validation
* Custom exceptions and centralized exception handling
* Pagination and sorting
* Spring Security authentication and authorization
* BCrypt password encoding
* Unit testing with JUnit and Mockito
* Environment-based database configuration
* Git and GitHub version control

## 🔮 Possible Future Improvements

* API documentation using OpenAPI and Swagger UI
* Integration tests using a dedicated test database
* Containerization with Docker
* Continuous integration with GitHub Actions
* Improved API error-response consistency

## 👨‍💻 Author

**LenenCodes**

GitHub: https://github.com/LenenCodes
