package com.Naveen.security_service.controller;
import com.Naveen.security_service.dto.AuthRequest;
import com.Naveen.security_service.entity.UserCredential;
import com.Naveen.security_service.exception.InvalidUserException;
import com.Naveen.security_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewUser() {
        UserCredential userCredential = new UserCredential();
        userCredential.setName("testUser");
        userCredential.setPassword("testPass");

        when(authService.saveUser(userCredential)).thenReturn("User registered successfully");

        String response = authController.addNewUser(userCredential);
        assertEquals("User registered successfully", response);
        verify(authService, times(1)).saveUser(userCredential);
    }

    @Test
    public void testGenerateUser_Success() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authService.generateToken(authRequest.getUsername())).thenReturn("generatedToken");

        String token = authController.generateUser(authRequest);
        assertEquals("generatedToken", token);
        verify(authService, times(1)).generateToken(authRequest.getUsername());
    }

    @Test
    public void testGenerateUser_Unauthorized() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        Exception exception = assertThrows(InvalidUserException.class, () -> {
            authController.generateUser(authRequest);
        });

        assertEquals("UnAuthorized User", exception.getMessage());
        verify(authService, times(0)).generateToken(anyString());
    }

    @Test
    public void testValidateToken() {
        String token = "validToken";
        doNothing().when(authService).validateToken(token);

        String response = authController.validateToken(token);
        assertEquals("Valid User", response);
        verify(authService, times(1)).validateToken(token);
    }
}
