package com.blogging.blog.controller;

import com.blogging.blog.dto.request.PostRequestDTO;
import com.blogging.blog.dto.response.PostResponseDTO;
import com.blogging.blog.exception.AccessDeniedException;
import com.blogging.blog.service.iservices.IPostServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/design")
public class PostController {
    private final IPostServices postService;

    public PostController(IPostServices postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostRequestDTO request) {
        PostResponseDTO responseDTO = postService.createPostDesign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        PostResponseDTO responseDTO = postService.getPostById(postId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId,
                                                      @RequestBody PostRequestDTO request) {
        PostResponseDTO responseDTO = postService.updatePost(postId, request);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
