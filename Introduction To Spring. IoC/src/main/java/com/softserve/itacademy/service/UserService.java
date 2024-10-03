package com.softserve.itacademy.service;

import java.util.List;

import com.softserve.itacademy.model.User;

public interface UserService {
    
    User addUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    List<User> getAll();

    User findByEmail(String email);

    User getUser(String email);

    List<User> findByLastName(String lastName);

    boolean changeUserPassword(String email, String newPassword);
}
