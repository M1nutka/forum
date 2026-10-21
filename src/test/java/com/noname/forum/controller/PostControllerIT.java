package com.noname.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.dto.user.UserCredentials;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.fixture.UserRequestCreateFixture;
import com.noname.forum.security.provider.JwtProvider;
import com.noname.forum.service.PostService;
import com.noname.forum.service.SecurityService;

@SpringBootTest 
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerIT extends AbstractPostgresIT{

    @Autowired SecurityService securityService;
    @Autowired PostService postService;
    @Autowired MockMvc mockMvc;
    @Autowired JwtProvider jwtProvider;
    
    
    @Test
    void whenPostIdDoesNotExistTest() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        Long userId = 1L;

        securityService.register(userRequestCreate);
        
        UserCredentials userCredentials = new UserCredentials(
            userId, userRequestCreate.username(), userRequestCreate.password());
        String token = jwtProvider.generateAccessToken(userCredentials);

        mockMvc.perform(get("/posts/{id}", 999L)
                .header("Authorization", "Bearer " + token)) 
            .andExpect(status().isNotFound()); 
    }
    
}
