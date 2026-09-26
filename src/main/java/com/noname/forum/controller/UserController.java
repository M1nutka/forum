package com.noname.forum.controller;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        log.info("Get all users, controller");
        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getByUsernameForResponse(username));
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> userUpdate(@PathVariable Long id, @RequestBody @Valid  UserRequestUpdate updateDTO) {
        return ResponseEntity.ok().body(userService.updateUser(id, updateDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> userRemove(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
