package com.busify.project.blog.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostFilterDTO {
    // Search - tìm kiếm theo title, content
    private String search;
    
    // Basic filters - những filter cơ bản nhất
    private Boolean published; // null = all, true = published, false = drafts
    private Boolean featured; // null = all, true = featured only
    private String tag; // Filter by tag
    private Boolean myPosts; // true = current user's posts only
    
    // Pagination - cần thiết cho mọi danh sách
    private int page = 0;
    private int size = 10;
    
    // Sorting - đơn giản hóa
    private String sortBy = "publishedAt"; // publishedAt, createdAt, title, viewCount
    private String sortDirection = "DESC"; // ASC, DESC
}