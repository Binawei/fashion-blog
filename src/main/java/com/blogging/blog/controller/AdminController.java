package com.blogging.blog.controller;

import com.blogging.blog.dto.request.AdminRequestDTO;
import com.blogging.blog.dto.request.AdminUpdateDTO;
import com.blogging.blog.dto.request.LoginDTO;
import com.blogging.blog.dto.response.AdminResponseDTO;
import com.blogging.blog.exception.ResourcesNotFoundException;
import com.blogging.blog.service.iservices.AdminServices;
import com.blogging.blog.utility.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/vi/admin")
public class AdminController {
    private final AdminServices adminServices;
    private final HttpSession session;

    public AdminController(AdminServices adminServices, HttpSession session) {
        this.adminServices = adminServices;
        this.session = session;
    }
    @PostMapping(path = "/register")
    public ResponseEntity<?>registerAdmin(@RequestBody @Valid AdminRequestDTO requestDTO)
    {
        var response = adminServices.registerAdmin(requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO requestDTO, HttpServletRequest request) {
        GenericResponse authenticatedAdmin = adminServices.loginAdmin(requestDTO, request);
        return ResponseEntity.ok(authenticatedAdmin);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUserInformation(@RequestBody @Valid AdminUpdateDTO request) {
        AdminResponseDTO updatedUser = adminServices.updateAdmin(request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/logout")
    public  ResponseEntity<?> logout(HttpSession session)
    {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

}
