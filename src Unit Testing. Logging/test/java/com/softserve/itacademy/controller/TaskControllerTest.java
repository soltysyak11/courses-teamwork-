package com.softserve.itacademy.controller;

import com.softserve.itacademy.ToDoListApplication;
import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.model.*;
import com.softserve.itacademy.service.impl.StateServiceImpl;
import com.softserve.itacademy.service.impl.TaskServiceImpl;
import com.softserve.itacademy.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
@ContextConfiguration(classes = ToDoListApplication.class)
public class TaskControllerTest {

    private final int TODO_ID = 100;
    private final int INVALID_TODO_ID = 1000;
    private final long TASK_ID = 90;
    private final long INVALID_TASK_ID = 91;
    private final int STATE_ID = 80;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl taskService;

    @MockBean
    private ToDoServiceImpl toDoService;

    @MockBean
    private StateServiceImpl stateService;

    private ToDo toDo;
    private Task task1;
    private List<State> allStates;

    @BeforeEach
    void setUp() throws Exception {
        Role adminRole = new Role();
        adminRole.setName("Admin");

        User user = new User();
        user.setFirstName("Jack");
        user.setLastName("Nicholson");
        user.setEmail("jacknicholson@gmail.com");
        user.setPassword("password");
        user.setRole(adminRole);

        toDo = new ToDo();
        toDo.setId(TODO_ID);
        toDo.setTitle("Todo #1");
        toDo.setOwner(user);

        State newState = new State();
        newState.setId(STATE_ID);
        newState.setName("New");

        allStates = new ArrayList<>();
        allStates.add(newState);

        task1 = new Task();
        task1.setId(TASK_ID);
        task1.setName("Task #1");
        task1.setTodo(toDo);
        task1.setPriority(Priority.MEDIUM);
        task1.setState(newState);


        when(toDoService.readById(TODO_ID)).thenReturn(toDo);
        when(toDoService.readById(INVALID_TODO_ID)).thenThrow(
                new EntityNotFoundException("To-Do with id " + INVALID_TODO_ID + " not found")
        );
        when(toDoService.readById(TODO_ID)).thenReturn(toDo);
        when(stateService.readById(STATE_ID)).thenReturn(newState);
        when(stateService.getAll()).thenReturn(allStates);

    }

    @Test
    void testCreateTaskFormSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/create/todos/" + TODO_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create-task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("task", "todo", "priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("task", instanceOf(TaskDto.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("todo", toDo))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()));
    }

    @Test
    void testCreateTaskFormEmptyToDo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/create/" + INVALID_TODO_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testCreateTaskSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/create/todos/" + TODO_ID)
                        .param("name", "Task #1")
                        .param("priority", Priority.MEDIUM.name())
                        .param("todoId", String.valueOf(TODO_ID))
                ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/" + TODO_ID + "/tasks"));

        verify(taskService, times(1)).create(ArgumentMatchers.any(Task.class));
    }

    @Test
    void testCreateTaskInvalidPriority() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/create/todos/" + TODO_ID)
                .param("name", "Task #1")
                .param("priority", "Invalid priority")
                .param("todoId", String.valueOf(TODO_ID))
        ).andExpect(MockMvcResultMatchers.status().is5xxServerError());

        verify(taskService, never()).create(ArgumentMatchers.any(Task.class));
    }

    @Test
    void testCreateTaskEmptyTodo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/create/todos/" + INVALID_TODO_ID)
                .param("name", "Task #1")
                .param("priority", Priority.MEDIUM.name())
                .param("todoId", String.valueOf(INVALID_TODO_ID))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(taskService, never()).create(ArgumentMatchers.any(Task.class));
    }

    @Test
    void testCreateTaskInvalidModelAttribute() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/create/todos/" + TODO_ID)
                        .param("invalidAttribute", "Task #1")
                        .param("priority", Priority.MEDIUM.name())
                        .param("todoId", String.valueOf(TODO_ID))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create-task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("todo", "priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("todo", toDo))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()));
        ;

        verify(taskService, never()).create(ArgumentMatchers.any(Task.class));
    }

    @Test
    void testUpdateTaskForm() throws Exception {
        when(taskService.readById(TASK_ID)).thenReturn(task1);
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + TASK_ID + "/update/todos/" + TODO_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update-task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("task", "states", "priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("task", instanceOf(TaskDto.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("states", allStates))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()))
                // check Task DTO
                .andExpect(MockMvcResultMatchers.model().attribute("task", hasProperty("id", equalTo(TASK_ID))))
                .andExpect(MockMvcResultMatchers.model().attribute("task", hasProperty("name", equalTo(task1.getName()))))
                .andExpect(MockMvcResultMatchers.model().attribute("task", hasProperty("todoId", equalTo(task1.getTodo().getId()))))
                .andExpect(MockMvcResultMatchers.model().attribute("task", hasProperty("stateId", equalTo(task1.getState().getId()))));
    }

    @Test
    void testUpdateTaskSuccess() throws Exception {
        when(taskService.readById(TASK_ID)).thenReturn(task1);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/" + TASK_ID + "/update/todos/" + TODO_ID)
                        .param("name", "Task #1")
                        .param("priority", Priority.HIGH.name())
                        .param("todoId", String.valueOf(TODO_ID))
                ).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/" + TODO_ID + "/tasks"));

        verify(taskService, times(1)).update(ArgumentMatchers.any(Task.class));
    }

    @Test
    void testUpdateTaskInvalidPriority() throws Exception {
        when(taskService.readById(TASK_ID)).thenReturn(task1);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/" + TASK_ID + "/update/todos/" + TODO_ID)
                .param("name", "Task #1")
                .param("priority", "Invalid priority")
                .param("todoId", String.valueOf(TODO_ID))
        ).andExpect(MockMvcResultMatchers.status().is5xxServerError());

        verify(taskService, never()).update(ArgumentMatchers.any(Task.class));
    }

    @Test
    void testUpdateTaskEmptyTodo() throws Exception {
        when(taskService.readById(TASK_ID)).thenReturn(task1);
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/" + TASK_ID + "/update/todos/" + TODO_ID)
                .param("name", "Task #1")
                .param("priority", Priority.HIGH.name())
                .param("todoId", String.valueOf(INVALID_TODO_ID))
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());

        verify(taskService, never()).update(ArgumentMatchers.any(Task.class));
    }

    @Test
    void testUpdateTaskInvalidModelAttribute() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/tasks/" + TASK_ID + "/update/todos/" + TODO_ID)
                        .param("invalidArgument", "Task #1")
                        .param("priority", Priority.HIGH.name())
                        .param("todoId", String.valueOf(TODO_ID)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update-task"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("states", "priorities"))
                .andExpect(MockMvcResultMatchers.model().attribute("states", allStates))
                .andExpect(MockMvcResultMatchers.model().attribute("priorities", Priority.values()));

        verify(taskService, never()).update(ArgumentMatchers.any(Task.class));
    }

    @Test
    void testDeleteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + TASK_ID + "/delete/todos/" + TODO_ID))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/" + TODO_ID + "/tasks"));

        verify(taskService, times(1)).delete(TASK_ID);
    }

    @Test
    void testDeleteInvalidTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + INVALID_TASK_ID + "/delete/todos/" + TODO_ID))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/" + TODO_ID + "/tasks"));

        verify(taskService, times(1)).delete(INVALID_TASK_ID);
    }

    @Test
    void testDeleteEmptyToDo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + TASK_ID + "/delete/todos/" + INVALID_TODO_ID))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/todos/" + INVALID_TODO_ID + "/tasks"));

        verify(taskService, times(1)).delete(TASK_ID);
    }
}
