package com.busify.project.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBlogPostDto {
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @Size(max = 1000, message = "Excerpt must be less than 1000 characters")
    private String excerpt;

    @NotBlank(message = "Content is required")
    private String content;

    private MultipartFile image;

    private String tags;

    private Boolean featured = false;

    private Boolean published = false;
}