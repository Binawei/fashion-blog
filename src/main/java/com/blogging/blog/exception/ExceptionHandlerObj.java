package com.blogging.blog.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerObj {
    @ExceptionHandler(ResourcesFoundException.class)
    public ResponseEntity<?> handleResourceFoundException(ResourcesFoundException es)
    {
        return ResponseEntity.badRequest().body(es.getLocalizedMessage());
    }

    @ExceptionHandler(PasswordAndEmailNotFound.class)
    public ResponseEntity<?>handlePasswordAndEmailNotFoundException(PasswordAndEmailNotFound es)
    {
        return ResponseEntity.badRequest().body(es.getLocalizedMessage());
    }

    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<?>handleResourcesNotFoundException(ResourcesNotFoundException e)
    {
        return ResponseEntity.badRequest().body(e.getLocalizedMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?>handleAccessDeniedException(AccessDeniedException es)
    {
        return ResponseEntity.badRequest().body(es.getLocalizedMessage());
    }
}
