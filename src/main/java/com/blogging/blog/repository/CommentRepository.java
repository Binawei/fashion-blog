package com.blogging.blog.repository;

import com.blogging.blog.domain.Commentfac;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Commentfac, Long> {
    List<Commentfac> findByPostDesignId(Long postId);
}
