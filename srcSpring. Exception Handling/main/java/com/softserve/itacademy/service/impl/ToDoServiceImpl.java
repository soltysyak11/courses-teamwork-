package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.ToDoRepository;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository todoRepository;

    public ToDoServiceImpl(ToDoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public ToDo create(ToDo todo) {
        if (todo == null) {
            throw new NullEntityReferenceException("Cannot create a ToDo as the passed object is null.");
        }
        return todoRepository.save(todo);
    }

    @Override
    public ToDo readById(long id) {
        return todoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("ToDo not found with id " + id)
        );
    }

    @Override
    public ToDo update(ToDo todo) {
        if (todo == null || (todo.getTitle() != null && todo.getTitle().trim().isEmpty())) {
            throw new NullEntityReferenceException("Cannot update a ToDo as the passed object is null or title is empty.");
        }
        // This ensures ToDo exists and throws EntityNotFoundException if it doesn't
        ToDo existingToDo = readById(todo.getId());
        return todoRepository.save(todo);
    }

    @Override
    public void delete(long id) {
        // This ensures ToDo exists before deletion and throws EntityNotFoundException if it doesn't
        ToDo todo = readById(id);
        todoRepository.delete(todo);
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> todos = todoRepository.findAll();
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        List<ToDo> todos = todoRepository.getByUserId(userId);
        if (todos.isEmpty()) {
            throw new EntityNotFoundException("No ToDos found for user id " + userId);
        }
        return todos;
    }
}
