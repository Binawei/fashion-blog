package com.blogging.blog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Getter
public class ResourcesFoundException extends RuntimeException{
    private String message;
    private HttpStatus status;
    private final LocalDateTime time = LocalDateTime.now();

    public ResourcesFoundException(String message) {
        this.message = message;
    }

    public ResourcesFoundException(String message,HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
