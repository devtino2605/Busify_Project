package com.busify.project.blog.mapper;

import com.busify.project.blog.dto.AuthorDto;
import com.busify.project.blog.dto.BlogPostDto;
import com.busify.project.blog.dto.BlogPostSummaryDto;
import com.busify.project.blog.entity.BlogPost;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlogMapper {
    public static BlogPostDto convertToDto(BlogPost post) {
        return BlogPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .excerpt(post.getExcerpt())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .author(convertAuthorToDto(post.getAuthor()))
                .tags(post.getTags())
                .readingTime(post.getReadingTime())
                .featured(post.getFeatured())
                .published(post.getPublished())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .publishedAt(post.getPublishedAt())
                .viewCount(post.getViewCount())
                .build();
    }

    public static BlogPostSummaryDto convertToSummaryDto(BlogPost post) {
        return BlogPostSummaryDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .excerpt(post.getExcerpt())
                .imageUrl(post.getImageUrl())
                .author(convertAuthorToDto(post.getAuthor()))
                .tags(post.getTags())
                .readingTime(post.getReadingTime())
                .featured(post.getFeatured())
                .publishedAt(post.getPublishedAt())
                .viewCount(post.getViewCount())
                .build();
    }

    public static AuthorDto convertAuthorToDto(User user) {
        String name = user.getEmail();
        if(user instanceof Profile profile){
            name = profile.getFullName();
        }
        return AuthorDto.builder()
                .id(user.getId())
                .name(name)
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }
}
