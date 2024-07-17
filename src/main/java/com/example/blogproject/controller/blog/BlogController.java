package com.example.blogproject.controller.blog;

import com.example.blogproject.entity.blog.Blog;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.service.blog.BlogService;
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
    private final BlogService blogService;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response) {
        User currentUser = userService.getCurrentUser(request);
        String username = null;

        if(currentUser != null) {
            username = currentUser.getUsername();
        }

        model.addAttribute("username", username);
        return "blog/home";
    }

    @GetMapping("/createForm")
    public String createBlogForm(HttpServletRequest request, Model model) {
        User currentUser = userService.getCurrentUser(request);
        if (currentUser != null) {
            model.addAttribute("blog", new Blog());
            model.addAttribute("user", currentUser);
            return "/blog/createBlog";
        }
        return "redirect:/user/loginForm";
    }

    @PostMapping("/create")
    public String createBlog(@RequestParam("title") String title,
                             HttpServletRequest request) {
        try {
            Long blogId = blogService.createBlog(title, request);
            return "redirect:/blog/" + blogId;
        } catch (Exception e) {
            return "redirect:/blog/createForm";
        }
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
        try {
            blogService.updateBlog(blog);
            return "redirect:/blog/" + blog.getId();
        } catch (Exception e) {
            return "redirect:/blog/updateForm";
        }
    }

//    @GetMapping("/{id}")
//    public String showBlog(@PathVariable("id") Long blogId,
//                              Model model,
//                              HttpServletRequest request) {
//        User currentUser = userService.getCurrentUser(request);
//        Long userId = currentUser.getId();
//        model.addAttribute("username", currentUser.getUsername());
//        if (blogService.getBlogById(blogId) != null) {
//            Blog blog = blogService.getBlogById(blogId);
//            blogService.sortedPosts(blog.getId());
//            model.addAttribute("posts", postService.(true));
//            model.addAttribute("blog", blog);
//            return "/blog/myBlog";
//        } else {
//            model.addAttribute("error", "찾으시는 블로그가 없습니다.");
//            return "/errorPage";
//        }
//    }
//
//    @GetMapping("/draft/{id}")
//    public String showDraftPosts(@PathVariable("id") Long blogId, Model model, HttpServletRequest request) {
//        User currentUser = userService.getCurrentUser(request);
//        model.addAttribute("user", currentUser);
//        model.addAttribute("blog", blogService.getBlogById(blogId));
//        model.addAttribute("posts", postService.getDraftPostsByBlog(blogId));
//        return "/blog/draft";
//    }
}
