package com.blogging.blog.service;

import com.blogging.blog.domain.BlogUser;
import com.blogging.blog.domain.Commentfac;
import com.blogging.blog.domain.LikeTable;
import com.blogging.blog.domain.PostDesign;
import com.blogging.blog.exception.AccessDeniedException;
import com.blogging.blog.exception.ResourcesNotFoundException;
import com.blogging.blog.repository.BlogUserRepository;
import com.blogging.blog.repository.CommentRepository;
import com.blogging.blog.repository.LikeRepository;
import com.blogging.blog.repository.PostRepository;
import com.blogging.blog.service.iservices.ILikeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class LikeServiceImpl implements ILikeService {
    private final PostRepository postRepository;
    private final HttpSession session;
    private final BlogUserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public LikeServiceImpl(PostRepository postRepository, HttpSession session, BlogUserRepository userRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.session = session;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public String likePost(Long postId) {
        // Retrieve the logged-in user ID from the session
        Long userId = (Long) session.getAttribute("userid");
        if (userId == null) {
            throw new AccessDeniedException("User not logged in");
        }

        // Retrieve the post from the database
        Optional<PostDesign> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new ResourcesNotFoundException("Post not found");
        }
        PostDesign post = optionalPost.get();

        // Retrieve the user from the database
        Optional<BlogUser> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResourcesNotFoundException("User not found");
        }
        BlogUser user = optionalUser.get();

        // Check if the user has already liked the post
        if (likeRepository.existsByPostDesignAndBlogUser(post, user)) {
            throw new IllegalStateException("User has already liked the post");
        }

        // Create and save the like
        LikeTable like = new LikeTable();
        like.setPostDesign(post);
        like.setBlogUser(user);
        likeRepository.save(like);

        // Update the likeCount for the post
        if (post.getLikeCount() == null) {
            post.setLikeCount(0);
        }
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        return "You have liked the post";
    }


    @Override
    public String unlikePost(Long postId) {
        // Retrieve the logged-in user ID from the session
        Long userId = (Long) session.getAttribute("userid");
        if (userId == null) {
            throw new AccessDeniedException("User not logged in");
        }

        // Retrieve the post from the database
        Optional<PostDesign> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new ResourcesNotFoundException("Post not found");
        }
        PostDesign post = optionalPost.get();

        // Retrieve the user from the database
        Optional<BlogUser> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResourcesNotFoundException("User not found");
        }
        BlogUser user = optionalUser.get();

        // Check if the user has liked the post
        LikeTable like = likeRepository.findByPostDesignAndBlogUser(post, user);
        if (like == null) {
            throw new IllegalStateException("User has not liked the post");
        }

        // Delete the like
        likeRepository.delete(like);

        // Update the likeCount for the post
        post.setLikeCount(post.getLikeCount() - 1);
        postRepository.save(post);
        return "you have unliked";
    }

    @Override
    public String likeComment(Long commentId) {
        // Retrieve the logged-in user ID from the session
        Long userId = (Long) session.getAttribute("userid");
        if (userId == null) {
            throw new AccessDeniedException("User not logged in");
        }

        // Retrieve the comment from the database
        Optional<Commentfac> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResourcesNotFoundException("Comment not found");
        }
        Commentfac comment = optionalComment.get();

        // Retrieve the user from the database
        Optional<BlogUser> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResourcesNotFoundException("User not found");
        }
        BlogUser user = optionalUser.get();

        // Check if the user has already liked the comment
        if (likeRepository.existsByCommentAndBlogUser(comment, user)) {
            throw new ResourcesNotFoundException("User has already liked the comment");
        }

        // Create and save the like
        LikeTable like = new LikeTable();
        like.setComment(comment);
        like.setBlogUser(user);
        likeRepository.save(like);

        // Update the likeCount for the comment
        if (comment.getLikeCount() == null) {
            comment.setLikeCount(0);
        }
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);

        return "You have liked the comment";
    }


    @Override
    public String unlikeComment(Long commentId) {
        // Retrieve the logged-in user ID from the session
        Long userId = (Long) session.getAttribute("userid");
        if (userId == null) {
            throw new AccessDeniedException("User not logged in");
        }

        // Retrieve the comment from the database
        Optional<Commentfac> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResourcesNotFoundException("Comment not found");
        }
        Commentfac comment = optionalComment.get();

        // Retrieve the user from the database
        Optional<BlogUser> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResourcesNotFoundException("User not found");
        }
        BlogUser user = optionalUser.get();

        // Check if the user has liked the comment
        LikeTable like = likeRepository.findByCommentAndBlogUser(comment, user);
        if (like == null) {
            throw new IllegalStateException("User has not liked the comment");
        }

        // Delete the like
        likeRepository.delete(like);

        // Update the likeCount for the comment
        comment.setLikeCount(comment.getLikeCount() - 1);
        commentRepository.save(comment);
        return "you have unliked";
    }
}
