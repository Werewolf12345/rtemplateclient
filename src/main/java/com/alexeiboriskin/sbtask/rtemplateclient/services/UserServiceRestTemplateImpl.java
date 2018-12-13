package com.alexeiboriskin.sbtask.rtemplateclient.services;

import com.alexeiboriskin.sbtask.rtemplateclient.models.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceRestTemplateImpl implements UserService {

    private static final String USER = "admin";
    private static final String PASSWORD = "admin";
    private static final String URL = "http://localhost:8081/users/";

    private final RestTemplate restTemplate;

    public UserServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(USER, PASSWORD)
                .build();
    }

    @Override
    public User saveUser(User user) {
        User userInDb = findByUserName(user.getUsername());

        if (userInDb != null) {
            user.setId(userInDb.getId());
            restTemplate.put(URL + "{id}", user, user.getId());
            return restTemplate.getForObject(URL + "{id}", User.class,
                    user.getId());
        } else {
            return restTemplate.postForObject(URL, user, User.class);
        }
    }

    @Override
    public User updateUser(User user) {
        restTemplate.put(URL + "{id}", user, user.getId());
        return restTemplate.getForObject(URL + "{id}", User.class, user.getId());
    }

    @Override
    public User getUserById(Long id) {
        return restTemplate.getForObject(URL + "{id}", User.class, id);
    }

    @Override
    public List<User> listAllUsers() {
        User[] usersArray = restTemplate.getForObject(URL, User[].class);

        if (usersArray != null) {
            return  Arrays.asList(usersArray);
        }

        return Collections.emptyList();
    }

    @Override
    public void deleteUser(Long id) {
        restTemplate.delete(URL + "{id}", id);
    }

    @Override
    public User findByUserName(String username) {
        return restTemplate.getForObject(URL + "find?username={username}", User.class, username);
    }
}
