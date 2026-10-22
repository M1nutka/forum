package com.noname.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.user.UserCredentials;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.repository.UserRepository;
import com.noname.forum.security.provider.JwtProvider;
import com.noname.forum.service.SecurityService;

import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.fixture.PostRequestFixture;
import com.noname.forum.fixture.UserRequestCreateFixture;

@SpringBootTest 
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerIT extends AbstractPostgresIT{

    @Autowired private SecurityService securityService;
    @Autowired private MockMvc mockMvc;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private UserRepository userRepository;

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
    void whenPostIdDoesNotExistIT() throws Exception{
        mockMvc.perform(get("/posts/{id}", 999L)
                .header("Authorization", "Bearer " + accessToken)) 
            .andExpect(status().isNotFound()); 
    }

    @Test
    void whenPostIdDoesNotExistUpdateIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();

        mockMvc.perform(put("/posts/{id}", 999L)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))) 
            .andExpect(status().isNotFound()); 
    }

    @Test
    void whenPostIdDoesNotExistDeleteIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();

        mockMvc.perform(delete("/posts/{id}", 999L)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))) 
            .andExpect(status().isNotFound()); 
    }
    
}
