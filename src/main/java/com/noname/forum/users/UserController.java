package com.noname.forum.users;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noname.forum.security.AuthController;

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
    public ResponseEntity<List<User>> getAllUser() {
        log.info("Get all users, controller");
        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> userUpdate(@PathVariable int id, @RequestBody @Valid  UserUpdateDTO updateDTO) {
        return ResponseEntity.ok().body(userService.updateUser(id, updateDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> userRemove(@PathVariable int id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
