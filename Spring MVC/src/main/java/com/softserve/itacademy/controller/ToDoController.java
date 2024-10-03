package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/todos")
public class ToDoController {

    private final ToDoService toDoService;
    private final UserService userService;

    public ToDoController(ToDoService toDoService, UserService userService) {
        this.toDoService = toDoService;
        this.userService = userService;
    }

    @GetMapping("/create/users/{owner_id}")
    public String createForm(@PathVariable("owner_id") long ownerId, Model model) {
        model.addAttribute("toDo", new ToDo());
        model.addAttribute("ownerId", ownerId);
        return "create-todo";
    }

    @PostMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long ownerId, @ModelAttribute ToDo toDo) {
        User owner = userService.readById(ownerId);
        toDo.setOwner(owner);
        toDoService.create(toDo);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/{todo_id}/update/users/{owner_id}")
    public String updateForm(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId, Model model) {
        ToDo toDo = toDoService.readById(todoId);
        model.addAttribute("toDo", toDo);
        model.addAttribute("ownerId", ownerId);
        return "update-todo";
    }

    @PostMapping("/{todo_id}/update/users/{owner_id}")
    public String update(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId, @ModelAttribute ToDo toDo) {
        toDo.setId(todoId);
        User owner = userService.readById(ownerId);
        toDo.setOwner(owner);
        toDoService.update(toDo);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/{todo_id}/delete/users/{owner_id}")
    public String delete(@PathVariable("todo_id") long todoId, @PathVariable("owner_id") long ownerId) {
        toDoService.delete(todoId);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/all/users/{user_id}")
    public String getAll(@PathVariable("user_id") long userId, Model model) {
        List<ToDo> todos = toDoService.getByUserId(userId);
        model.addAttribute("todos", toDoService.getByUserId(userId));
        model.addAttribute("userId", userId);
        return "todo-lists";
    }
}