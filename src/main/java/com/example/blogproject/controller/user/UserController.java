package com.example.blogproject.controller.user;

import com.example.blogproject.dto.UserSaveRequestDTO;
import com.example.blogproject.entity.user.User;
import com.example.blogproject.filter.UserContextHolder;
import com.example.blogproject.service.image.FileStorageService;
import com.example.blogproject.service.user.UserService;
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

    private final String uploadDir = "C://Temp/upload/";
    private final FileStorageService fileStorageService;

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

    @GetMapping("/myPage/{username}")
    public String myPage(HttpServletRequest request, @PathVariable String username, Model model) {
        User user = UserContextHolder.getUser();
        model.addAttribute("user", user);

        return "/user/myPage";
    }
}
