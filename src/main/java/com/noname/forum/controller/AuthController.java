package com.noname.forum.controller;

import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.dto.jwt.JwtRequest;
import com.noname.forum.dto.jwt.JwtResponse;
import com.noname.forum.dto.jwt.RefreshJwtRequest;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping ("/api/auth")
@RestController 
@RequiredArgsConstructor 
public class AuthController implements  AuthApi{

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid  UserRequestCreate userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userCreateDTO));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest){
        final JwtResponse token = authService.login(authRequest);
        log.info("Login user " + authRequest.getLogin());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @Override
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request){
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());     
        log.info("User try get token");
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request){
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        log.info("User try get refresh");
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
    
    
    
}
