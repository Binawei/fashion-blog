package com.blogging.blog.controller;

import com.blogging.blog.dto.request.CommentRequestDTO;
import com.blogging.blog.dto.response.CommentResponseDTO;
import com.blogging.blog.exception.AccessDeniedException;
import com.blogging.blog.service.CommentServiceImpl;
import com.blogging.blog.utility.GenericResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/vi/comment")
public class CommentController {
    private final CommentServiceImpl commentService;

    public CommentController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }


    @PostMapping("/create/{postId}")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long postId, @RequestBody @NotBlank CommentRequestDTO requestDTO, HttpSession session) {
        // Retrieve the logged-in user ID from the session
        Long userId = (Long) session.getAttribute("userid");
        boolean isAdmin = false; // Set this based on the user's role or any other condition

        CommentResponseDTO responseDTO = commentService.createComment(requestDTO, postId, userId, isAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    @PostMapping("/create/admin/{postId}")
    public ResponseEntity<CommentResponseDTO> createAdminComment(@PathVariable Long postId, @RequestBody @NotBlank CommentRequestDTO requestDTO, HttpSession session) {
        // Retrieve the logged-in admin ID from the session
        Long adminId = (Long) session.getAttribute("adminId");
        boolean isAdmin = true; // Set the isAdmin flag to true

        CommentResponseDTO responseDTO = commentService.createComment(requestDTO, postId, adminId, isAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO requestDTO, HttpSession session) {
        // Retrieve the logged-in user ID from the session
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new AccessDeniedException("User not logged in");
        }

        // Update the comment using the retrieved user ID
        commentService.updateComment(commentId, requestDTO.getText(),userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<GenericResponse> deleteComment(@PathVariable Long commentId) {
        GenericResponse response = commentService.deleteCommentByUser(commentId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping("admin/{commentId}")
    public ResponseEntity<GenericResponse> deleteCommentAdmin(@PathVariable Long commentId) {
        GenericResponse response = commentService.deleteCommentByAdmin(commentId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> getComment(@PathVariable Long commentId) {
        CommentResponseDTO responseDTO = commentService.getCommentById(commentId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentResponseDTO> responseDTOList = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(responseDTOList);
    }


}
