package com.blogging.blog.dto.response;

import com.blogging.blog.enums.Role;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private String name;
    private String email;
    private Role role;
}
