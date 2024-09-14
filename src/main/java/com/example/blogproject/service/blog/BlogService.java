package com.example.blogproject.service.blog;

import com.example.blogproject.dto.UserSaveRequestDTO;
import com.example.blogproject.entity.blog.Blog;
import com.example.blogproject.entity.post.Post;
import com.example.blogproject.repository.blog.BlogRepository;
import com.example.blogproject.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createBlog(UserSaveRequestDTO userSaveRequestDTO) {
        Blog blog = new Blog();
        blog.setTitle(userSaveRequestDTO.getUsername() + ".log");
        blog.setUser(userRepository.findByUsername(userSaveRequestDTO.getUsername()));
        blogRepository.save(blog);
    }

    @Transactional
    public void updateBlog(Blog blog) {
        blogRepository.findById(blog.getId()).ifPresent(existingBlog -> blogRepository.save(blog));
    }

    @Transactional
    public Page<Blog> findBlogByUserId(Pageable pageable, Long userId) {
        Pageable sortedByDescId = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return blogRepository.findAllByUserId(sortedByDescId, userId);
    }

    public Blog getBlogById(Long blogId) {
        return blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("블로그가 없습니다"));
    }

    public void sortedPosts(Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElse(null);
        assert blog != null;
        List<Post> list = blog.getPosts().stream()
                .sorted((post1, post2) -> post2.getCreatedAt().compareTo(post1.getCreatedAt()))
                .toList();
        blog.setPosts(list);
    }
}