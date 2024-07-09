package com.example.blogproject.controller.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {
    @GetMapping("/errorPage")
    public String showErrorPage() {
        return "errorPage";
    }
}
