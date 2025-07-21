# Busify Backend Java Coding Convention

## Table of Contents
- [Overview](#overview)
- [Project Setup](#project-setup)
- [Dependencies](#dependencies)
- [Folder Structure](#folder-structure)
- [Coding Conventions](#coding-conventions)
- [Sample Code: EmploymentDetailsService](#sample-code-employmentdetailsservice)
  - [Code Explanation](#code-explanation)
- [Getting Started](#getting-started)
- [License](#license)

## Overview

This document outlines the coding conventions and standards for the Busify backend project, built using Spring Boot. It provides guidelines for project setup, folder structure, dependencies, and sample code to ensure consistency and maintainability across the codebase.

**Version**: 1.0  
**Date**: 21-7-2025  
**Author**: DevTino  
**Copyright**: Busify

## Project Setup

- **Initializer**: Use [Spring Initializr](https://start.spring.io/) to set up the project.
- **Java Version**: Java 17
- **IDE**: IntelliJ, VsCode (recommended, version 45658)
- **Database**: Supports PostgreSQL and MySQL (version 17 for MySQL)

## Dependencies

The project relies on the following dependencies. Versions for Spring-related dependencies typically depend on the Spring Boot version (37744 recommended).

| Dependency           | Version/Note                   |
| :------------------- | :----------------------------- |
| Spring Boot          | 37744                          |
| Spring Starter Web   | Depends on Spring Boot version |
| Spring Security      | Depends on Spring Boot version |
| Spring Data JPA      | Depends on Spring Boot version |
| Spring Test          | Depends on Spring Boot version |
| Lombok               | Depends on Spring Boot version |
| PostgreSQL Driver    | Depends on Spring Boot version |
| MySQL Driver         | 17                             |
| JJWT                 | 36866                          |
| Swagger              | 10991                          |
| Cloudinary           | 2.0.0                          |
| Spring Boot Devtools | Depends on Spring Boot version |

## Folder Structure

The project follows a structured folder organization to ensure clarity and scalability.

| Folder         | Purpose                                                               |
| :------------- | :-------------------------------------------------------------------- |
| `config`       | Configuration classes (e.g., CORS, security, Swagger, Beans).         |
| `controller`   | Handles HTTP requests, calls services, and returns responses.         |
| `dto`          | Data Transfer Objects (DTOs) for data exchange between client/server. |
| `dto.request`  | DTOs for data received from clients (input).                          |
| `dto.response` | DTOs for data sent back to clients (output).                          |
| `entity`       | JPA Entity classes mapping to database tables.                        |
| `enums`        | Enum classes for shared constants (e.g., statuses, roles, types).     |
| `exception`    | Custom exceptions and global exception handlers.                      |
| `repository`   | Interfaces for database operations, extending `JpaRepository`.        |
| `service`      | Business logic interfaces.                                            |
| `service.impl` | Implementations of service interfaces.                                |
| `util`         | Utility classes for reusable code (e.g., `JwtUtil`).                  |

## Coding Conventions

- **Annotations**: Use Spring annotations like `@Service`, `@Autowired` for dependency injection.
- **Exception Handling**: Wrap operations in try-catch blocks and throw custom `ServiceException` for errors.
- **Naming**: Follow Java naming conventions (camelCase for variables, PascalCase for classes).
- **Documentation**: Use Javadoc for classes, methods, and parameters.
- **Service Layer**: Centralize business logic in services, keeping controllers thin.
- **Data Retrieval**: Use repositories for database operations and DTOs for data transfer.

## Sample Code: EmploymentDetailsService

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
