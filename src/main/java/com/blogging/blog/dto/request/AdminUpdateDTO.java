package com.blogging.blog.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminUpdateDTO {
    private String name;
    private String email;
}
