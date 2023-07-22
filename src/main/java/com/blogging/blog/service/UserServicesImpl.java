package com.blogging.blog.service;

import com.blogging.blog.domain.BlogUser;
import com.blogging.blog.dto.request.LoginDTO;
import com.blogging.blog.dto.request.UserRequestDTO;
import com.blogging.blog.dto.request.UserUpdateDTO;
import com.blogging.blog.dto.response.UserResponseDTO;
import com.blogging.blog.exception.PasswordAndEmailNotFound;
import com.blogging.blog.exception.ResourcesFoundException;
import com.blogging.blog.exception.ResourcesNotFoundException;
import com.blogging.blog.repository.BlogUserRepository;
import com.blogging.blog.service.iservices.UserServices;
import com.blogging.blog.utility.EntityMapper;
import com.blogging.blog.utility.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServicesImpl implements UserServices {
    private HttpSession session;
    private final BlogUserRepository userRepository;
    private final EntityMapper entityMapper;

    public UserServicesImpl(HttpSession session, BlogUserRepository userRepository, EntityMapper entityMapper) {
        this.session = session;
        this.userRepository = userRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public GenericResponse registerUser(UserRequestDTO requestDTO) {
        Optional<BlogUser>user = userRepository.findByEmail(requestDTO.getEmail());
        if (user.isPresent())
        {
            throw new ResourcesFoundException("the user already exits", HttpStatus.ALREADY_REPORTED);
        }
        BlogUser newUser = BlogUser.builder().name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword())
                .role(requestDTO.getRole()).build();

        BlogUser savedUser = userRepository.save(newUser);
        UserResponseDTO userResponseDTO = UserResponseDTO.builder().name(savedUser.getName()).role(savedUser.getRole())
                .email(savedUser.getEmail()).build();
        return GenericResponse.builder().status("00").message("Request successful").httpStatus(HttpStatus.CREATED)
                .data(userResponseDTO).build();

    }
    @Override
    public GenericResponse login(LoginDTO loginDTO, HttpServletRequest request) {
        Optional<BlogUser>optionalUser = userRepository.findByEmail(loginDTO.getEmail());
        if (optionalUser.isEmpty())
        {
            return GenericResponse.builder().message("Invalid Request")
                    .status("01").httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        UserResponseDTO userResponseDTO = entityMapper.userToDtoMapper(optionalUser.get());
        if (!loginDTO.getPassword().equals(optionalUser.get().getPassword()))
        {
            throw new PasswordAndEmailNotFound("Please check your email or password");
        }
        //Successfully logged in
        session = request.getSession();
        session.setAttribute("userid", optionalUser.get().getId());
        return GenericResponse.builder().message("Request successful").status("00").data(userResponseDTO)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public GenericResponse update(UserUpdateDTO request) {
        Long userid = (Long) session.getAttribute("userid");
        if (userid == null) {
            throw new ResourcesNotFoundException("Please log in first", HttpStatus.FORBIDDEN);
        }

        Optional<BlogUser> optionalUser = userRepository.findById(userid);
        if (optionalUser.isEmpty())
        {
            throw  new ResourcesNotFoundException("user not found", HttpStatus.NOT_FOUND);
        }

        //get the user
        BlogUser user = optionalUser.get();
        //update the fields
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setRole(request.getRole());

        //save the updated use back to the database
        BlogUser updatedUser = userRepository.save(user);
        UserResponseDTO userResponseDTO = UserResponseDTO.builder().name(updatedUser.getName()).email(user.getEmail()).role(updatedUser.getRole()).build();

        return GenericResponse.builder().message("updated successfully").httpStatus(HttpStatus.OK).status("00").data(userResponseDTO).build();
    }

}
