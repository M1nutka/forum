package com.noname.forum.security;

import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.users.UserRequestToCreate;
import com.noname.forum.users.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequiredArgsConstructor 
public class RegController {
    
    private final SecurityService securityService;



    @PostMapping("/register")
    public UserResponse regester(@RequestBody UserRequestToCreate userCreateDTO) {
        return securityService.register(userCreateDTO);
    }
    
}
