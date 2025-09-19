package com.busify.project.blog.service;

import com.busify.project.blog.dto.BlogPostDto;
import com.busify.project.blog.dto.BlogPostSummaryDto;
import com.busify.project.blog.dto.CreateBlogPostDto;
import com.busify.project.blog.dto.request.BlogPostFilterDTO;
import com.busify.project.blog.dto.response.BlogPostPageDTO;
import com.busify.project.blog.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogPostService {
    
    BlogPostPageDTO getPostsWithFilter(BlogPostFilterDTO filterDTO);
    
    Page<BlogPostSummaryDto> getPublishedPosts(Pageable pageable);
    
    Page<BlogPostSummaryDto> getFeaturedPosts(Pageable pageable);
    
    Page<BlogPostSummaryDto> getPostsByAuthor( Pageable pageable, boolean publishedOnly);
    
    Page<BlogPostSummaryDto> getPostsByTag(String tag, Pageable pageable);
    
    Page<BlogPostSummaryDto> searchPosts(String keyword, Pageable pageable);
    
    BlogPostDto getPostBySlug(String slug, boolean incrementView);
    
    BlogPostDto getPostById(Long id);
    
    List<String> getAllUsedTags();
    
    BlogPostDto createPost(CreateBlogPostDto createDto);
    
    BlogPostDto updatePost(Long id, CreateBlogPostDto updateDto);
    
    void deletePost(Long id);
    
    BlogPost incrementViewCount(Long postId);

    void markPostAsPublished(Long postId);

    void markPostAsFeatured(Long postId);
}