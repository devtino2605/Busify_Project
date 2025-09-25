package com.busify.project.blog.exception;

public class BlogPostNotFoundException extends RuntimeException {
    public BlogPostNotFoundException(String message) {
        super(message);
    }
    
    public BlogPostNotFoundException(Long id) {
        super("Blog post not found with id: " + id);
    }
    
    public BlogPostNotFoundException(String slug, boolean bySlug) {
        super("Blog post not found with slug: " + slug);
    }
}