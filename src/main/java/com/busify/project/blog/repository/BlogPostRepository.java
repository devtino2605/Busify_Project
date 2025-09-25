package com.busify.project.blog.repository;

import com.busify.project.blog.entity.BlogPost;
import com.busify.project.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    
    Optional<BlogPost> findBySlugAndPublishedTrue(String slug);
    
    Page<BlogPost> findByPublishedTrueOrderByPublishedAtDesc(Pageable pageable);
    
    Page<BlogPost> findByAuthorAndPublishedTrueOrderByPublishedAtDesc(User author, Pageable pageable);
    
    Page<BlogPost> findByFeaturedTrueAndPublishedTrueOrderByPublishedAtDesc(Pageable pageable);
    
    Page<BlogPost> findByAuthor(User author, Pageable pageable);
    
    @Query("SELECT bp FROM BlogPost bp WHERE bp.published = true AND " +
           "(:tag IS NULL OR :tag MEMBER OF bp.tags) " +
           "ORDER BY bp.publishedAt DESC")
    Page<BlogPost> findPublishedPostsByTag(@Param("tag") String tag, Pageable pageable);
    
    @Query("SELECT bp FROM BlogPost bp WHERE bp.published = true AND " +
           "(LOWER(bp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(bp.excerpt) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY bp.publishedAt DESC")
    Page<BlogPost> searchPublishedPosts(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT DISTINCT tag FROM BlogPost bp JOIN bp.tags tag WHERE bp.published = true")
    List<String> findAllUsedTags();
    
    boolean existsBySlug(String slug);

    // Simplified unified filter method
    @Query("SELECT bp FROM BlogPost bp WHERE " +
           "(:published IS NULL OR bp.published = :published) AND " +
           "(:featured IS NULL OR bp.featured = :featured) AND " +
           "(:authorId IS NULL OR bp.author.id = :authorId) AND " +
           "(:tag IS NULL OR :tag MEMBER OF bp.tags) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "  LOWER(bp.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "  LOWER(bp.content) LIKE LOWER(CONCAT('%', :search, '%')))" +
            " ORDER BY bp.publishedAt DESC")
    Page<BlogPost> findPostsWithFilter(
            @Param("published") Boolean published,
            @Param("featured") Boolean featured,
            @Param("authorId") Long authorId,
            @Param("tag") String tag,
            @Param("search") String search,
            Pageable pageable);
    
}