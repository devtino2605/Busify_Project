package com.busify.project.complaint.dto;

import com.busify.project.complaint.enums.ComplaintStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintAddCurrentUserDTO {

    @NotNull(message = "Title cannot be null")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Booking Code cannot be null")
    private String bookingCode;

    @NotNull(message = "Status cannot be null")
    private ComplaintStatus status;
}
