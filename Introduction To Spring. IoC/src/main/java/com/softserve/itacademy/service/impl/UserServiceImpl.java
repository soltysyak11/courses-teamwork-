package com.softserve.itacademy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final List<User> users = new ArrayList<>();

    @Override
    public User addUser(User user) {
        if (user == null || getUser(user.getEmail()) != null) {
            return null; // User is null or email already exists
        }
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        User foundUser = getUser(user.getEmail());
        if (foundUser != null) {
            foundUser.setFirstName(user.getFirstName());
            foundUser.setLastName(user.getLastName());
            foundUser.setPassword(user.getPassword());
            return foundUser;
        }
        return null;
    }

    @Override
    public void deleteUser(User user) {
        users.removeIf(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User findByEmail(String email) {
        return getUser(email);
    }

    @Override
    public User getUser(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findByLastName(String lastName) {
        List<User> matchingUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getLastName().equalsIgnoreCase(lastName)) {
                matchingUsers.add(user);
            }
        }
        return matchingUsers;
    }

    @Override
    public boolean changeUserPassword(String email, String newPassword) {
        User user = findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
