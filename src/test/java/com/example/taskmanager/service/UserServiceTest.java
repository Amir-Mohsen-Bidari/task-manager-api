package com.example.taskmanager.service;

import com.example.taskmanager.dto.AuthResponse;
import com.example.taskmanager.dto.LoginRequest;
import com.example.taskmanager.dto.RegisterRequest;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JwtService;
import com.example.taskmanager.service.impl.UserServiceImpl;

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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Mock
    private JwtService jwtService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // given
        String email = "john@example.com";
        String rawPassword = "password123";
        String hashedPassword = "encodedPassword";
        String name = "john";
        RegisterRequest request = new RegisterRequest(email, name, rawPassword);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn(hashedPassword);
        when(jwtService.generateToken(email)).thenReturn("mock-jwt-token");

        // when
        AuthResponse response = userService.register(request);

        // then
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("john", savedUser.getName());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.token());
        verify(jwtService).generateToken(email);
        verifyNoMoreInteractions(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void shouldLoginUserSuccessfully() {
        // given
        String email = "john@example.com";
        String rawPassword = "password123";
        String hashedPassword = "hashedPassword";

        LoginRequest request = new LoginRequest(email, rawPassword);

        User user = new User(email, "John", hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);
        when(jwtService.generateToken(email)).thenReturn("mock-jwt-token");

        // when
        AuthResponse response = userService.login(request);

        // then
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.token());
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword, hashedPassword);
        verify(jwtService).generateToken(email);
        verifyNoMoreInteractions(userRepository, passwordEncoder, jwtService);
    }

}
