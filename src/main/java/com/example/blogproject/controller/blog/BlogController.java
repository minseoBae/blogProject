package com.example.blogproject.controller.blog;

import com.example.blogproject.entity.user.User;
import com.example.blogproject.service.blog.BlogService;
import com.example.blogproject.service.post.PostService;
import com.example.blogproject.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final UserService userService;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response) {
        User currentUser = userService.getCurrentUser(request);

        model.addAttribute("user", currentUser);
        return "blog/home";
    }

    @GetMapping("/myBlog")
    public String myBlog(Model model, HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);

        model.addAttribute("user", currentUser);
        return "blog/myBlog";
    }
}
