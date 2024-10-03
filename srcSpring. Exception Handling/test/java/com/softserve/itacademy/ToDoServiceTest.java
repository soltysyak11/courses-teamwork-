package com.softserve.itacademy;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.ToDoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.softserve.itacademy.exception.EntityNotFoundException;
import com.softserve.itacademy.repository.ToDoRepository;
import com.softserve.itacademy.service.impl.ToDoServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {

    @Mock
    private ToDoRepository todoRepositoryMock;

    private ToDoService service;

    @BeforeEach
    void setUp() {
        service = new ToDoServiceImpl(todoRepositoryMock);
    }

    @Test
    void createToDoWithNullShouldThrowException() {
        assertThrows(NullEntityReferenceException.class, () -> service.create(null));
    }

    @Test
    void updateToDoWithNullShouldThrowException() {
        assertThrows(NullEntityReferenceException.class, () -> service.update(null));
    }

    @Test
    void readNonExistingToDoShouldThrowException() {
        long id = 999L;
        when(todoRepositoryMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.readById(id));
    }
    @Test
    void updateNonExistingToDoShouldThrowException() {
        ToDo fakeToDo = new ToDo();
        fakeToDo.setId(999L);
        when(todoRepositoryMock.findById(fakeToDo.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(fakeToDo));
    }

}
