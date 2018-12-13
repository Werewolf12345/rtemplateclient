package com.alexeiboriskin.sbtask.rtemplateclient.services;

import com.alexeiboriskin.sbtask.rtemplateclient.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class MyUserDetailsService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserService userService;

    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(oAuth2UserRequest);

        Map<String, Object> attributes = user.getAttributes();
        User userInBb = userService.findByUserName(((String) attributes.get(
                "name")).replaceAll(" ", ""));

        Set<GrantedAuthority> authoritySet;
        if (userInBb == null) {
            authoritySet = new HashSet<>(user.getAuthorities());
            authoritySet.add(new SimpleGrantedAuthority("ROLE_GOOUSER"));
        } else {
            authoritySet = new HashSet<>(userInBb.getAuthorities());
            authoritySet.add(new SimpleGrantedAuthority("ROLE_GOOUSER"));
        }

        String userNameAttributeName =
                oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        final DefaultOAuth2User defaultOAuth2User =
                new DefaultOAuth2User(authoritySet, attributes,
                        userNameAttributeName);
        userService.saveUser(new User(defaultOAuth2User));

        return defaultOAuth2User;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " was not found!");
        }
        return user;
    }
}