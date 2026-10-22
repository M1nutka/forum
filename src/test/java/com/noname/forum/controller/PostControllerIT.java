package com.noname.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.dto.user.UserCredentials;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.fixture.UserRequestCreateFixture;
import com.noname.forum.repository.UserRepository;
import com.noname.forum.security.provider.JwtProvider;
import com.noname.forum.service.SecurityService;

@SpringBootTest 
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerIT extends AbstractPostgresIT{

    @Autowired private SecurityService securityService;
    @Autowired private MockMvc mockMvc;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private UserRepository userRepository;

    private String accessToken;
    
    @BeforeEach 
    void clean(){
        userRepository.deleteAll();
    }

    @BeforeEach 
    void setUp(){
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = securityService.register(userRequestCreate);

        UserCredentials userCredentials = new UserCredentials(
            userResponse.id(), userResponse.username(), userRequestCreate.password());

        accessToken = jwtProvider.generateAccessToken(userCredentials);
    }

    @Test
    void whenPostIdDoesNotExistTest() throws Exception{
        mockMvc.perform(get("/posts/{id}", 999L)
                .header("Authorization", "Bearer " + accessToken)) 
            .andExpect(status().isNotFound()); 
    }
    
}
