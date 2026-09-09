package com.noname.forum.security;

import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.users.Response;
import com.noname.forum.users.User;
import com.noname.forum.users.UserCreateDTO;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController 
public class SecurityController {

    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);

    private final AuthService authService;

    private final SecurityService securityService;
    
    public SecurityController(
        AuthService authService,
        SecurityService securityService
    ) {
        this.authService = authService;
        this.securityService = securityService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> postMethodName(@RequestBody Response response) {
        
        try {
            Authentication auth = authService.loginUser(response);
            log.info("Login successful for: {}", auth.getName());
            
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "email", auth.getName()
            ));
            
        } catch (BadCredentialsException e) {
            log.warn("Login failed - bad credentials for: {}", response.email());
            return ResponseEntity.status(401).body(Map.of(
                "error", "Invalid email or password"
            ));
            
        } catch (UsernameNotFoundException e) {
            log.warn("Login failed - user not found: {}", response.email());
            return ResponseEntity.status(401).body(Map.of(
                "error", "User not found"
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        log.info("Create new user");
        User user = securityService.register(userCreateDTO);
        
        return ResponseEntity.ok().body(user);
    }
    
    
}
