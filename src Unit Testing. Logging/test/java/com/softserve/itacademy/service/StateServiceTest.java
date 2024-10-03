package com.softserve.itacademy.service;

import com.softserve.itacademy.model.State;
import com.softserve.itacademy.repository.StateRepository;
import com.softserve.itacademy.service.impl.StateServiceImpl;
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
public class StateServiceTest {
    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private StateServiceImpl stateService;

    private static State state;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init() {
        state = new State();
        state.setId(1L);
        state.setName("State Name");
    }

    @Test
    public void createValidStateTest() {
        stateService.create(state);
        Mockito.verify(stateRepository, Mockito.times(1)).save(state);
    }

    @Test
    public void testReadById() {
        long id = 1L;
        Mockito.when(stateRepository.findById(id)).thenReturn(Optional.of(state));
        State result = stateService.readById(id);
        Assertions.assertEquals(state, result);
    }

    @Test
    public void updateValidStateTest() {
        Mockito.when(stateRepository.findById(state.getId())).thenReturn(Optional.of(state));
        stateService.update(state);
        Mockito.verify(stateRepository, Mockito.times(1)).save(state);
    }

    @Test
    public void deleteExistingStateTest() {
        Mockito.when(stateRepository.findById(state.getId())).thenReturn(Optional.of(state));
        stateService.delete(state.getId());
        Mockito.verify(stateRepository, Mockito.times(1)).delete(state);
    }

    @Test
    public void deleteNotExistingStateTest() {
        Mockito.when(stateRepository.findById(state.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> stateService.delete(state.getId()));
    }

    @Test
    public void getByNameExistingStateTest() {
        String name = "State Name";
        Mockito.when(stateRepository.getByName(name)).thenReturn(state);
        State result = stateService.getByName(name);
        Assertions.assertEquals(state, result);
    }

    @Test
    public void getByNameNotExistingStateTest() {
        String name = "Non-existing State Name";
        Mockito.when(stateRepository.getByName(name)).thenReturn(null);
        Assertions.assertThrows(EntityNotFoundException.class, () -> stateService.getByName(name));
    }

    @Test
    public void getAllStatesTest() {
        List<State> stateList = Collections.singletonList(state);
        Mockito.when(stateRepository.getAll()).thenReturn(stateList);
        Assertions.assertEquals(stateService.getAll(), stateList);
    }
}
