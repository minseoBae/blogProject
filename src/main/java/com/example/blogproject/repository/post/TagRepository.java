package com.example.blogproject.repository.post;

import com.example.blogproject.entity.post.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
