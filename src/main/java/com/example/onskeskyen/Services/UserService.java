package com.example.onskeskyen.Services;

import com.example.onskeskyen.Models.User;
import com.example.onskeskyen.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@org.springframework.stereotype.Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email).orElseThrow();
    }
}
