package com.noname.forum.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service 
public class AuthService {
    private final AuthenticationManager authenticationManager;
    
    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

      
    public Authentication loginUser(JwtRequest response){
        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(response.getLogin(), response.getPassword())
        );
    }

}
