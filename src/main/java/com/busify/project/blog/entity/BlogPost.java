package com.busify.project.blog.entity;

import com.busify.project.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "blog_posts", indexes = {
        @Index(name = "idx_blog_posts_slug", columnList = "slug", unique = true),
        @Index(name = "idx_blog_posts_featured", columnList = "featured"),
        @Index(name = "idx_blog_posts_created_at", columnList = "created_at")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 250)
    private String slug;

    @Column(name = "excerpt", columnDefinition = "TEXT")
    private String excerpt;

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ElementCollection
    @CollectionTable(name = "blog_post_tags", joinColumns = @JoinColumn(name = "blog_post_id"))
    @Column(name = "tag")
    private Set<String> tags;

    @Column(name = "reading_time")
    private Integer readingTime;

    @Column(name = "featured")
    @Builder.Default
    private Boolean featured = false;

    @Column(name = "published")
    @Builder.Default
    private Boolean published = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @PrePersist
    public void prePersist() {
        if (featured == null) {
            featured = false;
        }
        if (published == null) {
            published = false;
        }
        if (viewCount == null) {
            viewCount = 0L;
        }
        if (published && publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
        if (readingTime == null && content != null) {
            // Estimate reading time: average 200 words per minute
            int wordCount = content.split("\\s+").length;
            readingTime = Math.max(1, wordCount / 200);
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (published && publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
        if (readingTime == null && content != null) {
            int wordCount = content.split("\\s+").length;
            readingTime = Math.max(1, wordCount / 200);
        }
    }
}