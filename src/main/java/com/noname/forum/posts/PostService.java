package com.noname.forum.posts;

import java.util.List;

import org.springframework.stereotype.Service;

@Service 
public class PostService {
    private final PostRepository postRepository;

    PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts(){
        return postRepository.getAllPosts();
    }
}
