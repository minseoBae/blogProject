package com.example.blogproject.service.blog;

import com.example.blogproject.entity.blog.Blog;
import com.example.blogproject.entity.post.Post;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.blog.BlogRepository;
import com.example.blogproject.repository.post.PostRepository;
import com.example.blogproject.repository.user.UserRepository;
import com.example.blogproject.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;

    @Transactional
    public Long createBlog(String title, HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        if (user == null) {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setUser(user);
        blogRepository.save(blog);
        return blog.getId();
    }

    @Transactional
    public void updateBlog(Blog blog) {
        blogRepository.findById(blog.getId()).ifPresent(existingBlog -> blogRepository.save(blog));
    }

    @Transactional
    public List<Blog> findBlogByUserId(Long id) {
        return blogRepository.findAllByUserId(id);
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

//    public Blog getBlogByPostId(Long postId) {
//        Post post = postRepository.findByPostId(postId);
//        if (post != null) {
//            return post.getBlog();
//        }
//        return null;
//    }


}