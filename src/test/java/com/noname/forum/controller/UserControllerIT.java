package com.noname.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.dto.jwt.JwtRequest;
import com.noname.forum.dto.user.UserCredentials;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.repository.UserRepository;
import com.noname.forum.security.provider.JwtProvider;
import com.noname.forum.service.SecurityService;

import com.noname.forum.fixture.JwtRequestFixture;
import com.noname.forum.fixture.UserRequestCreateFixture;
import com.noname.forum.fixture.UserRequestUpdateFixture;
import com.noname.forum.fixture.UserResponseFixture;

@SpringBootTest 
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIT extends AbstractPostgresIT{

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private SecurityService securityService;
    @Autowired private JwtProvider jwtProvider;

    private ObjectMapper objectMapper;

    private String accessToken;
    private UserResponse userResponse;

    @BeforeEach 
    void clean(){
        userRepository.deleteAll();
    }

    @BeforeEach 
    void setUp(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserRequestCreate userRequestCreate2 = UserRequestCreateFixture.defaultRequestCreate2();
        userResponse = securityService.register(userRequestCreate);
        securityService.register(userRequestCreate2);

        UserCredentials userCredentials = new UserCredentials(
            userResponse.id(), userResponse.username(), userRequestCreate.password());

        accessToken = jwtProvider.generateAccessToken(userCredentials);
    }

    @Test 
    void getAllUserIT() throws Exception{
        mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].username").value("ogurchikRick"))
            .andExpect(jsonPath("$[1].username").value("mortyyy"))
            .andExpect(jsonPath("$").isArray());
    }

    @Test 
    void getUserIT() throws Exception{

        mockMvc.perform(get("/users/{id}", userResponse.id())
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("ogurchikRick"))
            .andReturn();
    }

    @Test 
    void whenUserIdDoesNotExistIT() throws Exception {
        UserResponse user = UserResponseFixture.defaultUser();
        
        mockMvc.perform(get("/users/{id}", user.id())
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNotFound());
    }

    @Test 
    void whenyUsernameDoesNotExistIT() throws Exception {
        UserResponse user = UserResponseFixture.defaultUser();
        
        mockMvc.perform(get("/users/username/{username}", user.username())
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNotFound());
    }

    @Test 
    void shouldCreateAndRetriveUserIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate3();

        MvcResult result = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestCreate)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Yogo"))
            .andExpect(jsonPath("$.password").doesNotExist())
            .andReturn();

        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest2();

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jwtRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.refreshToken").isNotEmpty())
            .andReturn();

        Integer userId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        String accessToken = JsonPath.read(loginResult.getResponse().getContentAsString(), "$.accessToken");
    
        mockMvc.perform(get("/users/{id}", userId)
            .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yogo"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn();
    }

    @Test 
    void updateUserIT() throws Exception{
        UserRequestUpdate userRequestUpdate = UserRequestUpdateFixture.defaultRequestUpdate();

        mockMvc.perform(put("/users")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestUpdate)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Nill"))
            .andReturn();
    }

    @Test 
    void deleteUserIT() throws Exception{
        mockMvc.perform(delete("/users")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
    }
}
