package com.alexeiboriskin.sbtask.rtemplateclient.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/signin", "/css/**", "/images/**", "/js/**", "/webjars/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/signin")
                .defaultSuccessUrl("/adminpanel", true)
                .and()
                .csrf().disable()
                .logout()
                .logoutSuccessUrl("/signin")
                .logoutUrl("/signout");
    }
}