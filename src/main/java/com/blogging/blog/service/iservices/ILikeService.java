package com.blogging.blog.service.iservices;

import org.springframework.stereotype.Service;

@Service
public interface ILikeService {
    String likePost(Long postId);

    String unlikePost(Long postId);

    String likeComment(Long commentId);

    String unlikeComment(Long commentId);
}
