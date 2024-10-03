package com.softserve.itacademy.service;

import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.TaskRepository;
import com.softserve.itacademy.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private static Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init() {
        task = new Task();
        task.setId(1L);
        task.setName("Task Name");
        task.setPriority(Priority.HIGH);
        task.setTodo(new ToDo());
        task.setState(new State());
    }

    @Test
    public void createValidTaskTest() {
        taskService.create(task);
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    public void testReadById() {
        long id = 1L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        Task result = taskService.readById(id);
        Assertions.assertEquals(task, result);
    }

    @Test
    public void updateValidTaskTest() {
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        taskService.update(task);
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    public void deleteExistingTaskTest() {
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        taskService.delete(task.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).delete(task);
    }

    @Test
    public void deleteNotExistingTaskTest() {
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.delete(task.getId()));
    }

    @Test
    public void getAllTasksTest() {
        List<Task> taskList = Collections.singletonList(task);
        Mockito.when(taskRepository.findAll()).thenReturn(taskList);
        Assertions.assertEquals(taskService.getAll(), taskList);
    }

    @Test
    public void getByTodoIdTest() {
        long todoId = 1L;
        List<Task> taskList = Collections.singletonList(task);
        Mockito.when(taskRepository.getByTodoId(todoId)).thenReturn(taskList);
        Assertions.assertEquals(taskService.getByTodoId(todoId), taskList);
    }
}
