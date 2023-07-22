package com.blogging.blog.controller;

import com.blogging.blog.service.LikeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeServiceImpl likeService;

    public LikeController(LikeServiceImpl likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        likeService.likePost(postId);
        return ResponseEntity.ok("Post liked successfully");
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> unlikePost(@PathVariable Long postId) {
        likeService.unlikePost(postId);
        return ResponseEntity.ok("Post unliked successfully");
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<String> likeComment(@PathVariable Long commentId) {
        likeService.likeComment(commentId);
        return ResponseEntity.ok("Comment liked successfully");
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> unlikeComment(@PathVariable Long commentId) {

        likeService.unlikeComment(commentId);
        return ResponseEntity.ok("Comment unliked successfully");
    }
}
