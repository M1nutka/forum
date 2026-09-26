package com.noname.forum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.service.SecurityService;

import lombok.RequiredArgsConstructor;


@RestController 
@RequiredArgsConstructor 
public class RegController {
    
    private final SecurityService securityService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> regester(@RequestBody UserRequestCreate userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(securityService.register(userCreateDTO));
    }
    
}
