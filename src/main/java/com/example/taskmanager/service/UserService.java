package com.example.taskmanager.service;

import com.example.taskmanager.dto.AuthResponse;
import com.example.taskmanager.dto.RegisterRequest;

public interface UserService {
    AuthResponse register(RegisterRequest request);
}
