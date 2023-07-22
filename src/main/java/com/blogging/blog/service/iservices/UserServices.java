package com.blogging.blog.service.iservices;

import com.blogging.blog.dto.request.LoginDTO;
import com.blogging.blog.dto.request.UserRequestDTO;
import com.blogging.blog.dto.request.UserUpdateDTO;
import com.blogging.blog.dto.response.UserResponseDTO;
import com.blogging.blog.utility.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserServices {
    GenericResponse registerUser(UserRequestDTO requestDTO);
    GenericResponse login(LoginDTO loginDTO, HttpServletRequest request);
    GenericResponse update(UserUpdateDTO request);

}
