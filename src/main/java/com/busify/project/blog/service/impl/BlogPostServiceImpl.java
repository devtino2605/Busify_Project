package com.busify.project.blog.service.impl;

import com.busify.project.blog.dto.BlogPostDto;
import com.busify.project.blog.dto.BlogPostSummaryDto;
import com.busify.project.blog.dto.CreateBlogPostDto;
import com.busify.project.blog.dto.request.BlogPostFilterDTO;
import com.busify.project.blog.dto.response.BlogPostPageDTO;
import com.busify.project.blog.entity.BlogPost;
import com.busify.project.blog.exception.BlogPermissionDeniedException;
import com.busify.project.blog.exception.BlogPostNotFoundException;
import com.busify.project.blog.mapper.BlogMapper;
import com.busify.project.blog.repository.BlogPostRepository;
import com.busify.project.blog.service.BlogPostService;
import com.busify.project.common.service.CloudinaryService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.user.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BlogPostServiceImpl implements BlogPostService {
    
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final CloudinaryService cloudinaryService;
    
    @Override
    public Page<BlogPostSummaryDto> getPublishedPosts(Pageable pageable) {
        return blogPostRepository.findByPublishedTrueOrderByPublishedAtDesc(pageable)
                .map(BlogMapper::convertToSummaryDto);
    }
    
    @Override
    public Page<BlogPostSummaryDto> getFeaturedPosts(Pageable pageable) {
        return blogPostRepository.findByFeaturedTrueAndPublishedTrueOrderByPublishedAtDesc(pageable)
                .map(BlogMapper::convertToSummaryDto);
    }
    
    @Override
    public Page<BlogPostSummaryDto> getPostsByAuthor(Pageable pageable, boolean publishedOnly) {
        User author = userService.getUserCurrentlyLoggedIn();
        
        if (publishedOnly) {
            return blogPostRepository.findByAuthorAndPublishedTrueOrderByPublishedAtDesc(author, pageable)
                    .map(BlogMapper::convertToSummaryDto);
        } else {
            return blogPostRepository.findByAuthor(author, pageable)
                    .map(BlogMapper::convertToSummaryDto);
        }
    }
    
    @Override
    public Page<BlogPostSummaryDto> getPostsByTag(String tag, Pageable pageable) {
        return blogPostRepository.findPublishedPostsByTag(tag, pageable)
                .map(BlogMapper::convertToSummaryDto);
    }
    
    @Override
    public Page<BlogPostSummaryDto> searchPosts(String keyword, Pageable pageable) {
        return blogPostRepository.searchPublishedPosts(keyword, pageable)
                .map(BlogMapper::convertToSummaryDto);
    }

    @Override
    @Transactional
    public BlogPostDto getPostBySlug(String slug, boolean incrementView) {
        log.info("Getting post by slug: {}, incrementView: {}", slug, incrementView);
        
        BlogPost post = blogPostRepository.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new BlogPostNotFoundException(slug, true));

        log.info("Found post with ID: {}, current viewCount: {}", post.getId(), post.getViewCount());

        if (incrementView) {
            Long currentViewCount = post.getViewCount();
            if (currentViewCount == null) {
                currentViewCount = 0L;
                log.warn("ViewCount was null for post {}, setting to 0", post.getId());
            }
            
            Long newViewCount = currentViewCount + 1L;
            post.setViewCount(newViewCount);
            
            log.info("Incrementing viewCount from {} to {} for post {}", currentViewCount, newViewCount, post.getId());
            
            post = blogPostRepository.save(post);
            
            log.info("Saved post with new viewCount: {}", post.getViewCount());
        }
        
        return BlogMapper.convertToDto(post);
    }
    
    @Override
    public BlogPostDto getPostById(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException(id));
        return BlogMapper.convertToDto(post);
    }
    
    @Override
    public List<String> getAllUsedTags() {
        return blogPostRepository.findAllUsedTags();
    }
    
    @Override
    @Transactional
    public BlogPostDto createPost(CreateBlogPostDto createDto) {
        User author = userService.getUserCurrentlyLoggedIn();
        
        if (!canUserCreatePosts(author)) {
            throw new BlogPermissionDeniedException("You don't have permission to create blog posts");
        }
        
        String slug = generateUniqueSlug(createDto.getTitle());
        String imageUrl = null;
        
        // Handle image upload
        if (createDto.getImage() != null && !createDto.getImage().isEmpty()) {
            try {
                imageUrl = cloudinaryService.uploadFile(createDto.getImage(), "blog-posts");
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
            }
        }

        Set<String> tagSet = new HashSet<>();
        if (createDto.getTags() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                tagSet = mapper.readValue(createDto.getTags(), new TypeReference<Set<String>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse tags JSON", e);
            }
        }

        BlogPost post = BlogPost.builder()
                .title(createDto.getTitle())
                .slug(slug)
                .excerpt(createDto.getExcerpt())
                .content(createDto.getContent())
                .imageUrl(imageUrl)
                .author(author)
                .tags(tagSet)
                .featured(createDto.getFeatured())
                .published(createDto.getPublished())
                .build();
        
        if (createDto.getPublished()) {
            post.setPublishedAt(LocalDateTime.now());
        }
        
        BlogPost savedPost = blogPostRepository.save(post);
        return BlogMapper.convertToDto(savedPost);
    }
    
    @Override
    @Transactional
    public BlogPostDto updatePost(Long id, CreateBlogPostDto updateDto) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException(id));
        
        User user = userService.getUserCurrentlyLoggedIn();
        
        if (!canUserEditPost(user, post)) {
            throw new BlogPermissionDeniedException("You don't have permission to edit this post");
        }
        
        // Update slug if title changed
        if (!post.getTitle().equals(updateDto.getTitle())) {
            String newSlug = generateUniqueSlugForUpdate(updateDto.getTitle(), id);
            post.setSlug(newSlug);
        }
        
        // Handle image upload/update
        String imageUrl = post.getImageUrl(); // Keep existing image by default
        if (updateDto.getImage() != null && !updateDto.getImage().isEmpty()) {
            try {
                // Delete old image if exists
                if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                    try {
                        String publicId = cloudinaryService.extractPublicId(post.getImageUrl());
                        cloudinaryService.deleteFile(publicId);
                    } catch (Exception e) {
                        // Log error but don't fail the update
                        System.err.println("Failed to delete old image: " + e.getMessage());
                    }
                }
                
                // Upload new image
                imageUrl = cloudinaryService.uploadFile(updateDto.getImage(), "blog-posts");
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload new image: " + e.getMessage(), e);
            }
        }

        Set<String> tagSet = new HashSet<>();
        if (updateDto.getTags() != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                tagSet = mapper.readValue(updateDto.getTags(), new TypeReference<Set<String>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse tags JSON", e);
            }
        }
        
        post.setTitle(updateDto.getTitle());
        post.setExcerpt(updateDto.getExcerpt());
        post.setContent(updateDto.getContent());
        post.setImageUrl(imageUrl);
        post.setTags(tagSet);
        post.setFeatured(updateDto.getFeatured());
        
        // Handle publish status change
            if (!post.getPublished() && updateDto.getPublished()) {
                post.setPublishedAt(LocalDateTime.now());
            }
            post.setPublished(updateDto.getPublished());
        
        BlogPost savedPost = blogPostRepository.save(post);
        return BlogMapper.convertToDto(savedPost);
    }
    
    @Override
    @Transactional
    public void deletePost(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException(id));
        
        User user = userService.getUserCurrentlyLoggedIn();
        
        if (!canUserEditPost(user, post)) {
            throw new BlogPermissionDeniedException("You don't have permission to delete this post");
        }
        
        // Delete image from Cloudinary if exists
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            try {
                String publicId = cloudinaryService.extractPublicId(post.getImageUrl());
                cloudinaryService.deleteFile(publicId);
            } catch (Exception e) {
                // Log error but don't fail the deletion
                System.err.println("Failed to delete image from Cloudinary: " + e.getMessage());
            }
        }
        
        blogPostRepository.delete(post);
    }

    @Override
    @Transactional
    public BlogPost incrementViewCount(Long postId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new BlogPostNotFoundException(postId));
        post.setViewCount(post.getViewCount() + 1L);
        return blogPostRepository.save(post);
    }

    @Override
    @Transactional
    public void markPostAsPublished(Long postId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new BlogPostNotFoundException(postId));
        
        boolean wasPublished = post.getPublished();
        post.setPublished(!wasPublished);
        
        // Only set publishedAt when actually publishing (not when unpublishing)
        if (!wasPublished && post.getPublished()) {
            post.setPublishedAt(LocalDateTime.now());
        }
        
        blogPostRepository.save(post);
    }

    @Override
    @Transactional
    public void markPostAsFeatured(Long postId) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new BlogPostNotFoundException(postId));
        
        boolean newFeaturedStatus = !post.getFeatured();
        post.setFeatured(newFeaturedStatus);
        
        log.info("Toggling featured status for post {}: now {}", postId, newFeaturedStatus);
        blogPostRepository.save(post);
    }

    @Override
    public BlogPostPageDTO getPostsWithFilter(BlogPostFilterDTO filterDTO) {
        // Handle myPosts filter
        Long authorId = null;
        if (Boolean.TRUE.equals(filterDTO.getMyPosts())) {
            User currentUser = userService.getUserCurrentlyLoggedIn();
            authorId = currentUser.getId();
        }
        
        // Create pageable with dynamic sorting
        Sort sort = createSort(filterDTO.getSortBy(), filterDTO.getSortDirection());
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);
        
        // Execute the simplified filter query
        Page<BlogPost> blogPosts = blogPostRepository.findPostsWithFilter(
                filterDTO.getPublished(),
                filterDTO.getFeatured(),
                authorId,
                filterDTO.getTag(),
                filterDTO.getSearch(),
                pageable
        );
        
        // Convert to DTOs using BlogMapper
        Page<BlogPostSummaryDto> postDtos = blogPosts.map(BlogMapper::convertToSummaryDto);
        
        return BlogPostPageDTO.builder()
                .posts(postDtos)
                .build();
    }
    
    private Sort createSort(String sortBy, String sortDirection) {
        // Default sorting
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        if ("publishedAt".equals(sortBy)) {
            // Ưu tiên featured trước, rồi mới đến publishedAt
            return Sort.by(Sort.Order.desc("featured"))
                    .and(Sort.by(new Sort.Order(direction, "publishedAt")));
        }

        return Sort.by(new Sort.Order(direction, sortBy));
    }
    
    private boolean canUserCreatePosts(User user) {
        if (user.getRole() == null) return false;
        String roleName = user.getRole().getName().toLowerCase();
        return roleName.contains("admin") || roleName.contains("operator") || roleName.contains("bus");
    }
    
    private boolean canUserEditPost(User user, BlogPost post) {
        if (user.getRole() == null) return false;
        String roleName = user.getRole().getName().toLowerCase();
        
        // Admin can edit any post
        if (roleName.contains("admin")) {
            return true;
        }
        
        // Author can edit their own post
        return post.getAuthor().getId().equals(user.getId());
    }
    
    private String generateUniqueSlug(String title) {
        String baseSlug = createSlugFromTitle(title);
        String slug = baseSlug;
        int counter = 1;
        
        while (blogPostRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        
        return slug;
    }
    
    private String generateUniqueSlugForUpdate(String title, Long excludeId) {
        String baseSlug = createSlugFromTitle(title);
        String slug = baseSlug;
        int counter = 1;
        
        while (blogPostRepository.existsBySlug(slug)) {
            BlogPost existingPost = blogPostRepository.findBySlugAndPublishedTrue(slug).orElse(null);
            if (existingPost != null && existingPost.getId().equals(excludeId)) {
                break; // This is the same post being updated
            }
            slug = baseSlug + "-" + counter;
            counter++;
        }
        
        return slug;
    }
    
    private String createSlugFromTitle(String title) {
        // Normalize and remove diacritics
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutDiacritics = pattern.matcher(normalized).replaceAll("");
        
        // Convert to lowercase and replace spaces and special characters with hyphens
        return withoutDiacritics.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
