package com.example.blogproject.service.blog;

import com.example.blogproject.entity.post.Post;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final UserRepository userRepository;

    @Transactional
    public void updateBlog(User user) {
        userRepository.findById(user.getId()).ifPresent(existingUser -> userRepository.save(user));
    }

    public void sortedPosts(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        assert user != null;
        List<Post> list = user.getPosts().stream()
                .sorted((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()))
                .toList();
        user.setPosts(list);
    }
}