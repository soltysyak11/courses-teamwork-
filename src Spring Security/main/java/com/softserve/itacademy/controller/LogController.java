package com.softserve.itacademy.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogController {

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login-form";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        return "redirect:/login?logout=true";
    }
}
