package com.noname.forum.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;
import com.noname.forum.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class PostController implements PostApi{

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final PostService postService;

    @Override
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAll() {
        log.info("Get all posts");
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAll());
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable Long id) {
        log.info("Get post id = " + id);
        return ResponseEntity.status(HttpStatus.OK).body(postService.get(id));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<PostResponse> create(@RequestBody @Valid  PostRequest request, @AuthenticationPrincipal Long id) {
        log.info("Create post");
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(request, id));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> put(@PathVariable Long id, @AuthenticationPrincipal Long userId,  @RequestBody @Valid  PostRequest request){
        log.info("Change post info id = " + id + ". UserId = " + userId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.update(request, id, userId));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal Long userId){
        log.info("Delete post id = " + id + ". UserId = " + userId);
        postService.delete(id, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
}
