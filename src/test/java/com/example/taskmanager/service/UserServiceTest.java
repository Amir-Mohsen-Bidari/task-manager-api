package com.example.taskmanager.service;

import com.example.taskmanager.dto.AuthResponse;
import com.example.taskmanager.dto.RegisterRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.impl.UserServiceImp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImp userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void shouldRegisterUserSuccessfully() {
        // given
        RegisterRequest request = new RegisterRequest("john@example.com", "john", "password");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        // when
        AuthResponse response = userService.register(request);

        // then
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("john", savedUser.getName());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertNotNull(response.token()); // Will implement JWT later
    }

}
