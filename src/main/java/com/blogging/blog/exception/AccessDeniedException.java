package com.blogging.blog.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends RuntimeException{
    private String message;
    private HttpStatus status;


    public AccessDeniedException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public AccessDeniedException(String message) {
        this.message = message;
    }
}
