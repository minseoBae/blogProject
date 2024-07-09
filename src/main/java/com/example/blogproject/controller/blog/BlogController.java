package com.example.blogproject.controller.blog;

import com.example.blogproject.entity.User;
import com.example.blogproject.jwt.util.JwtTokenizer;
import com.example.blogproject.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final UserRepository memberRepository;
    private final JwtTokenizer jwtTokenizer;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token != null) {
            Long userId = jwtTokenizer.getUserIdFromToken(token);
            User member = memberRepository.findById(userId).orElse(null);
            assert member != null;
            username = member.getUsername();
        }
        model.addAttribute("username", username);
        return "blog/home";
    }
}
