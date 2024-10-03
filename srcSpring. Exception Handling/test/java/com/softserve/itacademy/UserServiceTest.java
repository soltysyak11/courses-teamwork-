package com.softserve.itacademy;

import com.softserve.itacademy.exception.EntityNotFoundException;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void readNonExistingUserShouldThrowException() {
        long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.readById(id));
    }

    @Test
    void updateNonExistingUserShouldThrowException() {
        User user = new User();
        user.setId(999L);
        when(userRepository.existsById(user.getId())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> userService.update(user));
    }

    @Test
    void deleteNonExistingUserShouldThrowException() {
        long id = 999L;
        when(userRepository.existsById(id)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> userService.delete(id));
    }

    @Test
    void createShouldNotThrowException() {
        User user = new User();
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);
        assertDoesNotThrow(() -> userService.create(user));
        verify(userRepository).save(user);
    }

    @Test
    void readExistingUserShouldNotThrowException() {
        long id = 1L;
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.readById(id));
        User foundUser = userService.readById(id);
        assertNotNull(foundUser);
        assertEquals(foundUser.getId(), id);
    }
}
