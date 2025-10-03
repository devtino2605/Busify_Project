package com.busify.project.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogPostSummaryDto {
    private Long id;
    private String title;
    private String slug;
    private String excerpt;
    private String imageUrl;
    private AuthorDto author;
    private Set<String> tags;
    private Integer readingTime;
    private Boolean featured;
    private LocalDateTime publishedAt;
    private Long viewCount;
}