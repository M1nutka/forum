package com.noname.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.dto.jwt.JwtRequest;
import com.noname.forum.dto.jwt.JwtResponse;
import com.noname.forum.dto.jwt.RefreshJwtRequest;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.fixture.JwtRequestFixture;
import com.noname.forum.fixture.UserRequestCreateFixture;
import com.noname.forum.repository.UserRepository;
import com.noname.forum.service.AuthService;
import com.noname.forum.service.UserService;

@SpringBootTest 
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIT extends AbstractPostgresIT{
    
    @Autowired private AuthService authService;
    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;

    private ObjectMapper objectMapper;

    private RefreshJwtRequest refreshJwtRequest;
    private UserResponse user;
    
    @BeforeEach 
    void clean(){
        userRepository.deleteAll();
    }

    @BeforeEach 
    void setUp(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        user = authService.register(userRequestCreate);

        JwtResponse jwtResponse = authService.login(JwtRequestFixture.defultJwtRequest());

        refreshJwtRequest = new RefreshJwtRequest(jwtResponse.getRefreshToken());
    }

    @Test 
    void registerIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate2();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestCreate)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("mortyyy"))
            .andExpect(jsonPath("$.name").value("Morty"));
    }

    @Test 
    void IncorrectDataRegisterIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.incorrectRequestCreate();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestCreate)))
            .andExpect(status().isBadRequest());
    }

    @Test 
    void loginIT() throws Exception{
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test 
    void IncorrectDataLoginIT() throws Exception{
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest2();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest)))
            .andExpect(status().isNotFound());
    }

    @Test 
    void IncorrectPasswordLoginIT() throws Exception{
        JwtRequest jwtRequest = JwtRequestFixture.PasswordIncorrectJwtRequest();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest)))
            .andExpect(status().isUnauthorized());
    }

    @Test 
    void tokenIT() throws Exception{
        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshJwtRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test 
    void UnvalidTokenIT() throws Exception{
        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshJwtRequest + "1")))
            .andExpect(status().isUnauthorized());
    }

    @Test 
    void UserDeletedTokenIT() throws Exception{
        userService.delete(user.id());

        mockMvc.perform(post("/api/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshJwtRequest)))
            .andExpect(status().isNotFound());
    }

    @Test 
    void refreshIT() throws Exception{
        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshJwtRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test 
    void UnvalidRefreshIT() throws Exception{
        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshJwtRequest + "1")))
            .andExpect(status().isUnauthorized());
    }

    @Test 
    void UserDeletedRefreshIT() throws Exception{
        userService.delete(user.id());

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshJwtRequest)))
            .andExpect(status().isNotFound());
    }
    
}
