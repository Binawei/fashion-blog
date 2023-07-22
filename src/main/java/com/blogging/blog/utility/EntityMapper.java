package com.blogging.blog.utility;

import com.blogging.blog.domain.Admin;
import com.blogging.blog.domain.BlogUser;
import com.blogging.blog.dto.request.AdminRequestDTO;
import com.blogging.blog.dto.request.UserRequestDTO;
import com.blogging.blog.dto.response.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class EntityMapper {
    public BlogUser dtoToUserMapper(UserRequestDTO userRequest){
        return BlogUser.builder()
                .email(userRequest.getEmail()).name(userRequest.getName())
                .role(userRequest.getRole()).password(userRequest.getPassword()).build();
    }
    public UserResponseDTO userToDtoMapper(BlogUser savedUser){
        return UserResponseDTO.builder()
                .role(savedUser.getRole()).email(savedUser.getEmail()).name(savedUser.getName()).build();
    }
    public Admin dtoToAdminMapper(AdminRequestDTO adminRequestDTO){
        return Admin.builder().name(adminRequestDTO.getName()).email(adminRequestDTO.getEmail())
                .password(adminRequestDTO.getPassword()).build();
    }
}
