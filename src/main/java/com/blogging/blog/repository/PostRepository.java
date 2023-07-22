package com.blogging.blog.repository;

import com.blogging.blog.domain.PostDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostDesign, Long> {
}
