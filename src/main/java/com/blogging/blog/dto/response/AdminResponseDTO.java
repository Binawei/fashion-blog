package com.blogging.blog.dto.response;

import lombok.*;

@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AdminResponseDTO {
    private Long AdminId;
    private String name;
    private String email;
}
