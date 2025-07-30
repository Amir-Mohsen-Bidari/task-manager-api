package com.example.taskmanager.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.taskmanager.dto.AuthResponse;
import com.example.taskmanager.dto.RegisterRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        String hashed = passwordEncoder.encode(request.password());

        User user = new User(request.email(), request.name(), hashed);
        userRepository.save(user);

        // Placeholder token
        return new AuthResponse("mock-jwt-token");
    }

}