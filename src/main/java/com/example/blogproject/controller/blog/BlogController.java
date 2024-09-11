package com.example.blogproject.controller.blog;

import com.example.blogproject.entity.blog.Blog;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.service.blog.BlogService;
import com.example.blogproject.service.post.PostService;
import com.example.blogproject.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final UserService userService;
    private final BlogService blogService;
    private final PostService postService;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response) {
        User currentUser = userService.getCurrentUser(request);

        model.addAttribute("user", currentUser);
        return "blog/home";
    }

    @GetMapping("/updateForm/{id}")
    public String updateBlogForm(@PathVariable Long id, HttpServletRequest request, Model model) {
        Blog blog = blogService.getBlogById(id);
        User currentUser = userService.getCurrentUser(request);
        if (currentUser != null) {
            model.addAttribute("userId", currentUser.getId());
            model.addAttribute("username", currentUser.getUsername());
            model.addAttribute("blog", blog);
            return "/blog/updateBlog";
        }
        return "redirect:/user/loginForm";
    }

    @PostMapping("/update")
    public String updateBlog(@ModelAttribute Blog blog, HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);
        try {
            blogService.updateBlog(blog);
            return "redirect:/blog/" + currentUser.getId();
        } catch (Exception e) {
            return "redirect:/blog/updateForm";
        }
    }

    @GetMapping("/{id}")
    public String showBlog(@PathVariable("id") Long userId,
                           @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size,
                              Model model, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        User currentUser = userService.getCurrentUser(request);
        model.addAttribute("user", currentUser);
        model.addAttribute("blogs", blogService.findBlogByUserId(pageable, userId));
        return "/blog/myBlog";
    }
}
