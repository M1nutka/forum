package com.noname.forum.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;
import com.noname.forum.service.PostService;

import lombok.RequiredArgsConstructor;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;






@RestController 
@RequestMapping("/posts")
@RequiredArgsConstructor 
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        log.info("Get all posts");
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        log.info("Get post id = " + id);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(id));
    }

    @PostMapping()
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request, @AuthenticationPrincipal Long id) {
        log.info("Create post");
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> putPost(@PathVariable Long id, @AuthenticationPrincipal Long userId,  @RequestBody PostRequest request) throws AccessDeniedException {
        log.info("Change post info id = " + id + ". UserId = " + userId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(request, id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal Long userId){
        log.info("Delete post id = " + id + ". UserId = " + userId);
        postService.deletePost(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
}
