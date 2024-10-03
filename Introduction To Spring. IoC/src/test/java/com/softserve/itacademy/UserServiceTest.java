package com.softserve.itacademy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;

import java.util.List;

public class UserServiceTest {

    private static UserService userService;

    @BeforeAll
    public static void setupBeforeClass() throws Exception {
        AnnotationConfigApplicationContext annotationConfigContext = new AnnotationConfigApplicationContext(Config.class);
        userService = annotationConfigContext.getBean(UserService.class);
        annotationConfigContext.close();
    }

    @Test
    public void checkAddUser() {
        User user = new User("John", "Doe", "johndoe@example.com", "password123", null);
        User actual = userService.addUser(user);
        Assertions.assertNotNull(actual, "The added user should not be null");
        Assertions.assertEquals("John", actual.getFirstName(), "Expected first name to be John");
    }

    @Test
    public void checkUpdateUser() {
        User user = new User("Alice", "Smith", "alicesmith@example.com", "password123", null);
        userService.addUser(user);
        user.setLastName("Johnson");
        user.setPassword("newPassword123");
        User actual = userService.updateUser(user);
        Assertions.assertNotNull(actual, "Updated user should not be null");
        Assertions.assertEquals("Johnson", actual.getLastName(), "Expected last name to be updated");
    }

    @Test
    public void checkDeleteUser() {
        User user = new User("Bob", "Brown", "bobbrown@example.com", "password123", null);
        userService.addUser(user);
        userService.deleteUser(user);
        Assertions.assertNull(userService.getUser(user.getEmail()), "User should be deleted");
    }

    @Test
    public void checkGetAllUsers() {
        // Clear existing users and add fresh test data
        List<User> allUsers = userService.getAll();
        allUsers.forEach(user -> userService.deleteUser(user));

        User user1 = new User("Carol", "White", "carolwhite@example.com", "password123", null);
        User user2 = new User("Dave", "Green", "davegreen@example.com", "password123", null);
        userService.addUser(user1);
        userService.addUser(user2);
        List<User> users = userService.getAll();
        Assertions.assertTrue(users.size() >= 2, "There should be at least two users in the list");
        Assertions.assertTrue(users.contains(user1) && users.contains(user2), "The list should contain both users");
    }

    @Test
    public void checkFindByEmail() {
        User user = new User("Eve", "Blue", "eveblue@example.com", "password123", null);
        userService.addUser(user);
        User foundUser = userService.findByEmail("eveblue@example.com");
        Assertions.assertEquals(user, foundUser, "User should be found by email");
    }

    @Test
    public void checkFindByLastName() {
        User user1 = new User("Frank", "Gray", "frankgray@example.com", "password123", null);
        User user2 = new User("Gina", "Gray", "ginagray@example.com", "password123", null);
        userService.addUser(user1);
        userService.addUser(user2);
        List<User> users = userService.findByLastName("Gray");
        Assertions.assertTrue(users.contains(user1) && users.contains(user2), "Both users with last name 'Gray' should be found");
    }

    @Test
    public void checkChangeUserPassword() {
        User user = new User("Hank", "White", "hankwhite@example.com", "password123", null);
        userService.addUser(user);
        boolean result = userService.changeUserPassword("hankwhite@example.com", "newPassword123");
        User updatedUser = userService.findByEmail("hankwhite@example.com");
        Assertions.assertTrue(result, "Password change should succeed");
        Assertions.assertEquals("newPassword123", updatedUser.getPassword(), "Password should be updated correctly");
    }
}
