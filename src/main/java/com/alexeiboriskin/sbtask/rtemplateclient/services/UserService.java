package com.alexeiboriskin.sbtask.rtemplateclient.services;

import com.alexeiboriskin.sbtask.rtemplateclient.models.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    List<User> listAllUsers();

    void deleteUser(Long id);

    User findByUserName(String username);
}
