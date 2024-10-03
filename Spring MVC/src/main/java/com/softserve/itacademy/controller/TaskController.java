package com.softserve.itacademy.controller;


import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;
    private final Logger logger= LoggerFactory.getLogger(TaskController.class);

    @GetMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") Long todoId, Model model) {
        model.addAttribute("taskDto", new TaskDto());
        model.addAttribute("todoId", todoId);
        model.addAttribute("priorities", Priority.values());
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    public String create(@PathVariable("todo_id") Long todoId, @Validated @ModelAttribute("taskDto") TaskDto taskDto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("todoId", todoId);
            model.addAttribute("priorities", Priority.values());
            return "create-task";
        }

        ToDo todo = todoService.readById(todoId);
        State state = stateService.readById(taskDto.getStateId());
        Task task = TaskTransformer.convertToEntity(taskDto, todo, state);
        taskService.create(task);

        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId, Model model) {
        Task task = taskService.readById(taskId);
        List<State> states = stateService.getAll();
        TaskDto taskDto = TaskTransformer.convertToDto(task);
        model.addAttribute("taskDto", taskDto);
        model.addAttribute("todoId", todoId);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("states", stateService.getAll());
        model.addAttribute("states", states);
        model.addAttribute("todo", task.getTodo());
        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    public String update(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId,
                         @Validated @ModelAttribute("taskDto") TaskDto taskDto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("taskDto", taskDto);
            model.addAttribute("todoId", todoId);
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("states", stateService.getAll());
            model.addAttribute("states", stateService.getAll());
            return "update-task";
        }

        ToDo todo = todoService.readById(todoId);
        State state = stateService.readById(taskDto.getStateId());
        Task task = TaskTransformer.convertToEntity(taskDto, todo, state);
        task.setId(taskId);
        taskService.update(task);

        return "redirect:/todos/" + todoId + "/tasks";
    }

    @GetMapping("/{task_id}/delete/todos/{todo_id}")
    public String delete(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId) {
        taskService.delete(taskId);
        return "redirect:/todos/" + todoId + "/tasks";
    }
}

