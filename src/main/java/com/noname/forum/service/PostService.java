package com.noname.forum.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.noname.forum.domain.Post;
import com.noname.forum.domain.User;
import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;
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
        return PostResponse.from(postRepository.findPostId(id));
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

    public PostResponse updatePost(PostRequest request, Long id, Long userId) throws AccessDeniedException {
         // TODO: Добавть проверку на админа
        if (postRepository.isAuthorPost(id) != userId) {
            throw new AccessDeniedException("User is not author post");
        }

        Post postUpdate = new Post();
        postUpdate.setTitle(request.title());
        postUpdate.setDescription(request.description());
        postUpdate.setId(id);

        return PostResponse.from(postRepository.updatePost(postUpdate));
    }

    public void deletePost(Long id, Long userId) {
         // TODO: Добавть проверку на админа
        if (postRepository.isAuthorPost(id) != userId) {
            throw new AccessDeniedException("User is not author post");
        }
        
        Post postDelete = new Post();
        postDelete.setId(id);

        postRepository.deletePost(postDelete);
    }
}


