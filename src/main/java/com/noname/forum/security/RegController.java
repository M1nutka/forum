package com.noname.forum.security;

import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.users.User;
import com.noname.forum.users.UserRequestToCreate;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequiredArgsConstructor 
public class RegController {
    
    private final SecurityService securityService;



    @PostMapping("/register")
    public User regester(@RequestBody UserRequestToCreate userCreateDTO) {
        
        return securityService.register(userCreateDTO);
    }
    
}
