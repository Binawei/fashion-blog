package com.blogging.blog.utility;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.http.HttpStatus;
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
    private String message;
    private String status;
    private Object data;
    @JsonIgnore
    private HttpStatus httpStatus;

    public GenericResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public GenericResponse(String message, String status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public GenericResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public GenericResponse(String message, String status, HttpStatus httpStatus) {
        this.message = message;
        this.status = status;
        this.httpStatus = httpStatus;
    }
}
