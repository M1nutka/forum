package com.noname.forum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import com.noname.forum.domain.Post;
import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;
import com.noname.forum.exception.PostNotFoundException;
import com.noname.forum.fixture.PostFixture;
import com.noname.forum.fixture.PostRequestFixture;
import com.noname.forum.repository.PostRepository;
@ExtendWith (MockitoExtension.class)
public class PosrServiceTest {
    
    @Mock 
    private PostRepository postRepository;

    @InjectMocks  
    private PostService postService;

    @Test
    void getAllPostsTest() throws Exception{
        List<Post> posts = new ArrayList<>();
        posts.add(PostFixture.defaultPost1());
        posts.add(PostFixture.defaultPost2());

        when(postRepository.findAllPosts()).thenReturn(posts);

        List<PostResponse> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("NiAl", result.get(0).author().username());
        assertEquals(2L, result.get(1).id());
        assertEquals("admin", result.get(1).author().username());

        verify(postRepository, times(1)).findAllPosts();
    }

    @Test
    void getPostTest() throws Exception{
        Long postId = 1L;
        Post post = PostFixture.defaultPost1();

        when(postRepository.findPostId(postId)).thenReturn(Optional.of(post));

        PostResponse result = postService.getPost(postId);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("NiAl", result.author().username());

        verify(postRepository, times(1)).findPostId(postId);
    }

    @Test
    void whenPostIdDoesNotExistTest() throws Exception{
        Long postId = 1L;

        when(postRepository.findPostId(postId)).thenReturn(Optional.empty());

        assertThrows(
            PostNotFoundException.class,
            () -> postService.getPost(postId)
        );

        verify(postRepository, times(1)).findPostId(postId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void createPostTest() throws Exception{
        Long userId = 1L;
        PostRequest postDTO = PostRequestFixture.defaultPost(); 
        Post post = PostFixture.defaultPost1();

        when(postRepository.createPost(any(Post.class))).thenReturn(post);

        PostResponse result = postService.createPost(postDTO, userId);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("NiAl", result.author().username());

        verify(postRepository, times(1)).createPost(any(Post.class));
    }

    @Test
    void updatePostTest() throws Exception{
        Long userId = 1L;
        Long postId = 1L;
        PostRequest postDTO = PostRequestFixture.defaultPost(); 
        Post post = PostFixture.defaultPost1();

        when(postRepository.isAuthorPost(postId)).thenReturn(userId);
        when(postRepository.findPostId(postId)).thenReturn(Optional.of(post));

        PostResponse result = postService.updatePost(postDTO, postId, userId);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("NiAl", result.author().username());

        verify(postRepository, times(1)).isAuthorPost(postId);
        verify(postRepository, times(1)).findPostId(postId);

    }

    @Test
    void updateWhereUserNotAuthorTest() throws Exception{
        Long userId = 1L;
        Long postId = 1L;
        PostRequest postDTO = PostRequestFixture.defaultPost(); 

        when(postRepository.isAuthorPost(postId)).thenReturn(2L);

        assertThrows(
            AccessDeniedException.class,
            () -> postService.updatePost(postDTO, postId, userId)
        );

        verify(postRepository, times(1)).isAuthorPost(postId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void deletePostTest() throws Exception{
        Long userId = 1L;
        Long postId = 1L;

        when(postRepository.isAuthorPost(postId)).thenReturn(userId);

        postService.deletePost(postId, userId);

        verify(postRepository, times(1)).isAuthorPost(postId);
        verify(postRepository, times(1)).deletePost(any(Post.class));
    }

    @Test
    void shouldThrowWhenNotAuthorTest() throws Exception{
        Long userId = 2L;
        Long postId = 1L;

        when(postRepository.isAuthorPost(postId)).thenReturn(1L);

        assertThrows(AccessDeniedException.class,
            () -> postService.deletePost(postId, userId));

        verify(postRepository, times(1)).isAuthorPost(postId);
        verify(postRepository, never()).deletePost(any(Post.class));
    }


}
