package com.blogging.blog.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequestDTO {
    private String name;
    private String email;
    private String password;
}
