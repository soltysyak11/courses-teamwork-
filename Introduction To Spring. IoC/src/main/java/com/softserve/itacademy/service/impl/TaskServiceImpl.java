package com.softserve.itacademy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;

@Service
public class TaskServiceImpl implements TaskService {

    private ToDoService toDoService;

    @Autowired
    public TaskServiceImpl(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    private boolean isTaskNameExists(List<Task> tasks, String taskName) {
        return tasks.stream()
                .anyMatch(existingTask -> existingTask.getName().equals(taskName));
    }

    public Task addTask(Task task, ToDo todo) {
        if (task == null || todo == null) {
            throw new IllegalArgumentException("Task and ToDo cannot be null.");
        }
        List<Task> tasks = todo.getTasks();
        if (isTaskNameExists(tasks, task.getName())) {
            throw new IllegalArgumentException("Task with the same name already exists");
        }
        tasks.add(task);
        toDoService.updateTodo(todo);
        return task;
    }

    public Task updateTask(Task taskToUpdate) {
        if (taskToUpdate == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        List<ToDo> allToDos = toDoService.getAll();
        for (ToDo toDo : allToDos) {
            List<Task> tasks = toDo.getTasks();
            for (Task task : tasks) {
                if (task.equals(taskToUpdate)) {
                    task.setName(taskToUpdate.getName());
                    task.setPriority(taskToUpdate.getPriority());
                    toDoService.updateTodo(toDo);
                    return task;
                }
            }
        }
        return null;
    }

    public void deleteTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        List<ToDo> allToDos = toDoService.getAll();
        for (ToDo toDo : allToDos) {
            List<Task> tasks = toDo.getTasks();
            if (tasks.remove(task)) {
                toDoService.updateTodo(toDo);
            }
        }
    }

    public List<Task> getAll() {
        return toDoService.getAll().stream()
                .flatMap(toDo -> toDo.getTasks().stream())
                .collect(Collectors.toList());
    }

    public List<Task> getByToDo(ToDo todo) {
        if (todo == null) {
            throw new IllegalArgumentException("ToDo cannot be null.");
        }
        return todo.getTasks();
    }

    public Task getByToDoName(ToDo todo, String name) {
        if (todo == null || name == null) {
            throw new IllegalArgumentException("ToDo and name cannot be null.");
        }
        return todo.getTasks().stream()
                .filter(task -> task.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Task getByUserName(User user, String name) {
        if (user == null || name == null) {
            throw new IllegalArgumentException("User and name cannot be null.");
        }
        return toDoService.getAll().stream()
                .flatMap(toDo -> toDo.getTasks().stream())
                .filter(task -> task.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
