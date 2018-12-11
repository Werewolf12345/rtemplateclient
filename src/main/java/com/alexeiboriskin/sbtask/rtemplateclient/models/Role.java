package com.alexeiboriskin.sbtask.rtemplateclient.models;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("unused")
public class Role implements GrantedAuthority {

    private long id;
    private String role;

    public Role(String role) {
        this.role = role;
    }

    public Role() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}