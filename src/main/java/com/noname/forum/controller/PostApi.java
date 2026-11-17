package com.noname.forum.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;

import jakarta.validation.Valid;

@RequestMapping("/posts")
public interface PostApi {
    
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAll();

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable Long id);

    @PostMapping()
    public ResponseEntity<PostResponse> create(@RequestBody @Valid  PostRequest request, @AuthenticationPrincipal Long id);

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> put(@PathVariable Long id, @AuthenticationPrincipal Long userId,  @RequestBody @Valid  PostRequest request);

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal Long userId);
}
