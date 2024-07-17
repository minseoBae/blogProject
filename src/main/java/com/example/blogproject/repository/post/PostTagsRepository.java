package com.example.blogproject.repository.post;

import com.example.blogproject.entity.post.PostTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagsRepository extends JpaRepository<PostTags, Long> {
}