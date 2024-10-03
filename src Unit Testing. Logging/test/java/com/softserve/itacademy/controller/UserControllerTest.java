package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityNotFoundException;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)

public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");

        when(userService.create(any(User.class))).thenReturn(user);
        when(userService.readById(1L)).thenReturn(user);
    }

    @Test
    public void testCreateUserForm() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-user"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "Jane")
                        .param("lastName", "Doe")
                        .param("email", "jane.doe@example.com")
                        .param("password", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/todos/all/users/{id}"));

        verify(userService, times(1)).create(any(User.class));
    }


    @Test
    public void testUpdateUserForm() throws Exception {
        mockMvc.perform(get("/users/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-user"))
                .andExpect(model().attributeExists("user", "roles"));
    }


    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(get("/users/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));

        verify(userService, times(1)).delete(1L);
    }

    @Test
    public void testListAllUsers() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("users-list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    public void createUser_ValidationError() throws Exception {
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "")  // Помилка валідації: ім'я не може бути пустим
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-user"));
    }

    @Test
    public void getUpdateUserForm_NonExistentUser() throws Exception {
        when(userService.readById(999L)).thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get("/users/999/update"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteNonExistentUser() throws Exception {
        doThrow(new EntityNotFoundException("User not found")).when(userService).delete(999L);

        mockMvc.perform(get("/users/999/delete"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testListAllUsersEmpty() throws Exception {
        when(userService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("users-list"))
                .andExpect(model().attribute("users", hasSize(0)));
    }
}

