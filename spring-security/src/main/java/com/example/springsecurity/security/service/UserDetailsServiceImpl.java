package com.example.springsecurity.security.service;

import com.example.springsecurity.security.entity.JwtUser;
import com.example.springsecurity.system.entity.User;
import com.example.springsecurity.system.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        User user = userService.find(name);
        return new JwtUser(user);
    }
}
