package com.blogging.blog.service.iservices;

import com.blogging.blog.dto.request.CommentRequestDTO;
import com.blogging.blog.dto.response.CommentResponseDTO;
import com.blogging.blog.utility.GenericResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICommentService {
    CommentResponseDTO createComment(CommentRequestDTO requestDTO, Long postId, Long userId, boolean isAdmin);

    String updateComment(Long commentId, String commentText, Long userId);
    CommentResponseDTO getCommentById(Long commentId);

    List<CommentResponseDTO> getCommentsByPostId(Long postId);

    GenericResponse deleteCommentByUser(Long commentId);

    GenericResponse deleteCommentByAdmin(Long commentId);
}
