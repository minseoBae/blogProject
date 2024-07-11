package com.example.blogproject.repository.post;

import com.example.blogproject.entity.post.PostTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagsRepository extends JpaRepository<PostTags, Long> {
}