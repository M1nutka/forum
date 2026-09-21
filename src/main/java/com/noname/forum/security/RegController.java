package com.noname.forum.security;

import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.users.User;
import com.noname.forum.users.UserCreateDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequiredArgsConstructor 
public class RegController {
    
    private final SecurityService securityService;



    @PostMapping("/register")
    public User regester(@RequestBody UserCreateDTO userCreateDTO) {
        
        return securityService.register(userCreateDTO);
    }
    
}
