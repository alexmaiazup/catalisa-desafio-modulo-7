package com.catalisa.controllers;

import com.catalisa.taxes_api.controllers.UserController;
import com.catalisa.taxes_api.dtos.UserRegisterDto;
import com.catalisa.taxes_api.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUsername("testuser");
        userRegisterDto.setPassword("password123");

        ObjectMapper objectMapper = new ObjectMapper();
        String userRegisterDtoJson = objectMapper.writeValueAsString(userRegisterDto);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRegisterDtoJson))
                .andExpect(status().isOk());

        verify(userService).registerUser(userRegisterDto);
    }

    @Test
    void shouldReturnBadRequestWhenUserDataIsInvalid() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        ObjectMapper objectMapper = new ObjectMapper();
        String userRegisterDtoJson = objectMapper.writeValueAsString(userRegisterDto);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRegisterDtoJson))
                .andExpect(status().isBadRequest());
    }

}