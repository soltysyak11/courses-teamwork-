package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.EntityNotFoundException;
import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.repository.StateRepository;
import com.softserve.itacademy.service.StateService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {
    private StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State create(State state) {
        if (state == null) {
            throw new NullEntityReferenceException("State cannot be null");
        }

        return stateRepository.save(state);
    }

    @Override
    public State readById(long id) {
        Optional<State> optional = stateRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("State with id " + id + " not found");
        }
        return optional.get();
    }

    @Override
    public State update(State state) {
        if (state == null) {
            throw new NullEntityReferenceException("State cannot be null");
        }
        if (!stateRepository.existsById(state.getId())) {
            throw new EntityNotFoundException("State with id " + state.getId() + " not found");
        }
        return stateRepository.save(state);
    }

    @Override
    public void delete(long id) {
        if (!stateRepository.existsById(id)) {
            throw new EntityNotFoundException("State with id " + id + " not found");
        }
        stateRepository.deleteById(id);
    }


    @Override
    public State getByName(String name) {
        Optional<State> optional = Optional.ofNullable(stateRepository.getByName(name));
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("State with name " + name + " not found");
        }
        return optional.get();
    }


    @Override
    public List<State> getAll() {
        List<State> states = stateRepository.getAll();
        return states.isEmpty() ? new ArrayList<>() : states;
    }
}