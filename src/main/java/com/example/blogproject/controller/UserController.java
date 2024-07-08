package com.example.blogproject.controller;

import com.example.blogproject.entity.User;
import com.example.blogproject.filter.UserContextHolder;
import com.example.blogproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signupForm")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "/user/signupForm";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "redirect:/user/signupForm";
        }
        User byUsername = userService.findByUsername(user.getUsername());
        if(byUsername != null){
            result.rejectValue("username",null,"이미 사용중인 아이디입니다.");
            return "redirect:/user/signupForm";
        }
        try {
            userService.registerUser(user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "서버 문제로 인해 회원 가입에 실패했습니다. 잠시 후 다시 시도해주세요.");
            return "/user/error";
        }
        model.addAttribute("username", user.getUsername());
        return "/user/welcome";
    }

    @GetMapping("/loginForm")
    public String showLoginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/myPage/{username}")
    public String myPage(HttpServletRequest request, @PathVariable String username, Model model) {
        User user = UserContextHolder.getUser();
        model.addAttribute("user", user);

        return "/user/myPage";
    }
}
