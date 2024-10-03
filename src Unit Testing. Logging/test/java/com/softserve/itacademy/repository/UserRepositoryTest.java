package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test") // This is assuming you might have a test-specific configuration
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // Setup data for each test
        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setEmail("john.doe@example.com");
        newUser.setPassword("password123");
        entityManager.persist(newUser);
        entityManager.flush();
    }

    @Test
    public void testGetUserByEmailWhenExists() {
        // Act
        User found = userRepository.getUserByEmail("john.doe@example.com");

        // Assert
        assertNotNull(found);
        assertEquals("John", found.getFirstName());
    }

    @Test
    public void testGetUserByEmailWhenDoesNotExist() {
        // Act
        User found = userRepository.getUserByEmail("nonexistent@example.com");

        // Assert
        assertNull(found);
    }
}
