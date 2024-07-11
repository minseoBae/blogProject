package com.example.blogproject.repository.blog;

import com.example.blogproject.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByUserId(Long userId);
}
