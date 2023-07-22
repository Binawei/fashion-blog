package com.blogging.blog.service;

import com.blogging.blog.domain.Admin;
import com.blogging.blog.domain.BlogUser;
import com.blogging.blog.domain.Commentfac;
import com.blogging.blog.domain.PostDesign;
import com.blogging.blog.dto.request.CommentRequestDTO;
import com.blogging.blog.dto.response.CommentResponseDTO;
import com.blogging.blog.exception.AccessDeniedException;
import com.blogging.blog.exception.ResourcesNotFoundException;
import com.blogging.blog.repository.AdminRepository;
import com.blogging.blog.repository.BlogUserRepository;
import com.blogging.blog.repository.CommentRepository;
import com.blogging.blog.repository.PostRepository;
import com.blogging.blog.service.iservices.ICommentService;
import com.blogging.blog.utility.GenericResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CommentServiceImpl implements ICommentService {
    private final PostRepository postRepository;
    private final BlogUserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AdminServicesImpl adminServices;
    private final AdminRepository adminRepository;
    private  final HttpSession session;

    public CommentServiceImpl(PostRepository postRepository, BlogUserRepository userRepository, CommentRepository commentRepository, AdminServicesImpl adminServices, AdminRepository adminRepository, HttpSession session) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.adminServices = adminServices;
        this.adminRepository = adminRepository;
        this.session = session;
    }

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO, Long postId, Long userId, boolean isAdmin) {
        // Retrieve the post from the database
        Optional<PostDesign> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new ResourcesNotFoundException("Post not found");
        }
        PostDesign post = optionalPost.get();

        // Retrieve the user or admin from the database based on the "isAdmin" flag
        Object userOrAdmin;
        if (isAdmin) {
            // Retrieve the admin from the session using the "userId" attribute
            Long adminId = (Long) session.getAttribute("adminId");
            if (adminId == null) {
                throw new AccessDeniedException("Admin not logged in");
            }
            userOrAdmin = adminRepository.findById(adminId).orElseThrow(() -> new ResourcesNotFoundException("Admin not found"));
        } else {
            Optional<BlogUser> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new ResourcesNotFoundException("User not found");
            }
            userOrAdmin = optionalUser.get();
        }

        // Create and save the comment
        Commentfac comment = Commentfac.builder()
                .text(requestDTO.getText())
                .postDesign(post)
                .createdAt(LocalDateTime.now())
                .build();

        if (!isAdmin) {
            // Set the admin field to null for comments created by regular users
            comment.setAdmin(null);

            if (userOrAdmin instanceof BlogUser) {
                comment.setUser((BlogUser) userOrAdmin);
            } else {
                throw new IllegalStateException("Invalid user or admin object");
            }
        } else {
            if (userOrAdmin instanceof Admin) {
                comment.setAdmin((Admin) userOrAdmin);
            } else {
                throw new IllegalStateException("Invalid admin object");
            }
        }

        Commentfac savedComment = commentRepository.save(comment);

        // Create and return the comment response DTO
        return CommentResponseDTO.builder()
                .commentId(savedComment.getId())
                .text(savedComment.getText())
                .userId(userId)
                .postId(savedComment.getPostDesign().getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String updateComment(Long commentId, String commentText, Long userId) {
        // Retrieve the comment from the database
        Optional<Commentfac> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResourcesNotFoundException("Comment not found");
        }
        Commentfac comment = optionalComment.get();

        // Check if the comment belongs to the user
        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to update this comment");
        }

        // Update the comment text
        comment.setText(commentText);
        commentRepository.save(comment);
        return  commentText +" has been updated";
    }

    @Override
    public CommentResponseDTO getCommentById(Long commentId) {
        // Retrieve the logged-in user ID from the session
        Long loggedInUserId = (Long) session.getAttribute("userId");
        if (loggedInUserId == null) {
            throw new AccessDeniedException("User not logged in");
        }

        // Retrieve the comment by ID
        Commentfac comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourcesNotFoundException("Comment not found"));

        // Check if the logged-in user is the owner of the comment or an admin
        if (!comment.getUser().getId().equals(loggedInUserId) || !adminServices.isAdmin(loggedInUserId)) {
            throw new AccessDeniedException("You are not authorized to access this comment");
        }

        // Create and return the DTO response
        return CommentResponseDTO.builder()
                .commentId(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .userId(comment.getUser().getId())
                .postId(comment.getPostDesign().getId())
                .build();
    }

    @Override
    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        // Retrieve the logged-in user ID from the session
        Long loggedInUserId = (Long) session.getAttribute("userId");
        if (loggedInUserId == null) {
            throw new AccessDeniedException("User not logged in");
        }

        // Retrieve the comments by post ID
        List<Commentfac> comments = commentRepository.findByPostDesignId(postId);


        return comments.stream()
                .map(comment -> CommentResponseDTO.builder()
                        .commentId(comment.getId())
                        .text(comment.getText())
                        .createdAt(comment.getCreatedAt())
                        .userId(comment.getUser().getId())
                        .postId(comment.getPostDesign().getId())
                        .build())
                .collect(Collectors.toList());
    }
    @Override
    public GenericResponse deleteCommentByUser(Long commentId)
    {
        Long userId = (Long) session.getAttribute("userid");
        if (userId == null) {
            return GenericResponse.builder().status("please log in")
                    .status("01").httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Commentfac> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new ResourcesNotFoundException("Comment not found");
        }

        Commentfac comment = optionalComment.get();
        if (!comment.getUser().getId().equals(userId)) {
            return GenericResponse.builder().message("Sorry you are not the owner of the comment")
                    .status("01").httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        commentRepository.delete(comment);
        return GenericResponse.builder().status("00").message("Deleted successfully by user")
                .httpStatus(HttpStatus.OK).build();
    }
    @Override
    public GenericResponse deleteCommentByAdmin(Long commentId)
    {
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null)
        {
            return GenericResponse.builder().message("please log in to delete te comment")
                    .status("01").httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Commentfac>optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty())
        {
            return GenericResponse.builder().message("Comment not found").status("01")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        Commentfac comment = optionalComment.get();
        commentRepository.delete(comment);
        return GenericResponse.builder().message("Deleted successfully").status("00")
                .httpStatus(HttpStatus.OK).build();
    }


}
