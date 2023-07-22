package com.blogging.blog.dto.response;

import com.blogging.blog.enums.DesignCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostResponseDTO {
    private Long postId;
    private String content;
    private DesignCategory category;
}
