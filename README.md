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
    - [📜 API Document](#-api-document)
    - [📋 Project Tasks](#-project-tasks)
    - [🔗 Related Projects](#-related-projects)
    - [🛠️ Support](#️-support)
    - [🤝 Contributing](#-contributing)
    - [📬 Contact](#-contact)
    - [📝 License](#-license)
    - [🌐 Frontend Links](#-frontend-links)
    - [📚 Learn More](#-learn-more)

## Overview

This document outlines the coding conventions and standards for the Busify backend project, built using Spring Boot. It
provides guidelines for project setup, folder structure, dependencies, and sample code to ensure consistency and
maintainability across the codebase.

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
|:---------------------|:-------------------------------|
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
|----------------|------------------------------------------------------------------|
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

- Create a MySQL or PostgreSQL database, then update the connection information in
  `src/main/resources/application.properties`.

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

## 📜 API Document

### 1. Authentication

| Method | Endpoint                        | Description                | Status |
|--------|---------------------------------|----------------------------|--------|
| POST   | `/api/auth/login`               | Login                      | Done   |
| POST   | `/api/auth/refresh-token`       | Refresh access token       | Done   |
| GET    | `/api/auth/login/google`        | Login with google account  | Done   |
| POST   | `/api/auth/logout`              | Logout                     | Done   |
| POST   | `/api/auth/register`            | Sign up account with email | Done   |
| GET    | `/api/auth/verify-email`        | Verify new email registed  | Done   |
| POST   | `/api/auth/resend-verification` | Resend a new verification  | Done   |

### 2. Booking

| Method | Endpoint                             | Description                               | Status |
|--------|--------------------------------------|-------------------------------------------|--------|
| GET    | `/api/bookings`                      | Retrieve history of booking               | Done   |
| POST   | `/api/bookings`                      | Book tickets                              | Done   |
| GET    | `/api/bookings/{bookingCode}`        | Retrieve booking detail                   | Done   |
| POST   | `/api/bookings/{bookingCode}/cancel` | Request cancellation for a booking/ticket | Todo   |
| GET    | `/api/bookings/cancellation-policy`  | Retrieve cancellation policy & conditions | Todo   |

### 3. Buses

| Method | Endpoint                  | Description                     | Status |
|--------|---------------------------|---------------------------------|--------|
| GET    | `/api/bus/layout/{busId}` | Retrieve the seat layout of bus | Done   |
| GET    | `/api/bus`                | Retrieve all buses              | Todo   |
| GET    | `/api/bus/search`         | Search bus                      | Todo   |
| PATCH  | `/api/bus/{id}`           | Update bus                      | Todo   |
| POST   | `/api/bus`                | Add bus                         | Todo   |
| DELETE | `/api/bus{id}`            | Delete bus                      | Todo   |

### 4. Bus operator

| Method | Endpoint                    | Description                        | Status |
|--------|-----------------------------|------------------------------------|--------|
| GET    | `/api/bus-operators/rating` | Retrieve highly rated bus operator | Done   |
| GET    | `/api/bus-operators/{id}`   | Retrieve bus operator detail       | Done   |
| GET    | `/api/bus-operators`        | Retrieve all bus operators         | Done   |
| POST   | `/api/bus-operators`        | Add bus operator                   | Todo   |
| PATCH  | `/api/bus-operators{id}`    | Update bus operators               | Todo   |
| GET    | `/api/bus-operators/search` | Search bus operators               | Todo   |
| DELETE | `/api/bus-operators{id}`    | Delete bus operators               | Todo   |

### 5. Complaint

| Method | Endpoint                                       | Description                               | Status |
|--------|------------------------------------------------|-------------------------------------------|--------|
| GET    | `/api/complaints/{id}`                         | Retrieve complaint detail                 | Done   |
| GET    | `/api/complaints/bookings/{bookingId}`         | Retrieve all complaints of a booking      | Done   |
| GET    | `/api/complaints/customer/{customerId}`        | Retrieve all complaints of a customer     | Done   |
| GET    | `/api/complaints/trip/{tripId}`                | Retrieve all complaints of a trip         | Done   |
| POST   | `/api/complaints/booking/{bookingId}`          | Add new complaint to a booking            | Done   |
| DELETE | `/api/complaints/{id}`                         | Delete complaint                          | Done   |
| PATCH  | `/api/complaints/{id}`                         | Update complaint                          | Done   |
| GET    | `/api/complaints/bus-operator/{busOperatorId}` | Retrieve all complaints of a bus operator | Done   |
| GET    | `/api/complaints`                              | Retrieve all complaints                   | Todo   |

### 6. Payment

| Method | Endpoint                           | Description                    | Status |
|--------|------------------------------------|--------------------------------|--------|
| POST   | `/api/payments/create`             | Create new payment             | Done   |
| GET    | `/api/payments/status/{paymentId}` | Retrieve status of payment     | Done   |
| GET    | `/api/payments/success`            | Successful payment with paypal | Done   |
| GET    | `/api/payments/cancel`             | Cancel payment                 | Done   |
| GET    | `/api/payments/debug`              | Debug payment                  | Done   |
| GET    | `/api/payments/vnpay/callback`     | Successful payment with VNPay  | Done   |
| GET    | `/api/payments/{id}`               | Retrieve payment detail        | Done   |

### 7. Review

| Method | Endpoint                                    | Description                         | Status |
|--------|---------------------------------------------|-------------------------------------|--------|
| GET    | `/api/reviews/{id}`                         | Retrieve review detail              | Done   |
| GET    | `/api/reviews/trip/{tripId}`                | Retrieve all reviews by trip        | Done   |
| POST   | `/api/reviews/trip`                         | Add review to trip                  | Done   |
| GET    | `/api/reviews/customer/{customerId}`        | Retrieve all reviews by customer    | Done   |
| DELETE | `/api/reviews/{id}`                         | Delete review                       | Done   |
| PATCH  | `/api/reviews/{id}`                         | Update review                       | Done   |
| GET    | `/api/reviews/bus-operator/{busOperatorId}` | Retrieve all review by bus operator | Done   |
| GET    | `/api/reviews`                              | Retrieve all customer reviews       | Todo   |

### 8. Route

| Method | Endpoint                     | Description                       | Status |
|--------|------------------------------|-----------------------------------|--------|
| GET    | `/api/routes/popular-routes` | Retrieve a list of popular routes | Done   |
| GET    | `/api/routes`                | Retrieve a list of all routes     | Done   |
| POST   | `/api/routes`                | Add route                         | Todo   |
| PATCH  | `/api/routes{id}`            | Update route                      | Todo   |
| GET    | `/api/routes/search`         | Search route                      | Todo   |
| DELETE | `/api/routes{id}`            | Delete route                      | Todo   |

### 9. Seat Layout

| Method | Endpoint                | Description               | Status |
|--------|-------------------------|---------------------------|--------|
| GET    | `/api/seat-layout`      | Retrieve all seat layouts | Done   |
| POST   | `/api/seat-layout`      | Add seat layout           | Todo   |
| GET    | `/api/seat-layout`      | Search seat layout        | Todo   |
| PATCH  | `/api/seat-layout/{id}` | Update seat layout        | Todo   |
| DELETE | `/api/seat-layout/{id}` | Delete seat layout        | Todo   |

### 10. Ticket

| Method | Endpoint                               | Description                              | Status |
|--------|----------------------------------------|------------------------------------------|--------|
| POST   | `/api/tickets`                         | Generate tickets from a booking (by ID)  | Done   |
| GET    | `/api/tickets/customer/{customerId}`   | Retrieve tickets purchased by a customer | Todo   |
| GET    | `/api/tickets/{ticketId}`              | Retrieve ticket details                  | Todo   |
| PATCH  | `/api/tickets/{ticketId}`              | Update ticket information                | Todo   |
| POST   | `/api/tickets/{ticketId}/resend-email` | Resend ticket to customer's email        | Todo   |

### 11. Trip

| Method | Endpoint                    | Description                                | Status |
|--------|-----------------------------|--------------------------------------------|--------|
| GET    | `/api/trips`                | Retrieve all trips                         | Done   |
| GET    | `/api/trips/upcoming-trips` | Retrieve upcoming trips                    | Done   |
| POST   | `/api/trips/filter`         | Filter trips based on provided criteria    | Done   |
| GET    | `/api/trips/{id}`           | Retrieve trip details by ID                | Done   |
| GET    | `/api/trips/similar`        | Retrieve trips similar to a given route ID | Done   |
| GET    | `/api/trips/{tripId}/stops` | Retrieve all stops for a specific trip     | Done   |

### 12. Trip Seat

| Method | Endpoint                   | Description                           | Status |
|--------|----------------------------|---------------------------------------|--------|
| GET    | `/api/trip-seats/{tripId}` | Retrieve seat availability for a trip | Done   |

### 13. User

| Method | Endpoint            | Description                                              | Status |
|--------|---------------------|----------------------------------------------------------|--------|
| GET    | `/api/users`        | Retrieve all users                                       | Done   |
| GET    | `/api/users/{id}`   | Retrieve user details by ID                              | Done   |
| PATCH  | `/api/users/{id}`   | Update user profile by ID                                | Done   |
| GET    | `/api/users/email`  | Retrieve user details of the authenticated user by email | Done   |
| GET    | `/api/customers/`   | Retrieve all customer                                    | Todo   |
| GET    | `/api/users/`       | Retrieve all users                                       | Todo   |
| GET    | `/api/users/search` | Search user                                              | Todo   |
| DELETE | `/api/users/{id}`   | Delete user                                              | Todo   |

---

## 📋 Project Tasks

Below is the list of completed tasks for the Busify Backend project:

| #  | Title                                                             | Assignees                       | Status      |
|----|-------------------------------------------------------------------|---------------------------------|-------------|
| 1  | Build UI and API user management with Admin                       | Hoài                            | Done        |
| 2  | Build UI View Trip Details                                        | Hoài, Toàn                      | Done        |
| 3  | Build API similar trip and trip details                           | Quốc                            | Done        |
| 4  | Build API get seats trip, state and get trip reviews              | Dương                           | Done        |
| 5  | Build API trip review and complaints                              | Thượng                          | Done        |
| 6  | Review Database                                                   | Hoài, Dương, Thượng, Quốc, Toàn | Done        |
| 7  | Create User Story of Staff                                        | Dương                           | Done        |
| 8  | Create User Story of Customer                                     | Hoài                            | Done        |
| 9  | Create User Story of Coach Operator                               | Quốc                            | Done        |
| 10 | Create User Story of Admin                                        | Thượng                          | Done        |
| 11 | Conduct a user survey and analyze actual business ops             | Hoài, Dương, Thượng, Quốc, Toàn | Done        |
| 12 | Anilyze and build Usecase                                         | Hoài, Dương, Thượng, Quốc, Toàn | Done        |
| 13 | Build ERD diagram                                                 | Hoài, Dương, Thượng, Quốc, Toàn | Done        |
| 14 | Build convention for backend java spring boot and project init    | Hoài                            | Done        |
| 15 | Build convention for backend java spring boot and project init    | Thượng                          | Done        |
| 16 | Create User Story of Customer Service                             | Toàn                            | Done        |
| 17 | Analyze and build API for Home Page and Trip Search Page          | Toàn                            | Done        |
| 18 | Build Entity in spring boot                                       | Hoài, Toàn                      | Done        |
| 19 | Create Entity Users to Trip                                       | Toàn                            | Done        |
| 20 | Create Entity Booking to Audit_Log                                | Hoài                            | Done        |
| 21 | Build UI Home Page and Trip Search Page                           | Thượng                          | Done        |
| 22 | Build API popular trips                                           | Hoài                            | Done        |
| 23 | Build API trip search                                             | Toàn                            | Done        |
| 24 | Build API popular routes                                          | Dương                           | Done        |
| 25 | Build API hot bus operators                                       | Quốc                            | Done        |
| 26 | Build UI Summary & Payment Screen, Booking History Screen         | Dương, Quốc                     | Done        |
| 27 | Build Summary & Payment Screen                                    | Dương                           | Done        |
| 28 | Build History Booking                                             | Quốc                            | Done        |
| 29 | Build Booking History                                             | Hoài, Thượng, Toàn              | Done        |
| 30 | Build POST /api/bookings                                          | Thượng                          | Done        |
| 31 | POST /api/payments (HOAI)                                         | Hoài                            | Done        |
| 32 | GET /api/bookings/my-bookings                                     | Toàn                            | Done        |
| 33 | GET /api/bookings/{booking_code}                                  | Toàn                            | Done        |
| 34 | Build API and Profile Page                                        | Thượng                          | Done        |
| 35 | Edit and optimize the current interface                           | Toàn, Quốc                      | Done        |
| 36 | Build login and register                                          | Hoài                            | Done        |
| 37 | Build UI and API for Customers Service                            | Quốc                            | In Progress |
| 38 | Build API and UI Bus Operator management                          | Hoài                            | In Progress |
| 39 | Update the access roles in both the UI and backend                | Hoài                            | Done        |
| 40 | Build UI and API for parts of Bus Operator - Trip Management      | Toàn                            | In Progress |
| 41 | Build UI and API for parts of Bus Operator - Buses Management     | Toàn                            | In Progress |
| 41 | Build UI and API for parts of Bus Operator - Routes Management    | Toàn                            | In Progress |
| 41 | Build UI and API for parts of Bus Operator - Employees Management | Toàn                            | In Progress |
| 42 | Quoc/customer service                                             | Quốc                            | Done        |
| 43 | Build UI for customer service                                     | Quốc, Hoài                      | Done        |
| 44 | Linking the Frontend to the Backend                               | Quốc                            | Done        |
| 45 | Write test script and unit test                                   | Quốc                            | Done        |
| 46 | Build chat and work assign                                        | Quốc                            | In Progress |
| 47 | Permission                                                        | Hoài, Dương, Thượng, Quốc, Toàn | In Progress |
| 47 | Audit logs                                                        | Hoài, Dương, Thượng, Quốc, Toàn | In Progress |

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

We welcome all contributions! Please create a pull request or issue if you want to contribute code, documentation, or
report bugs.

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



