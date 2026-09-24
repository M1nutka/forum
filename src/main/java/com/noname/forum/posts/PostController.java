package com.noname.forum.posts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.security.AuthController;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController 
@RequestMapping("/posts")
@RequiredArgsConstructor 
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final PostService postService;

    @GetMapping
    public List<PostResponse> getAllPosts() {
        log.info("Get all posts");
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable long id) {
        log.info("Get post id = " + id);
        return postService.getPost(id);
    }
    
    
}
