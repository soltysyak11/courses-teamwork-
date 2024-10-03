package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.model.State;
import com.softserve.itacademy.repository.StateRepository;
import com.softserve.itacademy.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    @Autowired
    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State create(State state) {
        return stateRepository.save(state);
    }

    @Override
    public State readById(long id) {
        return stateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("State not found with id " + id));
    }

    @Override
    public State update(State state) {
        if (state.getId() == 0 || !stateRepository.existsById(state.getId())) {
            throw new EntityNotFoundException("State not found with id " + state.getId());
        }
        return stateRepository.save(state);
    }

    @Override
    public void delete(long id) {
        stateRepository.deleteById(id);
    }

    @Override
    public List<State> getAll() {
        return stateRepository.findAll();
    }

    @Override
    public State getByName(String name) {
        return stateRepository.findByName(name);
    }

    @Override
    public List<State> getSortAsc() {
        return stateRepository.findAllByOrderByNameAsc();
    }
}
