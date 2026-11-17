package com.noname.forum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;

import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.exception.PostNotFoundException;
import com.noname.forum.fixture.PostRequestFixture;
import com.noname.forum.fixture.UserFixture;
import com.noname.forum.repository.PostRepository;
import com.noname.forum.repository.UserRepository;


@SpringBootTest 
@ActiveProfiles("test")
public class PostServiceIT extends AbstractPostgresIT{

    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired PostService postService;
    @Autowired UserService userService;
    @Autowired AuthService authService;

    private UserResponse userResponse;
    private UserResponse adminResponse;
    
    @BeforeEach 
    void clean(){
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp(){
        userResponse = UserResponse.from(userRepository.create(UserFixture.defaultUser()));
        adminResponse = UserResponse.from(userRepository.create(UserFixture.defaultAdmin()));
    }

    @Test 
    void getAllPostsIT() throws Exception{
        PostRequest postRequest1 = PostRequestFixture.defaultPost();
        PostRequest postRequest2 = PostRequestFixture.defaultPost();
        PostResponse postResponse1 = postService.create(postRequest1, userResponse.id());
        PostResponse postResponse2 = postService.create(postRequest2, adminResponse.id());


        List<PostResponse> posts =  postService.getAll();
        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertEquals(postResponse1.id(), posts.get(0).id());
        assertEquals(postRequest1.title(), posts.get(0).title());
        assertEquals(postResponse2.id(), posts.get(1).id());
        assertEquals(postRequest2.title(), posts.get(1).title());
    }

    @Test 
    void getPostIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());

        PostResponse post =  postService.get(postResponse.id());
        assertNotNull(post);
        assertEquals(postResponse.id(), post.id());
        assertEquals(postRequest.title(), post.title());
    }

    @Test 
    void PostNotFoundIT() throws Exception{
        PostNotFoundException exception = assertThrows(
            PostNotFoundException.class, 
            () -> postService.get(1L)
        );

        assert(exception.getMessage()).contains(String.valueOf(1L));
    }

    @Test 
    void createPostIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());

        PostResponse post =  postService.get(postResponse.id());
        assertNotNull(post);
        assertEquals(postResponse.id(), post.id());
        assertEquals(postRequest.title(), post.title());
    }

    @Test 
    void updatePostIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());
        PostRequest postRequestToUpdate = PostRequestFixture.defaultPost2();

        PostResponse post =  postService.update(postRequestToUpdate, postResponse.id(), userResponse.id());
        assertNotNull(post);
        assertEquals(postResponse.id(), post.id());
        assertEquals(postRequestToUpdate.title(), post.title());
    }

    @Test 
    void updateThrowPostNotFoundIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostNotFoundException exception = assertThrows(
            PostNotFoundException.class, 
            () -> postService.update(postRequest, 1L, userResponse.id())
        );

        assert(exception.getMessage()).contains(String.valueOf(1L));
    }

    @Test 
    void updateThrowAccessDeniedIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());
        PostRequest postRequestToUpdate = PostRequestFixture.defaultPost2();

        AccessDeniedException exception = assertThrows(
            AccessDeniedException.class, 
            () -> postService.update(postRequestToUpdate, postResponse.id(), userResponse.id() + 1)
        );

        assert(exception.getMessage()).contains("User is not author post");
    }

    @Test 
    void deletePostIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());

        postService.delete(postResponse.id(), userResponse.id());

        PostNotFoundException exception = assertThrows(
            PostNotFoundException.class, 
            () -> postService.get(1L)
        );    

        assert(exception.getMessage()).contains(String.valueOf(1L));
    }

    @Test 
    void deleteThrowPostNotFoundIT() throws Exception{
        PostNotFoundException exception = assertThrows(
            PostNotFoundException.class, 
            () -> postService.delete(1L, userResponse.id())
        );

        assert(exception.getMessage()).contains(String.valueOf(1L));
    }

    @Test 
    void deleteThrowAccessDeniedIT() throws Exception{
        PostRequest postRequest = PostRequestFixture.defaultPost();
        PostResponse postResponse = postService.create(postRequest, userResponse.id());

        AccessDeniedException exception = assertThrows(
            AccessDeniedException.class, 
            () -> postService.delete(postResponse.id(), userResponse.id() + 1)
        );

        assert(exception.getMessage()).contains("User is not author post");
    }
}
