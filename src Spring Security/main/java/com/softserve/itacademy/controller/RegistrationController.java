package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";

    }
    @PostMapping("/register")
    public String registerUser(@Validated @ModelAttribute("user") User user, BindingResult result) {
        if(result.hasErrors()){
            return "register";
        }
        Role userRole=roleService.readById(2);
        user.setPassword(user.getPassword());
        user.setRole(userRole);
        userService.create(user);
        return "redirect:/login";
    }
}
