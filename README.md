# Job Portal

**Job Portal** is a web application that allows users to apply for jobs, upload resumes, and manage job applications. Employers can post jobs and track applications efficiently.
Admin can view all registered users and delete them.

## 🛠️ Technologies Used

- **Java 17** – Core programming language.
- **Spring Boot 3.4.3** – Backend framework.
- **MySQL** – Relational database to store job postings, applications, and users.
- **Spring Security** – Authentication & Authorization.
- **JWT (JSON Web Token)** – Secure authentication.
- **MapStruct** – Entity-DTO mapping.
- **JPA (Java Persistence API)** – Database interaction.
- **Maven** – Dependency management and build tool.
- **Postman** – API testing.

## 🚀 Getting Started

### Prerequisites

Before starting, ensure you have installed:

- [JDK 17+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/download.cgi)
- [MySQL Server](https://dev.mysql.com/downloads/installer/)
- [Postman](https://www.postman.com/)
- [Git](https://git-scm.com/)

### Setup & Configuration

#### Clone the repository
git clone https://github.com/klediandoko/job-portal.git
cd job-portal

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/job_portal
spring.datasource.username=root
spring.datasource.password=1234

# JPA Properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.highlight_sql=true

# Build & Run the Project
#Run with Maven
mvn clean install
mvn spring-boot:run
