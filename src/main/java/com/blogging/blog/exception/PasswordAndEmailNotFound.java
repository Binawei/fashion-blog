package com.blogging.blog.exception;

import org.springframework.http.HttpStatus;

public class PasswordAndEmailNotFound extends RuntimeException{
    private String message;
    private HttpStatus status;

    public PasswordAndEmailNotFound(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public PasswordAndEmailNotFound(String message) {
        this.message = message;
    }
}
