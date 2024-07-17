package com.example.blogproject.service.post;

import com.example.blogproject.repository.blog.BlogRepository;
import com.example.blogproject.repository.post.PostRepository;
import com.example.blogproject.repository.post.PostTagsRepository;
import com.example.blogproject.repository.post.TagRepository;
import com.example.blogproject.repository.user.UserRepository;
import com.example.blogproject.service.image.FileStorageService;
import com.example.blogproject.service.user.UserService;
import com.example.blogproject.service.blog.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PostService {
    private final PostTagsRepository postTagsRepository;
    private final PostRepository postRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final UserService userService;
    private final BlogService blogService;
    private final FileStorageService fileStorageService;


}