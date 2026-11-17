package com.noname.forum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.noname.forum.dto.jwt.JwtRequest;
import com.noname.forum.dto.jwt.JwtResponse;
import com.noname.forum.dto.jwt.RefreshJwtRequest;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;

import jakarta.validation.Valid;

@RequestMapping ("/api/auth")
public interface  AuthApi {
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid  UserRequestCreate userCreateDTO);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest);

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request);

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request);
}
