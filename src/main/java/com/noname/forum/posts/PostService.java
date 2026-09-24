package com.noname.forum.posts;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class PostService {
    private final PostRepository postRepository;

    public List<PostResponse> getAllPosts(){
        return postRepository.findAllPosts();
    }

    public PostResponse getPost(long id){
        return postRepository.findPostById(id);
    }
}
