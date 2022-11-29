package com.example.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class AdminController {
    @Deprecated
    @RequestMapping(value = {"/oldLogin"})
    public String oldLogin(Model model) {
        return "oldLogin";
    }

    @RequestMapping(value = {"/login"})
    public String login(Model model) {
        return "login";
    }
}
