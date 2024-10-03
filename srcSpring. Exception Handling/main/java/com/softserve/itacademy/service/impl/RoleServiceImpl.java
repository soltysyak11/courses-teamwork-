package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.EntityNotFoundException;
import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.repository.RoleRepository;
import com.softserve.itacademy.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {
        if (role == null) {
            throw new NullEntityReferenceException("Role cannot be null");
        }

        return roleRepository.save(role);
    }

    @Override
    public Role readById(long id) {
        Optional<Role> optional = roleRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Role with id " + id + " not found");
        }
        return optional.get();
    }

    @Override
    public Role update(Role role) {
        if (role == null) {
            throw new NullEntityReferenceException("Role cannot be null");
        }
        if (!roleRepository.existsById(role.getId())) {
            throw new EntityNotFoundException("Role with id " + role.getId() + " not found");
        }
        return roleRepository.save(role);
    }

    @Override
    public void delete(long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role with id " + id + " not found");
        }
        roleRepository.deleteById(id);
    }
    @Override
    public List<Role> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.isEmpty() ? new ArrayList<>() : roles;
    }
}