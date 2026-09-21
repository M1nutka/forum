package com.noname.forum.posts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.security.AuthController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;



@RestController 
@RequestMapping("/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final PostService postService;

    PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        log.info("Get all posts");
        return postService.getAllPosts();
    }
    
}
