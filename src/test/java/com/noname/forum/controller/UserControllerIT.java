package com.noname.forum.controller;

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
import com.noname.forum.dto.jwt.JwtRequest;
import com.noname.forum.dto.user.UserCredentials;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.fixture.JwtRequestFixture;
import com.noname.forum.fixture.UserRequestCreateFixture;
import com.noname.forum.fixture.UserRequestUpdateFixture;
import com.noname.forum.fixture.UserResponseFixture;
import com.noname.forum.repository.UserRepository;
import com.noname.forum.security.provider.JwtProvider;
import com.noname.forum.service.SecurityService;

@SpringBootTest 
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private SecurityService securityService;
    @Autowired private JwtProvider jwtProvider;

    private ObjectMapper objectMapper;

    private String accessToken;

    @BeforeEach 
    void clean(){
        userRepository.deleteAll();
    }

    @BeforeEach 
    void setUp(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = securityService.register(userRequestCreate);

        UserCredentials userCredentials = new UserCredentials(
            userResponse.id(), userResponse.username(), userRequestCreate.password());

        accessToken = jwtProvider.generateAccessToken(userCredentials);
    }

    @Test 
    void whenUserIdDoesNotExistTest() throws Exception {
        UserResponse user = UserResponseFixture.defaultUser();
        
        mockMvc.perform(get("/users/{id}", user.id())
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNotFound());
    }

    @Test 
    void whenyUsernameDoesNotExistTest() throws Exception {
        UserResponse user = UserResponseFixture.defaultUser();
        
        mockMvc.perform(get("/users/username/{username}", user.username())
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isNotFound());
    }

    @Test 
    void whenUserIdDoesNotExistUpdateTest() throws Exception {
        Long userId = 100L;
        UserRequestUpdate inputDTO = UserRequestUpdateFixture.defaultRequestUpdate();

        mockMvc.perform(put("/users/{id}", userId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isNotFound());
    }

    @Test 
    void shouldCreateAndRetriveUser() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate2();

        MvcResult result = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestCreate)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Morty"))
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
                .andExpect(jsonPath("$.name").value("Morty"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn();
    }
}
