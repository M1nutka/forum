package com.noname.forum.posts;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class PostService {
    private final PostRepository postRepository;

    public List<PostResponse> getAllPosts(){
        return postRepository.findAllPosts();
    }

    public PostResponse getPost(Long id){
        return postRepository.findPostId(id);
    }

    public PostResponse createPost(PostRequest request, Long authorId) {
        return  postRepository.createPost(request, authorId);
    }

    public PostResponse updatePost(PostRequest request, Long id, Long userId) throws AccessDeniedException {
        if (!postRepository.isAuthorPost(id, userId)) {
            throw new AccessDeniedException("User is not author post");
        }

        return postRepository.updatePost(request, id);
    }
}


