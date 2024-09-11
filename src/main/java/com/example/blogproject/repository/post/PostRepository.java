package com.example.blogproject.repository.post;

import com.example.blogproject.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("UPDATE Post p SET p.likes = :likeCnt WHERE p.id = :postId")
    void updateLikes(@Param("postId") Long postId, @Param("likeCnt") Long likeCnt);
    Page<Post> findAllByBlogIdAndStatus(Long blogId, Boolean status, Pageable pageable);}
