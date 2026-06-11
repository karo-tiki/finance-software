package com.finance.project.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/persons").permitAll()
                .antMatchers(HttpMethod.GET, "/persons/**").permitAll()
                .antMatchers(HttpMethod.GET, "/groups/**").permitAll()
                .antMatchers(HttpMethod.POST, "/groups/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .headers().frameOptions().disable();
    }
}
