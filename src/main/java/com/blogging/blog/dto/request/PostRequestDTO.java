package com.blogging.blog.dto.request;

import com.blogging.blog.enums.DesignCategory;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostRequestDTO {
    private String content;
    private DesignCategory category;
}
