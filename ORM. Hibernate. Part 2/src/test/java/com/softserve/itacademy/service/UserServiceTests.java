package com.softserve.itacademy.service;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private User validUser;
    private Role validRole;

    @BeforeEach
    void setUp() {
        userService.getAll().forEach(user -> userService.delete(user.getId()));

        validRole = new Role();
        validRole.setName("TEST_ROLE");
        validRole = roleService.create(validRole);

        validUser = new User();
        validUser.setEmail("test@test.com");
        validUser.setFirstName("Test");
        validUser.setLastName("User");
        validUser.setPassword("Test@1234");
        validUser.setRole(validRole);
    }

    @Test
    public void createUserTest() {
        User createdUser = userService.create(validUser);
        assertNotNull(createdUser.getId(), "Created user should have an id");
        assertEquals(validUser.getEmail(), createdUser.getEmail(), "Emails should match");
    }

    @Test
    public void readUserByIdTest() {
        User createdUser = userService.create(validUser);
        User foundUser = userService.readById(createdUser.getId());
        assertEquals(createdUser.getId(), foundUser.getId(), "User ids should match");
    }

    @Test
    public void updateUserTest() {
        User createdUser = userService.create(validUser);
        createdUser.setFirstName("Updated");
        User updatedUser = userService.update(createdUser);
        assertEquals("Updated", updatedUser.getFirstName(), "First name should be updated");
    }

    @Test
    public void deleteUserTest() {
        User createdUser = userService.create(validUser);
        userService.delete(createdUser.getId());
        assertThrows(IllegalArgumentException.class, () -> userService.readById(createdUser.getId()), "User should be deleted");
    }

    @Test
    public void getAllUsersTest() {
        User validUser1 = new User();
        validUser1.setEmail("test1@test.com");
        validUser1.setFirstName("User");
        validUser1.setLastName("Test");
        validUser1.setPassword("Test1@1234");
        validUser1.setRole(validRole);

        userService.create(validUser);
        userService.create(validUser1);
        int expectedSize = 2;
        List<User> users = userService.getAll();
        assertTrue(users.size() >= expectedSize, String.format("At least %d users should be in the 'users' table", expectedSize));
    }
}