package com.blogging.blog.repository;

import com.blogging.blog.domain.BlogUser;
import com.blogging.blog.domain.Commentfac;
import com.blogging.blog.domain.LikeTable;
import com.blogging.blog.domain.PostDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LikeRepository extends JpaRepository<LikeTable, Long> {
    boolean existsByPostDesignAndBlogUser(PostDesign post, BlogUser user);

    boolean existsByCommentAndBlogUser(Commentfac comment, BlogUser user);
    LikeTable findByCommentAndBlogUser(Commentfac comment, BlogUser user);
    LikeTable findByPostDesignAndBlogUser(PostDesign post, BlogUser user);


}
