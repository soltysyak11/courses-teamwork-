package com.softserve.itacademy.repository;

import com.softserve.itacademy.model.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    public void newStateTest() {
        State state = new State();
        state.setName("Test");
        stateRepository.save(state);
        State actual = stateRepository.getByName("Test");
        Assertions.assertEquals("Test", actual.getName());
    }

    @Test
    public void getAll() {
        List<State> actual = stateRepository.getAll();
        int excepted = 4;
        Assertions.assertEquals(excepted, actual.size());
    }
}
