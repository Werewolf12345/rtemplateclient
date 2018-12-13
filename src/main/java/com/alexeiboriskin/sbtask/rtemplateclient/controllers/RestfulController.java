package com.alexeiboriskin.sbtask.rtemplateclient.controllers;

import com.alexeiboriskin.sbtask.rtemplateclient.models.Role;
import com.alexeiboriskin.sbtask.rtemplateclient.models.User;
import com.alexeiboriskin.sbtask.rtemplateclient.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public User user(@AuthenticationPrincipal UserDetails userDetails,
                     OAuth2AuthenticationToken authentication) {
        if(userDetails == null) {
            Map<String, Object> principalDataMap = authentication.getPrincipal().getAttributes();
            return new User((String) principalDataMap.get("name"),
                            (String) principalDataMap.get("given_name"),
                            (String) principalDataMap.get("family_name"),
                            (String) principalDataMap.get("email"),
                            (String) principalDataMap.get("sub"),
                            authentication.getAuthorities().stream()
                                           .map(a -> new Role(a.getAuthority()))
                                           .collect(Collectors.toSet()));
        } else {
            return userService.findByUserName(userDetails.getUsername());
        }
    }
}