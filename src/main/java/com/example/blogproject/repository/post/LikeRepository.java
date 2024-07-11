package com.example.blogproject.repository.post;

import com.example.blogproject.entity.post.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserIdAndPostId(Long userId, Long postId);
    List<Like> findAllByUserId(Long userId);
    Long countByPostId(Long postId);
}
