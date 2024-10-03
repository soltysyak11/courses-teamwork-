package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        logger.info("Displaying user creation form");
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("user") User user, BindingResult result) {
        logger.info("Attempting to create a new user");
        if (result.hasErrors()) {
            logger.error("Validation errors while creating user: {}", result);
            return "create-user";
        }
        user.setPassword(user.getPassword()); // Consider using a service to encrypt the password
        user.setRole(roleService.readById(2)); // The role should be validated or handled dynamically
        User newUser = userService.create(user);
        logger.info("Created new user with ID {}", newUser.getId());
        return "redirect:/todos/all/users/" + newUser.getId();
    }

    @GetMapping("/{id}/read")
    public String read(@PathVariable long id, Model model) {
        logger.info("Reading user with ID {}", id);
        User user = userService.readById(id);
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable long id, Model model) {
        logger.info("Displaying update form for user ID {}", id);
        User user = userService.readById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAll());
        return "update-user";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable long id, Model model, @Validated @ModelAttribute("user") User user, @RequestParam("roleId") long roleId, BindingResult result) {
        logger.info("Updating user ID {}", id);
        if (result.hasErrors()) {
            logger.error("Validation errors while updating user ID {}: {}", id, result);
            User oldUser = userService.readById(id);
            user.setRole(oldUser.getRole());
            model.addAttribute("roles", roleService.getAll());
            return "update-user";
        }
        user.setRole(roleService.readById(roleId));
        userService.update(user);
        logger.info("Updated user ID {}", id);
        return "redirect:/users/" + id + "/read";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        logger.warn("Deleting user ID {}", id);
        userService.delete(id);
        return "redirect:/users/all";
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        logger.info("Listing all users");
        model.addAttribute("users", userService.getAll());
        return "users-list";
    }
}
