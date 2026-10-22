package com.noname.forum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noname.forum.domain.Post;
import com.noname.forum.domain.User;
import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;
import com.noname.forum.exception.PostNotFoundException;
import com.noname.forum.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class PostService {
    private final PostRepository postRepository;

    public List<PostResponse> getAllPosts(){
        return postRepository.findAllPosts().stream()
            .map(PostResponse::from)
            .toList();
    }

    public PostResponse getPost(Long id){
        return postRepository.findPostId(id)
            .map(PostResponse::from)
            .orElseThrow(() -> new PostNotFoundException(String.valueOf(id)));
    }

    public PostResponse createPost(PostRequest request, Long authorId) {
        Post postCreate = new Post();
        postCreate.setTitle(request.title());
        postCreate.setDescription(request.description());
        
        User author = new User();
        author.setId(authorId);

        postCreate.setAuthor(author);
        return PostResponse.from(postRepository.createPost(postCreate));
    }

    @Transactional 
    public PostResponse updatePost(PostRequest request, Long id, Long userId) throws AccessDeniedException {

        Optional<Post> post = postRepository.findPostId(id);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post not found where id = " + id);
        }

        if (postRepository.isAuthorPost(id) != userId) {
            throw new AccessDeniedException("User is not author post");
        }

        Post postUpdate = new Post();
        postUpdate.setTitle(request.title());
        postUpdate.setDescription(request.description());
        postUpdate.setId(id);

        postRepository.updatePost(postUpdate);

        return PostResponse.from(postRepository.findPostId(postUpdate.getId()).get());
    }

    @Transactional 
    public void deletePost(Long id, Long userId) {
        Optional<Post> post = postRepository.findPostId(id);
        if (post.isEmpty()) {
            throw new PostNotFoundException("Post not found where id = " + id);
        }

        if (postRepository.isAuthorPost(id) != userId) {
            throw new AccessDeniedException("User is not author post");
        }
        
        Post postDelete = new Post();
        postDelete.setId(id);
        postRepository.deletePost(postDelete);
    }
}


