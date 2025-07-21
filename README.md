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
    - [5. Truy cập API](#5-truy-cập-api)
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

- **Annotations**: Sử dụng các annotation của Spring như `@Service`, `@Autowired` cho dependency injection.
- **Exception Handling**: Bọc các thao tác trong try-catch và ném custom `ServiceException` khi có lỗi.
- **Naming**: Tuân thủ quy tắc đặt tên Java (camelCase cho biến, PascalCase cho class).
- **Documentation**: Viết Javadoc cho class, method, và parameter.
- **Service Layer**: Đưa toàn bộ business logic vào service, controller chỉ xử lý request/response.
- **Data Retrieval**: Sử dụng repository cho thao tác DB, dùng DTO để truyền dữ liệu.

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

- **Service Layer**: Business logic nằm trong service, controller chỉ gọi service.
- **Exception Handling**: Sử dụng custom exception để báo lỗi rõ ràng.
- **Javadoc**: Tất cả class và method đều có Javadoc.
- **Repository Usage**: Truy cập DB thông qua repository.
- **DTOs**: Chỉ dùng DTO để truyền dữ liệu, không dùng entity trực tiếp.

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/devtino2605/Busify_Project.git
cd Busify_Project
```

### 2. Configure your database

- Tạo database MySQL hoặc PostgreSQL, cập nhật thông tin kết nối trong `src/main/resources/application.properties`.

### 3. Install dependencies

```bash
./mvnw install
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

### 5. Truy cập API

- Mặc định chạy tại: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🔗 Related Projects

Busify là hệ sinh thái gồm nhiều thành phần:

- [busify-admin](https://github.com/ThomasHandlag/busify-admin.git): Admin dashboard cho quản trị hệ thống
- [busify-next](https://github.com/ThomasHandlag/busify-next.git): Web app chính cho người dùng cuối
- [busify-provider](https://github.com/ThomasHandlag/busify-provider.git): Backend API và dịch vụ

## 🛠️ Support

Nếu có vấn đề hoặc câu hỏi:

- Kiểm tra [GitHub Issues](https://github.com/devtino2605/Busify_Project/issues)
- Tạo issue mới với đầy đủ thông tin
- Hoặc liên hệ trực tiếp với team Busify

---

## 🤝 Contributing

Chúng tôi hoan nghênh mọi đóng góp! Vui lòng tạo pull request hoặc issue nếu bạn muốn đóng góp code, tài liệu hoặc báo lỗi.

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
