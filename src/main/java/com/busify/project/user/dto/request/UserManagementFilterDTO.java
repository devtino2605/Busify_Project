package com.busify.project.user.dto.request;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementFilterDTO {
    // Search
    private String search; // Search by name, email, phone

    // Filters
    private UserStatus status;
    private AuthProvider authProvider;
    private String roleName;
    private Boolean emailVerified;

    // Date range
    private String createdFromDate; // Format: YYYY-MM-DD
    private String createdToDate; // Format: YYYY-MM-DD

    // Pagination
    private int page = 0; // Default page 0
    private int size = 10; // Default size 10

    // Sorting
    private String sortBy = "createdAt"; // Default sort by createdAt
    private String sortDirection = "desc"; // Default desc (newest first)
}
