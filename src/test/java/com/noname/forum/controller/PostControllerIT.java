package com.noname.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


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
import com.noname.forum.dto.post.PostResponse;
import com.noname.forum.dto.user.UserCredentials;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.repository.UserRepository;
import com.noname.forum.security.provider.JwtProvider;
import com.noname.forum.service.AuthService;
import com.noname.forum.service.PostService;
import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.fixture.PostRequestFixture;
import com.noname.forum.fixture.UserRequestCreateFixture;

@SpringBootTest 
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerIT extends AbstractPostgresIT{

    @Autowired private AuthService authService;
    @Autowired private MockMvc mockMvc;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private UserRepository userRepository;
    @Autowired private PostService postService;

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
        userResponse = authService.register(userRequestCreate);

        UserCredentials userCredentials = new UserCredentials(
            userResponse.id(), userResponse.username(), userRequestCreate.password());
        accessToken = jwtProvider.generateAccessToken(userCredentials);
    }

    @Test 
    void getAllIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostRequest postRequest2 = PostRequestFixture.defaultPost2();
        postService.create(postRequest, userResponse.id());
        postService.create(postRequest2, userResponse.id());

        mockMvc.perform(get("/posts")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("My first post!"))
            .andExpect(jsonPath("$[0].description").value("Tu tu ru"))
            .andExpect(jsonPath("$[0].author.username").value("ogurchikRick"))
            .andExpect(jsonPath("$[1].title").value("My second post!"))
            .andExpect(jsonPath("$[1].description").value("Lorem orem impale"))
            .andExpect(jsonPath("$[1].author.username").value("ogurchikRick"))
            .andExpect(jsonPath("$").isArray());
    }

    @Test 
    void getIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());

        mockMvc.perform(get("/posts/{id}", postResponse.id())
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("My first post!"))
            .andExpect(jsonPath("$.description").value("Tu tu ru"))
            .andExpect(jsonPath("$.author.username").value("ogurchikRick"));
    }

    @Test
    void whenPostIdDoesNotExistIT() throws Exception{
        mockMvc.perform(get("/posts/{id}", 999L)
                .header("Authorization", "Bearer " + accessToken)) 
            .andExpect(status().isNotFound()); 
    }

    @Test 
    void createIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();

        mockMvc.perform(post("/posts")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("My first post!"))
            .andExpect(jsonPath("$.description").value("Tu tu ru"))
            .andExpect(jsonPath("$.author.id").value(userResponse.id()));
    }

    @Test 
    void IncorrectDataCreateIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.incorrectPost();

        mockMvc.perform(post("/posts")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test 
    void updateIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());

        mockMvc.perform(put("/posts/{id}", postResponse.id())
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("My first post!"))
            .andExpect(jsonPath("$.description").value("Tu tu ru"))
            .andExpect(jsonPath("$.author.id").value(userResponse.id()));
    }

    @Test 
    void IncorrectDataUpdateIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());

        PostRequest incorrectRequest = PostRequestFixture.incorrectPost();

        mockMvc.perform(put("/posts/{id}", postResponse.id())
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incorrectRequest)))
            .andExpect(status().isBadRequest());
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
    void deleteIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());

        mockMvc.perform(delete("/posts/{id}", postResponse.id())
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk());
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
