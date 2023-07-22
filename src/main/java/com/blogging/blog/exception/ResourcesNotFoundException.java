package com.blogging.blog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class ResourcesNotFoundException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public ResourcesNotFoundException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public ResourcesNotFoundException(String message) {
        this.message = message;
    }
}
