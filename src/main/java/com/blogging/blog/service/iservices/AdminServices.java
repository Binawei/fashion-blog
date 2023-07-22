package com.blogging.blog.service.iservices;

import com.blogging.blog.dto.request.*;
import com.blogging.blog.dto.response.AdminResponseDTO;
import com.blogging.blog.utility.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface AdminServices {
    GenericResponse registerAdmin(AdminRequestDTO requestDTO);
    GenericResponse loginAdmin(LoginDTO loginDTO, HttpServletRequest request);
    AdminResponseDTO updateAdmin(AdminUpdateDTO request);

    boolean isAdmin(Long adminId);
}
