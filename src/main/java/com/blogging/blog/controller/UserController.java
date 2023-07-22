package com.blogging.blog.controller;

import com.blogging.blog.dto.request.LoginDTO;
import com.blogging.blog.dto.request.UserRequestDTO;
import com.blogging.blog.dto.request.UserUpdateDTO;
import com.blogging.blog.dto.response.UserResponseDTO;
import com.blogging.blog.exception.ResourcesNotFoundException;
import com.blogging.blog.service.iservices.UserServices;
import com.blogging.blog.utility.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vi/users")
public class UserController {
    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody @Valid UserRequestDTO requestDTO)
    {
        var response = userServices.registerUser(requestDTO);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<GenericResponse> login(@RequestBody LoginDTO loginRequest, HttpServletRequest httpServletRequest){
        GenericResponse genericResponse = userServices.login(loginRequest, httpServletRequest);
        return new ResponseEntity<>(genericResponse, genericResponse.getHttpStatus());
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUserInformation(@RequestBody @Valid UserUpdateDTO request) {
        GenericResponse updatedUser = userServices.update(request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/logout")
    public  ResponseEntity<?> logout(HttpSession session)
    {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

}
