package com.busify.project.review.dto;

import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ReviewResponseDTO {
    private String username;
    private Integer rating;
    private String comment;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime createdAt;

    public ReviewResponseDTO(String username, Integer rating, String comment, LocalDateTime createdAt) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}