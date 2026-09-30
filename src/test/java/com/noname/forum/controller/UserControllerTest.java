package com.noname.forum.controller;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.fixture.UserRequestUpdateFixture;
import com.noname.forum.fixture.UserResponseFixture;
import com.noname.forum.service.UserService;

@ExtendWith (MockitoExtension.class)
public class UserControllerTest {
    
    @Mock 
    private UserService userService;

    @InjectMocks 
    private UserController userController;

    @Autowired 
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;


    @BeforeEach 
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test 
    void getAllUserTest() throws Exception {
        List<UserResponse> users = new ArrayList<>();
        users.add(UserResponseFixture.defaultUser());
        users.add(UserResponseFixture.defaultAdmin());

        when(userService.getAllUser()).thenReturn(users);
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[0].username").value("NiAl"))
            .andExpect(jsonPath("$").isArray());
        verify(userService, times(1)).getAllUser();
    }

    @Test 
    void getUserTest() throws Exception {
        UserResponse user = UserResponseFixture.defaultUser();

        when(userService.getUserById(user.id())).thenReturn(user);
        
        mockMvc.perform(get("/users/{id}", user.id()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("NiAl"));

        verify(userService, times(1)).getUserById(user.id());
    }

    @Test 
    void getUserByUsernameTest() throws Exception {
        UserResponse user = UserResponseFixture.defaultUser();

        when(userService.getByUsernameForResponse(user.username())).thenReturn(user);
        
        mockMvc.perform(get("/users/username/{username}", user.username()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("NiAl"));

        verify(userService, times(1)).getByUsernameForResponse(user.username());
    }

    @Test 
    void userUpdate() throws Exception {
        Long userId = 1L;
        UserRequestUpdate inputDTO = UserRequestUpdateFixture.defaultRequestUpdate();
        UserResponse updatedDTO = UserResponseFixture.defaultUser();

        Mockito.when(userService.updateUser(Mockito.eq(userId), Mockito.any(UserRequestUpdate.class)))
            .thenReturn(updatedDTO);

        mockMvc.perform(put("/users/{id}", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("NiAl"));

        verify(userService, times(1)).updateUser(userId, inputDTO);
    }

    @Test 
    void userRemoveTest() throws Exception {
        Long userId = 1L;
        
        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
            .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }
}
