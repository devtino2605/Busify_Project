package com.busify.project.blog.controller;

import com.busify.project.blog.dto.BlogPostDto;
import com.busify.project.blog.dto.BlogPostSummaryDto;
import com.busify.project.blog.dto.response.BlogPostPageDTO;
import com.busify.project.blog.dto.CreateBlogPostDto;
import com.busify.project.blog.dto.request.BlogPostFilterDTO;
import com.busify.project.blog.service.BlogPostService;
import com.busify.project.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Blog", description = "Blog Management API")
public class BlogController {
    
    private final BlogPostService blogPostService;
    
    @GetMapping("/posts")
    @Operation(summary = "Get all blog posts with pagination, filtering, and search",
               description = "Retrieve all published blog posts with optional filtering by tag, search keyword, and featured status. Supports pagination.")
    public ApiResponse<Page<BlogPostSummaryDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "false") boolean featured) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPostSummaryDto> posts;
        
        if (search != null && !search.trim().isEmpty()) {
            posts = blogPostService.searchPosts(search.trim(), pageable);
        } else if (tag != null && !tag.trim().isEmpty()) {
            posts = blogPostService.getPostsByTag(tag.trim(), pageable);
        } else if (featured) {
            posts = blogPostService.getFeaturedPosts(pageable);
        } else {
            posts = blogPostService.getPublishedPosts(pageable);
        }
        
        return ApiResponse.<Page<BlogPostSummaryDto>>builder()
                .code(HttpStatus.OK.value())
                .message("Blog posts retrieved successfully")
                .result(posts)
                .build();
    }
    
    @GetMapping("/posts/{slug}/slug")
    @Operation(summary = "Get a blog post by slug",
               description = "Retrieve a single published blog post by its slug. Optionally increments the view count.")
    public ApiResponse<BlogPostDto> getPostBySlug(
            @PathVariable String slug,
            @RequestParam(defaultValue = "true") boolean incrementView) {
        
        BlogPostDto post = blogPostService.getPostBySlug(slug, incrementView);
        return ApiResponse.<BlogPostDto>builder()
                .code(HttpStatus.OK.value())
                .message("Blog post retrieved successfully")
                .result(post)
                .build();
    }
    
    @GetMapping("/posts/author")
    @Operation(summary = "Get blog posts by author with pagination",
               description = "Retrieve blog posts authored by a specific user. Supports pagination and filtering for published posts only.")
    public ApiResponse<Page<BlogPostSummaryDto>> getPostsByAuthor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "true") boolean publishedOnly) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPostSummaryDto> posts = blogPostService.getPostsByAuthor( pageable, publishedOnly);
        return ApiResponse.<Page<BlogPostSummaryDto>>builder()
                .code(HttpStatus.OK.value())
                .message("Author's blog posts retrieved successfully")
                .result(posts)
                .build();
    }
    
    @GetMapping("/tags")
    @Operation(summary = "Get all used tags",
               description = "Retrieve a list of all unique tags used in published blog posts.")
    public ApiResponse<List<String>> getAllUsedTags() {
        List<String> tags = blogPostService.getAllUsedTags();
        return ApiResponse.<List<String>>builder()
                .code(HttpStatus.OK.value())
                .message("Tags retrieved successfully")
                .result(tags)
                .build();
    }
    
    @PostMapping("/posts")
    @Operation(summary = "Create a new blog post",
               description = "Create a new blog post. Requires authentication.")
    public ApiResponse<BlogPostDto> createPost(
            @Valid @ModelAttribute CreateBlogPostDto createDto
            ) {
        
        BlogPostDto createdPost = blogPostService.createPost(createDto);
        return ApiResponse.<BlogPostDto>builder()
                .code(HttpStatus.CREATED.value())
                .message("Blog post created successfully")
                .result(createdPost)
                .build();
    }
    
    @PutMapping("/posts/{id}")
    @Operation(summary = "Update an existing blog post",
               description = "Update an existing blog post by its ID. Requires authentication and ownership of the post.")
    public ApiResponse<BlogPostDto> updatePost(
            @PathVariable Long id,
            @Valid @ModelAttribute CreateBlogPostDto updateDto
            ) {
        
        BlogPostDto updatedPost = blogPostService.updatePost(id, updateDto);
        return ApiResponse.<BlogPostDto>builder()
                .code(HttpStatus.OK.value())
                .message("Blog post updated successfully")
                .result(updatedPost)
                .build();
    }
    
    @DeleteMapping("/posts/{id}")
    @Operation(summary = "Delete a blog post",
               description = "Delete a blog post by its ID. Requires authentication and ownership of the post.")
    public ApiResponse<Void> deletePost(
            @PathVariable Long id
            ) {
        blogPostService.deletePost(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("Blog post deleted successfully")
                .result(null)
                .build();
    }
    
    @GetMapping("/posts/{id}")
    @Operation(summary = "Get a blog post by ID (admin/author only)",
               description = "Retrieve a blog post by its ID. This endpoint is intended for admin or the author to view unpublished posts.")
    public ApiResponse<BlogPostDto> getPostById(
            @PathVariable Long id) {
        
        // This endpoint is for admin/author to view unpublished posts
        BlogPostDto post = blogPostService.getPostById(id);
        return ApiResponse.<BlogPostDto>builder()
                .code(HttpStatus.OK.value())
                .message("Blog post retrieved successfully")
                .result(post)
                .build();
    }
    
    @PutMapping("/posts/{id}/view")
    @Operation(summary = "Increment view count of a blog post",
               description = "Increment the view count of a blog post by its ID. This can be called when a post is viewed.")
    public ApiResponse<Void> incrementViewCount(@PathVariable Long id) {
        blogPostService.incrementViewCount(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("View count incremented successfully")
                .result(null)
                .build();
    }

    @PutMapping("/posts/{id}/published")
    @Operation(summary = "Mark a blog post as published",
                description = "Mark a blog post as published by its ID. Requires authentication and ownership of the post.")
    public ApiResponse<Void> markPostAsPublished(@PathVariable Long id) {
        blogPostService.markPostAsPublished(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Blog post marked as published successfully")
                .result(null)
                .build();
    }

    @PutMapping("/posts/{id}/featured")
    @Operation(summary = "Mark a blog post as featured",
                description = "Mark a blog post as featured by its ID. Requires authentication and ownership of the post.")
    public ApiResponse<Void> markPostAsFeatured(@PathVariable Long id) {
        blogPostService.markPostAsFeatured(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Blog post marked as featured successfully")
                .result(null)
                .build();
    }

    
    @GetMapping("/posts/filter")
    @Operation(summary = "Get blog posts with simplified filtering",
               description = "Retrieve blog posts with essential filtering options: search, published status, featured status, tags, and user's own posts.")
    public ApiResponse<BlogPostPageDTO> getPostsWithFilter(
            // Search
            @RequestParam(required = false) String search,
            
            // Essential filters only
            @RequestParam(required = false) Boolean published,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false, defaultValue = "false") Boolean myPosts,
            
            // Pagination & Sorting
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "publishedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        // Create filter DTO from simplified parameters
        BlogPostFilterDTO filterDTO = new BlogPostFilterDTO();
        filterDTO.setSearch(search);
        filterDTO.setPublished(published);
        filterDTO.setFeatured(featured);
        filterDTO.setTag(tag);
        filterDTO.setMyPosts(myPosts);
        filterDTO.setPage(page);
        filterDTO.setSize(size);
        filterDTO.setSortBy(sortBy);
        filterDTO.setSortDirection(sortDirection);
        
        BlogPostPageDTO result = blogPostService.getPostsWithFilter(filterDTO);
        return ApiResponse.<BlogPostPageDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Blog posts retrieved successfully")
                .result(result)
                .build();
    }
}