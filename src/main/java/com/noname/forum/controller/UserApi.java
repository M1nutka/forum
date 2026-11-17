package com.noname.forum.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;

import jakarta.validation.Valid;

@RequestMapping("/users")
public interface  UserApi {
    
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll();

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id);

    @PutMapping
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal Long userId, @RequestBody @Valid  UserRequestUpdate updateDTO);

    @DeleteMapping
    public ResponseEntity<?> remove(@AuthenticationPrincipal Long userId);

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> removeAdmin(@PathVariable Long userId);
}
