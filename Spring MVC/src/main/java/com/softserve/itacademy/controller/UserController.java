package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", result.getFieldError().getDefaultMessage());
            model.addAttribute("errorField", result.getFieldError().getField());
            model.addAttribute("user", new User());
            return "create-user";
        }
        model.addAttribute("user", user);
        userService.create(user);
        return "redirect:/todos/all/users/" + user.getId();
//        return "redirect:/home";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAll());
        return "update-user";
    }

    @PostMapping("/{id}/update")
    public String update(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", result.getFieldError().getDefaultMessage());
            model.addAttribute("errorField", result.getFieldError().getField());
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAll());
            return "update-user";
        }
        Role role = roleService.readById(user.getRole().getId());
        user.setRole(role);
        userService.update(user);
        return "redirect:/home";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "redirect:/home";
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users-list";
    }
}