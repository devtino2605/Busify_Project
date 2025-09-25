package com.busify.project.blog.exception;

public class BlogPermissionDeniedException extends RuntimeException {
    public BlogPermissionDeniedException(String message) {
        super(message);
    }
    
    public BlogPermissionDeniedException() {
        super("You don't have permission to perform this action");
    }
}