package com.catalisa.controllers;

import com.catalisa.taxes_api.controllers.AuthenticationController;
import com.catalisa.taxes_api.dtos.LoginDto;
import com.catalisa.taxes_api.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void shouldReturnTokenWhenLoginIsSuccessful() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("password123");

        String expectedToken = "mocked-jwt-token";

        when(authService.login(loginDto)).thenReturn(expectedToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String loginDtoJson = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(expectedToken));
    }

    @Test
    void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("invaliduser");
        loginDto.setPassword("wrongpassword");

        when(authService.login(loginDto)).thenThrow(new BadCredentialsException("Invalid credentials"));

        ObjectMapper objectMapper = new ObjectMapper();
        String loginDtoJson = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void shouldReturnBadRequestWhenUsernameIsMissing() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String loginDtoJson = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsMissing() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testuser");

        ObjectMapper objectMapper = new ObjectMapper();
        String loginDtoJson = objectMapper.writeValueAsString(loginDto);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginDtoJson))
                .andExpect(status().isBadRequest());
    }
}