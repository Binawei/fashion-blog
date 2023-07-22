package com.blogging.blog.service.iservices;

import com.blogging.blog.dto.request.PostRequestDTO;
import com.blogging.blog.dto.response.PostResponseDTO;

public interface IPostServices {
    PostResponseDTO createPostDesign(PostRequestDTO request);
    PostResponseDTO getPostById(Long postId);
    PostResponseDTO updatePost(Long postId, PostRequestDTO request);
    void deletePost(Long postId);
}
