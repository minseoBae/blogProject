package com.example.blogproject.controller.user;

import com.example.blogproject.dto.UserSaveRequestDTO;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.filter.UserContextHolder;
import com.example.blogproject.service.blog.BlogService;
import com.example.blogproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final BlogService blogService;

    @GetMapping("/signupForm")
    public String signup(Model model) {
        model.addAttribute("user", new UserSaveRequestDTO());
        return "/user/signupForm";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute UserSaveRequestDTO userSaveRequestDTO,
                               BindingResult result, Model model) {
        if(result.hasErrors()){
            return "redirect:/user/signupForm";
        }
        User byUsername = userService.findByUsername(userSaveRequestDTO.getUsername());
        if(byUsername != null){
            result.rejectValue("username",null,"이미 사용중인 아이디입니다.");
            return "redirect:/user/signupForm";
        }
        try {
            userService.registerUser(userSaveRequestDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "서버 문제로 인해 회원 가입에 실패했습니다. 잠시 후 다시 시도해주세요.");
            return "/user/error";
        }
        model.addAttribute("username", userSaveRequestDTO.getUsername());
        return "/user/welcome";
    }

    @GetMapping("/loginForm")
    public String showLoginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/myPage/{id}")
    public String myPage(@PathVariable("id") Long userId, Model model) {
        User CurrentUser = UserContextHolder.getUser();
        User loginUser = userService.getUser(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못했습니다."));
        if(!Objects.equals(CurrentUser.getId(), loginUser.getId())) {
            return "redirect:/blog/home";
        }

        model.addAttribute("user", CurrentUser);

        return "user/myPage";
    }
}
