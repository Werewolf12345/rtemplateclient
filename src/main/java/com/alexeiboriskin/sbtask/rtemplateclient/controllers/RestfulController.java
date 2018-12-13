package com.alexeiboriskin.sbtask.rtemplateclient.controllers;

import com.alexeiboriskin.sbtask.rtemplateclient.models.User;
import com.alexeiboriskin.sbtask.rtemplateclient.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class RestfulController {

    private final UserService userService;

    public RestfulController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
    public List<User> getAllUsers() {
        return userService.listAllUsers();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public User getUserById(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public User postUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public User putUser(@PathVariable("id") long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public void deleteUserById(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }

    @GetMapping(value = "/logged", produces = "application/json")
    public User user(@AuthenticationPrincipal UserDetails basicAuthUser,
                     @AuthenticationPrincipal OAuth2User oAuth2User) {
        if(oAuth2User != null) {
            User userDetails = new User(oAuth2User);
            User userInDb = userService.findByUserName(userDetails.getUsername());
            userDetails.setId(userInDb.getId());
            return  userDetails;
        } else {
            return userService.findByUserName(basicAuthUser.getUsername());
        }
    }
}