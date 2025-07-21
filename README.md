<p align="center">
  <img src="https://img.shields.io/github/contributors/devtino2605/Busify_Project" alt="GitHub contributors" />
  <img src="https://img.shields.io/github/issues/devtino2605/Busify_Project" alt="GitHub Issues" />
  <img src="https://img.shields.io/github/license/devtino2605/Busify_Project" alt="GitHub License" />
</p>

# 🚍 Busify Backend

<p align="center">
  <b>Backend service for the Busify ecosystem, built with Spring Boot.</b>
</p>

## 📑 Table of Contents

- [🚍 Busify Backend](#-busify-backend)
  - [📑 Table of Contents](#-table-of-contents)
  - [Overview](#overview)
  - [⚙️ Project Setup](#️-project-setup)
  - [📦 Dependencies](#-dependencies)
  - [📁 Folder Structure](#-folder-structure)
  - [📝 Coding Conventions](#-coding-conventions)
  - [💡 Sample Code: EmploymentDetailsService](#-sample-code-employmentdetailsservice)
  - [🧩 Code Explanation](#-code-explanation)
  - [🚀 Getting Started](#-getting-started)
    - [1. Clone the repository](#1-clone-the-repository)
    - [2. Configure your database](#2-configure-your-database)
    - [3. Install dependencies](#3-install-dependencies)
    - [4. Run the application](#4-run-the-application)
    - [5. Access the API](#5-access-the-api)
  - [🔗 Related Projects](#-related-projects)
  - [🛠️ Support](#️-support)
  - [🤝 Contributing](#-contributing)
  - [📬 Contact](#-contact)
  - [📝 License](#-license)
  - [🌐 Frontend Links](#-frontend-links)
  - [📚 Learn More](#-learn-more)

## Overview

This document outlines the coding conventions and standards for the Busify backend project, built using Spring Boot. It provides guidelines for project setup, folder structure, dependencies, and sample code to ensure consistency and maintainability across the codebase.

> **Version**: 1.0  
> **Date**: 21-07-2025  
> **Author**: Busify

## ⚙️ Project Setup

- **Initializer**: [Spring Initializr](https://start.spring.io/)
- **Java Version**: 17
- **IDE**: IntelliJ IDEA, VS Code (recommended)
- **Database**: PostgreSQL, MySQL (v17)

## 📦 Dependencies

| Dependency           | Version/Note                   |
| :------------------- | :----------------------------- |
| Spring Boot          | 3.5.2003                       |
| Spring Starter Web   | Depends on Spring Boot version |
| Spring Security      | Depends on Spring Boot version |
| Spring Data JPA      | Depends on Spring Boot version |
| Spring Test          | Depends on Spring Boot version |
| Lombok               | Depends on Spring Boot version |
| PostgreSQL Driver    | Depends on Spring Boot version |
| MySQL Driver         | 17                             |
| JJWT                 | 2000.12.6                      |
| Swagger              | 2.2.30                         |
| Cloudinary           | 2.0.0                          |
| Spring Boot Devtools | Depends on Spring Boot version |


## 📁 Folder Structure

| Folder         | Purpose                                                          |
| -------------- | ---------------------------------------------------------------- |
| `config`       | Configuration classes (CORS, security, Swagger, Beans, etc.)     |
| `controller`   | Handles HTTP requests, calls services, and returns responses     |
| `dto`          | Data Transfer Objects (DTOs) for data exchange                   |
| `dto/request`  | DTOs for data received from clients (input)                      |
| `dto/response` | DTOs for data sent back to clients (output)                      |
| `entity`       | JPA Entity classes mapping to database tables                    |
| `enums`        | Enum classes for shared constants (statuses, roles, types, etc.) |
| `exception`    | Custom exceptions and global exception handlers                  |
| `repository`   | Interfaces for database operations, extending `JpaRepository`    |
| `service`      | Business logic interfaces                                        |
| `service/impl` | Implementations of service interfaces                            |
| `utils`        | Utility classes for reusable code (e.g., `JwtUtil`)              |

## 📝 Coding Conventions

- **Annotations**: Use Spring annotations such as `@Service`, `@Autowired` for dependency injection.
- **Exception Handling**: Wrap operations in try-catch blocks and throw custom `ServiceException` on errors.
- **Naming**: Follow Java naming conventions (camelCase for variables, PascalCase for classes).
- **Documentation**: Write Javadoc for classes, methods, and parameters.
- **Service Layer**: Place all business logic in services, controllers should only handle request/response.
- **Data Retrieval**: Use repositories for database operations and DTOs for data transfer.

## 💡 Sample Code: EmploymentDetailsService

Below is an example of a service class demonstrating the coding conventions.

```java
package model.service;

import java.util.ArrayList;
import java.util.List;
import model.entity.EmpStatus;
import model.form.EmployeeDetailsForm;
import model.repository.EmpStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * EmploymentDetailsService
 *
 * Provides business logic for managing employment details.
 *
 * Version 1.0
 * Date: 19-06-2025
 *
 * Modification Logs:
 * DATE       AUTHOR    DESCRIPTION
 * -----------------------------------------------------------------------
 * 19-06-2025 Grok      Create
 */
@Service
public class EmploymentDetailsService {

    private final EmpStatusRepository empStatusRepository;

    /**
     * Constructor for dependency injection.
     * @param empStatusRepository Repository for EmpStatus entity
     */
    @Autowired
    public EmploymentDetailsService(EmpStatusRepository empStatusRepository) {
        this.empStatusRepository = empStatusRepository;
    }

    /**
     * Retrieves data for updating job details page.
     * @param establishmentCode The unique code for the establishment
     * @return EmployeeDetailsForm containing job details data
     * @throws ServiceException if an error occurs during data retrieval
     */
    public EmployeeDetailsForm getDataForUpdateJobDetails(String establishmentCode) throws ServiceException {
        EmployeeDetailsForm form = new EmployeeDetailsForm();
        try {
            // Populating employment status data
            List<EmpStatus> arrayEmpStatus = new ArrayList<>();
            List<EmpStatus> empStatuses = empStatusRepository.findAll();
            for (EmpStatus status : empStatuses) {
                EmpStatus empStatus = new EmpStatus();
                empStatus.setEmpStatusId(status.getEmpStatusId());
                empStatus.setEmpStatusName(status.getEmpStatusName());
                arrayEmpStatus.add(empStatus);
            }
            form.setArrayEmpStatus(arrayEmpStatus);

            // Set establishment code (mocked for illustration)
            form.setEstablishmentCode(establishmentCode);
        } catch (Exception e) {
            throw new ServiceException("Error in getDataForUpdateJobDetails method.");
        }
        return form;
    }

    /**
     * Checks if an employee exists based on establishment code.
     * @param establishmentCode The unique code for the establishment
     * @return true if employee exists, false otherwise
     * @throws ServiceException if an error occurs during the check
     */
    public boolean checkEmployeeDetailsIsExist(String establishmentCode) throws ServiceException {
        try {
            // Query repository to check existence
            boolean exists = employeeDetailsRepository.existsByEstablishmentCode(establishmentCode);
            return exists;
        } catch (Exception e) {
            throw new ServiceException("Error in checkEmployeeDetailsIsExist method.");
        }
    }
}
```

---

## 🧩 Code Explanation

- **Service Layer**: All business logic is in the service layer, controllers only call services.
- **Exception Handling**: Use custom exceptions for clear error reporting.
- **Javadoc**: Every class and method is documented with Javadoc.
- **Repository Usage**: All database access is through repositories.
- **DTOs**: Only use DTOs for data transfer, never entities directly.

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/devtino2605/Busify_Project.git
cd Busify_Project
```

### 2. Configure your database

- Create a MySQL or PostgreSQL database, then update the connection information in `src/main/resources/application.properties`.

### 3. Install dependencies

```bash
./mvnw install
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

### 5. Access the API

- Default URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🔗 Related Projects

Busify is an ecosystem with multiple components:

- [busify-admin](https://github.com/ThomasHandlag/busify-admin.git): Admin dashboard for system management
- [busify-next](https://github.com/ThomasHandlag/busify-next.git): Main web application for end users
- [busify-provider](https://github.com/ThomasHandlag/busify-provider.git): Backend API and services

## 🛠️ Support

For issues or questions:

- Check [GitHub Issues](https://github.com/devtino2605/Busify_Project/issues)
- Create a new issue with details
- Or contact the Busify team directly

---

## 🤝 Contributing

We welcome all contributions! Please create a pull request or issue if you want to contribute code, documentation, or report bugs.

---

## 📬 Contact

- **Email**: busify.team@gmail.com
- **Facebook**: [Busify Fanpage](https://facebook.com/busify)

## 📝 License

This project is licensed under the MIT License.

## 🌐 Frontend Links

- [Busify Admin FE](https://github.com/ThomasHandlag/busify-admin.git)
- [Busify Next FE](https://github.com/ThomasHandlag/busify-next.git)
- [Busify Provider FE](https://github.com/ThomasHandlag/busify-provider.git)

## 📚 Learn More

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/) - Learn about Spring Boot
- [Java Documentation](https://docs.oracle.com/en/java/javase/17/) - Learn about Java 17
- [Swagger Documentation](https://swagger.io/docs/) - Learn about API documentation
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/) - Learn about security
