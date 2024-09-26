package com.Naveen.security_service.controller;

import com.Naveen.security_service.dto.AuthRequest;
import com.Naveen.security_service.entity.UserCredential;
import com.Naveen.security_service.exception.InvalidTokenException;
import com.Naveen.security_service.exception.InvalidUserDetails;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewUser_Success() {
        UserCredential userCredential = new UserCredential();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(authService.saveUser(userCredential)).thenReturn("User registered successfully");

        String response = authController.addNewUser(userCredential, bindingResult);
        assertEquals("User registered successfully", response);
    }

    @Test
    void testAddNewUser_ValidationError() {
        UserCredential userCredential = new UserCredential();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userCredential", "name", "Name is required"),
                new FieldError("userCredential", "email", "Email format is invalid")
        ));

        InvalidUserDetails exception = assertThrows(InvalidUserDetails.class, () -> {
            authController.addNewUser(userCredential, bindingResult);
        });

        assertTrue(exception.getMessage().contains("Validation errors:"));
    }


    @Test
    void testGenerateUser_Success() {
        AuthRequest authRequest = new AuthRequest("username", "password");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authService.generateToken("username")).thenReturn("generatedToken");

        String token = authController.generateUser(authRequest);
        assertEquals("generatedToken", token);
    }

    @Test
    void testGenerateUser_InvalidCredentials() {
        AuthRequest authRequest = new AuthRequest("username", "wrongPassword");

        when(authenticationManager.authenticate(any())).thenThrow(new InvalidUserException("Invalid User Credentials"));

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            authController.generateUser(authRequest);
        });

        assertEquals("Invalid User Credentials", exception.getMessage());
    }

    @Test
    void testValidateToken_Valid() {
        String token = "validToken";

        doNothing().when(authService).validateToken(token);

        String response = authController.validateToken(token);
        assertEquals("Valid User", response);
    }

    @Test
    void testValidateToken_Invalid() {
        String token = "invalidToken";

        doThrow(new InvalidTokenException("Invalid Token")).when(authService).validateToken(token);

        InvalidTokenException exception = assertThrows(InvalidTokenException.class, () -> {
            authController.validateToken(token);
        });

        assertEquals("Invalid Token", exception.getMessage());
    }
    @Test
    void testAddNewUser_MissingField() {
        UserCredential userCredential = new UserCredential();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("userCredential", "password", "Password is required")
        ));

        InvalidUserDetails exception = assertThrows(InvalidUserDetails.class, () -> {
            authController.addNewUser(userCredential, bindingResult);
        });

        assertTrue(exception.getMessage().contains("Validation errors:"));
    }

    @Test
    void testGenerateUser_InvalidAuthentication() {

        AuthRequest authRequest = new AuthRequest("user", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            authController.generateUser(authRequest);
        });

        assertEquals("Invalid User Credentials", exception.getMessage());
    }
    @Test
    void testGenerateUser_UnexpectedException() {
        AuthRequest authRequest = new AuthRequest("username", "password");
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Unexpected error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.generateUser(authRequest);
        });

        assertEquals("Unexpected error", exception.getMessage());
    }

}
