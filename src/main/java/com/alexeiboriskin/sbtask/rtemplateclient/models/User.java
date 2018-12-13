package com.alexeiboriskin.sbtask.rtemplateclient.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class User implements UserDetails {

    public static final PasswordEncoder PASSWORD_ENCODER =
            new BCryptPasswordEncoder();

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private boolean passEncoded;
    private String password;
    private Set<Role> roles;

    public User(String username, String firstName, String lastName,
                String email, String password, Set<Role> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        setPassword(password);
        this.roles = roles;
    }

    public  User(OAuth2User oAuth2User) {
        Map<String, Object> oAuth2UserAttributes = oAuth2User.getAttributes();

        this.username = ((String) oAuth2UserAttributes.get("name")).replaceAll(" ", "");
        this.firstName = (String) oAuth2UserAttributes.get("given_name");
        this.lastName = (String) oAuth2UserAttributes.get("family_name");
        this.email = (String) oAuth2UserAttributes.get("email");
        setPassword((String) oAuth2UserAttributes.get("sub"));
        this.roles = oAuth2User.getAuthorities().stream()
                        .map(a -> new Role(a.getAuthority()))
                        .collect(Collectors.toSet());
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPassEncoded() {
        return passEncoded;
    }

    public void setPassEncoded(boolean passEncoded) {
        this.passEncoded = passEncoded;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
            this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void encryptAndSetPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
