package com.noname.forum.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.fixture.UserRequestCreateFixture;
import com.noname.forum.fixture.UserResponseFixture;
import com.noname.forum.service.AuthService;

@ExtendWith (MockitoExtension.class)
public class AuthControllerTest {
    
    @Mock 
    private AuthService authService;

    @InjectMocks AuthController authController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach 
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test 
    void registerTest() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = UserResponseFixture.defaultUser2();
        
        when(authService.register(userRequestCreate)).thenReturn(userResponse);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestCreate)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.username").value("ogurchikRick"));

        verify(authService, times(1)).register(userRequestCreate);
    }

    @Test 
    void loginTest() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = UserResponseFixture.defaultUser2();
        
        when(authService.register(userRequestCreate)).thenReturn(userResponse);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestCreate)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.username").value("ogurchikRick"));

        verify(authService, times(1)).register(userRequestCreate);
    }
}
