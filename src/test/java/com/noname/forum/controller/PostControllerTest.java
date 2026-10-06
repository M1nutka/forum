package com.noname.forum.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.noname.forum.dto.post.PostResponse;
import com.noname.forum.fixture.PostResponseFixture;
import com.noname.forum.service.PostService;

@ExtendWith (MockitoExtension.class)
public class PostControllerTest {
    
    @Mock 
    private PostService postService;

    @InjectMocks  
    private PostController postController;

    private MockMvc mockMvc;

    @BeforeEach 
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test 
    void getAllPostsTest() throws Exception{
        List<PostResponse> posts = new ArrayList<>();
        posts.add(PostResponseFixture.defaultPostResponse1());
        posts.add(PostResponseFixture.defaultPostResponse2());

        when(postService.getAllPosts()).thenReturn(posts);
        mockMvc.perform(get("/posts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$").isArray());

        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void getPostTest() throws Exception{
        PostResponse post = PostResponseFixture.defaultPostResponse1();

        when(postService.getPost(post.id())).thenReturn(post);
        mockMvc.perform(get("/posts/{id}", post.id()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));

        verify(postService, times(1)).getPost(post.id());
    }

}
