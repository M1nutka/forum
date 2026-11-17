package com.noname.forum.controller;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/users")
public class UserController implements UserApi{

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @Override
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        log.info("Get all users");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.get(id));
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal Long userId, @RequestBody @Valid  UserRequestUpdate updateDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, updateDTO));
    }
    
    @Override
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<?> remove(@AuthenticationPrincipal Long userId){
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')") 
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> removeAdmin(@PathVariable Long userId){
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
