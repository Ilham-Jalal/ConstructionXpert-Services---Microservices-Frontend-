package com.user_management_service;

import com.user_service.controller.AuthenticationController;
import com.user_service.dto.*;
import com.user_service.exception.LoginException;
import com.user_service.exception.RegistrationException;
import com.user_service.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTests {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        AuthenticationRequest request = new AuthenticationRequest("username", "password");
        AuthenticationResponse response = new AuthenticationResponse("jwt-token");
        when(authenticationService.login(request)).thenReturn(response);

        ResponseEntity<?> result = authenticationController.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testLoginFailureInvalidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("username", "wrongpassword");
        when(authenticationService.login(request)).thenThrow(LoginException.invalidCredentials());

        ResponseEntity<?> result = authenticationController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals(LoginException.invalidCredentials().getMessage(), result.getBody());
    }

    @Test
    public void testLoginFailureUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("username", "wrongpassword");
        when(authenticationService.login(request)).thenThrow(LoginException.userNotFound());

        ResponseEntity<?> result = authenticationController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals(LoginException.userNotFound().getMessage(), result.getBody());
    }

    @Test
    public void testLoginFailureAuthenticationFailed() {
        AuthenticationRequest request = new AuthenticationRequest("username", "wrongpassword");
        when(authenticationService.login(request)).thenThrow(LoginException.authenticationFailed());

        ResponseEntity<?> result = authenticationController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals(LoginException.authenticationFailed().getMessage(), result.getBody());
    }

    @Test
    public void testAdminRegisterSuccess() {
        AdminDto adminDto = new AdminDto();
        AuthenticationResponse response = new AuthenticationResponse("jwt-token");
        when(authenticationService.adminRegister(adminDto)).thenReturn(response);

        ResponseEntity<?> result = authenticationController.adminRegister(adminDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testAdminRegisterFailure() {
        AdminDto adminDto = new AdminDto();
        when(authenticationService.adminRegister(adminDto)).thenThrow(new RegistrationException());

        ResponseEntity<?> result = authenticationController.adminRegister(adminDto);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(new RegistrationException().getMessage(), result.getBody());
    }

    @Test
    public void testClientRegisterSuccess() {
        ClientDto clientDto = new ClientDto();
        AuthenticationResponse response = new AuthenticationResponse("jwt-token");
        when(authenticationService.clientRegister(clientDto)).thenReturn(response);

        ResponseEntity<?> result = authenticationController.clientRegister(clientDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testClientRegisterFailure() {
        ClientDto clientDto = new ClientDto();
        when(authenticationService.clientRegister(clientDto)).thenThrow(new RegistrationException());

        ResponseEntity<?> result = authenticationController.clientRegister(clientDto);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(new RegistrationException().getMessage(), result.getBody());
    }

    @Test
    public void testSupervisorRegisterSuccess() {
        SupervisorDto supervisorDto = new SupervisorDto();
        AuthenticationResponse response = new AuthenticationResponse("jwt-token");
        when(authenticationService.supervisorRegister(supervisorDto)).thenReturn(response);

        ResponseEntity<?> result = authenticationController.supervisorRegister(supervisorDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testSupervisorRegisterFailure() {
        SupervisorDto supervisorDto = new SupervisorDto();
        when(authenticationService.supervisorRegister(supervisorDto)).thenThrow(new RegistrationException());

        ResponseEntity<?> result = authenticationController.supervisorRegister(supervisorDto);

        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals(new RegistrationException().getMessage(), result.getBody());
    }
}
