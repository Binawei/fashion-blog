package com.blogging.blog.service;

import com.blogging.blog.domain.Admin;
import com.blogging.blog.domain.PostDesign;
import com.blogging.blog.dto.request.PostRequestDTO;
import com.blogging.blog.dto.response.PostResponseDTO;
import com.blogging.blog.exception.AccessDeniedException;
import com.blogging.blog.exception.ResourcesNotFoundException;
import com.blogging.blog.repository.AdminRepository;
import com.blogging.blog.repository.PostRepository;
import com.blogging.blog.service.iservices.AdminServices;
import com.blogging.blog.service.iservices.IPostServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class PostServiceImpl implements IPostServices {
    private final AdminRepository adminRepository;
    private final  PostRepository postRepository;
    private final AdminServices adminServices;
    private final HttpSession session;

    public PostServiceImpl(AdminRepository adminRepository, PostRepository postRepository, AdminServices adminServices, HttpSession session) {
        this.adminRepository = adminRepository;
        this.postRepository = postRepository;
        this.adminServices = adminServices;
        this.session = session;
    }


    @Override
    public PostResponseDTO createPostDesign(PostRequestDTO request) {
        // Retrieve the admin ID from the session
        Long adminId = (Long) session.getAttribute("adminId");
        if (adminId == null) {
            throw new AccessDeniedException("Admin not logged in");
        }

        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isEmpty()) {
            throw new ResourcesNotFoundException("Admin does not exist");
        }

        Admin admin = optionalAdmin.get();

        // Create a new Design object and set its properties
        PostDesign design = PostDesign.builder()
                .category(request.getCategory())
                .content(request.getContent())
                .admin(admin)
                .timestamp(LocalDateTime.now())
                .build();

        // Save Design to the database
        PostDesign savedPost = postRepository.save(design);

        // Create and return the DTO response
        return PostResponseDTO.builder()
                .postId(savedPost.getId())
                .content(savedPost.getContent())
                .category(savedPost.getCategory())
                .build();
    }




    @Override
    public PostResponseDTO getPostById(Long postId) {
        Optional<PostDesign> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new ResourcesNotFoundException("PostDesign not found",HttpStatus.NOT_FOUND);
        }

        PostDesign post = optionalPost.get();

        // Create and return the DTO response
        return PostResponseDTO.builder()
                .postId(post.getId())
                .content(post.getContent())
                .category(post.getCategory())
                .build();
    }

    @Override
    public PostResponseDTO updatePost(Long postId, PostRequestDTO request) {
        Optional<PostDesign> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new ResourcesNotFoundException("Post not found");
        }

        PostDesign post = optionalPost.get();

        // Check if the person is logged in as admin
        if (!adminServices.isAdmin(post.getAdmin().getId())) {
            throw new AccessDeniedException("You are not authorized to update this post", HttpStatus.FORBIDDEN);
        }

        // Update the post properties
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());

        // Save the updated post to the database
        PostDesign updatedPost = postRepository.save(post);

        // Create and return the DTO response
        return PostResponseDTO.builder()
                .postId(updatedPost.getId())
                .content(updatedPost.getContent())
                .category(updatedPost.getCategory())
                .build();
    }

    @Override
    public void deletePost(Long postId) {
        // Retrieve the postDesign by ID
        Optional<PostDesign> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new ResourcesNotFoundException("Post not found");
        }

        // Retrieve the logged-in admin ID
        Long adminId = (Long) session.getAttribute("userid");
        if (adminId == null) {
            throw new ResourcesNotFoundException("Please log in first");
        }

        // Check if the logged-in user is an admin
        if (!adminServices.isAdmin(adminId)) {
            throw new ResourcesNotFoundException("Only admins can delete a post");
        }

        // Delete the postDesign
        postRepository.delete(optionalPost.get());
    }

}
