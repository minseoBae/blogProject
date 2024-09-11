package com.example.blogproject.controller.post;

import com.example.blogproject.dto.PostSaveRequestDTO;
import com.example.blogproject.entity.blog.Blog;
import com.example.blogproject.entity.post.Post;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.filter.UserContextHolder;
import com.example.blogproject.repository.blog.BlogRepository;
import com.example.blogproject.service.blog.BlogService;
import com.example.blogproject.service.post.PostService;
import com.example.blogproject.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final BlogService blogService;
    private final BlogRepository blogRepository;

    @GetMapping("showDetail/{id}")
    public String showPost(@PathVariable("id") Long postId, Model model) {
        User CurrentUser = UserContextHolder.getUser();

        Post post = postService.findById(postId);
        model.addAttribute("post", post);
        model.addAttribute("user", CurrentUser);
        return "/post/detailPost"; // Path to your Thymeleaf template
    }

    @GetMapping("/addForm/{id}")
    public String addForm(@PathVariable("id") Long blogId, Model model) {
        User CurrentUser = UserContextHolder.getUser();

        model.addAttribute("user", CurrentUser);
        model.addAttribute("blogId", blogId);
        model.addAttribute("post", new PostSaveRequestDTO());
        return "/post/addPost";
    }

    @PostMapping("/addPost")
    public String saveContent(@ModelAttribute PostSaveRequestDTO postSaveRequestDTO,
                              @RequestParam Long blogId) {
        try {
            Blog blog = blogRepository.findById(blogId).orElseThrow(() ->
                    new IllegalArgumentException("Invalid blog ID"));
            postService.savePost(postSaveRequestDTO, blog);
            return "redirect:/post/" + blog.getId();
        } catch (Exception e) {
            return "blog/home";
        }
    }

    @GetMapping("/updateForm/{id}")
    public String updateForm(@PathVariable("id") Long postId, Model model) {
        User CurrentUser = UserContextHolder.getUser();
        if(CurrentUser != null) {
            if (!postService.checkUser(CurrentUser.getId(), postId)) {
            };
        }
        model.addAttribute("user", CurrentUser);

        return "post/updatePost" + postId;
    }

    @GetMapping("/{id}")
    public String showPublicPosts(@PathVariable("id") Long blogId,
                           @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size,
                           Model model, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        User currentUser = userService.getCurrentUser(request);
        model.addAttribute("user", currentUser);
        if (blogService.getBlogById(blogId) != null) {
            Blog blog = blogService.getBlogById(blogId);
            blogService.sortedPosts(blog.getId());
            model.addAttribute("posts", postService.findAll(pageable, true, blogId));
            model.addAttribute("blog", blog);
            return "post/publishPost";
        } else {
            model.addAttribute("error", "찾으시는 블로그가 없습니다.");
            return "errorPage";
        }
    }

    @GetMapping("/draft/{id}")
    public String showDraftPosts(@PathVariable("id") Long blogId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size,
                                 Model model, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        User currentUser = userService.getCurrentUser(request);
        if (blogService.getBlogById(blogId) != null) {
            Blog blog = blogService.getBlogById(blogId);
            blogService.sortedPosts(blog.getId());
            model.addAttribute("user", currentUser);
            model.addAttribute("posts", postService.findAll(pageable, false, blogId));
            model.addAttribute("blog", blog);
            return "post/draftPost";
        } else {
            model.addAttribute("error", "찾으시는 블로그가 없습니다.");
            return "errorPage";
        }
    }
}
