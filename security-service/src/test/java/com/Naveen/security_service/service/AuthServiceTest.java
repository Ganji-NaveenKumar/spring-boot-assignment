package com.Naveen.security_service.service;

import com.Naveen.security_service.entity.UserCredential;
import com.Naveen.security_service.repository.UserCredentialRepository;
import com.Naveen.security_service.utils.JwtService;
import com.Naveen.security_service.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserCredentialRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        UserCredential userCredential = new UserCredential();
        userCredential.setName("testUser");
        userCredential.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        String response = authService.saveUser(userCredential);

        assertEquals(Constant.USER_SAVED, response);
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(repository, times(1)).save(userCredential);
        assertEquals("encodedPassword", userCredential.getPassword());
    }

    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String expectedToken = "generatedToken";

        when(jwtService.generateToken(username)).thenReturn(expectedToken);

        String token = authService.generateToken(username);

        assertEquals(expectedToken, token);
        verify(jwtService, times(1)).generateToken(username);
    }

    @Test
    public void testValidateToken() {
        String token = "validToken";

        doNothing().when(jwtService).validateToken(token);

        authService.validateToken(token);

        verify(jwtService, times(1)).validateToken(token);
    }
}
