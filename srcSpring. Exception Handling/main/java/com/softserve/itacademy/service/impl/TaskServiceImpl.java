package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.EntityNotFoundException;
import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.repository.TaskRepository;
import com.softserve.itacademy.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task task) {
        if (task == null) {
            throw new NullEntityReferenceException("Task cannot be null");
        }
        return taskRepository.save(task);
    }

    @Override
    public Task readById(long id) {
        Optional<Task> optional = taskRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
        return optional.get();
    }

    @Override
    public Task update(Task task) {
        if (task == null) {
            throw new NullEntityReferenceException("Task cannot be null");
        }
        if (!taskRepository.existsById(task.getId())) {
            throw new EntityNotFoundException("Task with id " + task.getId() + " not found");
        }
        return taskRepository.save(task);
    }

    @Override
    public void delete(long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }


    @Override
    public List<Task> getByTodoId(long todoId) {
        List<Task> tasks = taskRepository.getByTodoId(todoId);
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }
}