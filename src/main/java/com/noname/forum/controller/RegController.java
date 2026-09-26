package com.noname.forum.controller;

import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.service.SecurityService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequiredArgsConstructor 
public class RegController {
    
    private final SecurityService securityService;

    @PostMapping("/register")
    public UserResponse regester(@RequestBody UserRequestCreate userCreateDTO) {
        return securityService.register(userCreateDTO);
    }
    
}
