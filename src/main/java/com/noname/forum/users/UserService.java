package com.noname.forum.users;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.noname.forum.security.AuthController;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUser(){
        log.info("Get all users");
        return userRepository.getAll();
    }

    public UserResponse getUserById(Long id){
        log.info("Get user by id = " + id);
        return  userRepository.getById(id);
    }

    public User updateUser(Long id, UserRequestToUpdate updateDTO){
        log.info("Update user " + id);
        return userRepository.update(id, updateDTO);
    }

    public void deleteUser(Long id){
        log.info("Delete user " + id);
        userRepository.delete(id);
    } 

    public Optional<User> getByUsername(String username) {
        log.info("Get user by username = " + username);
        return userRepository.getByUsername(username);
    }

    public UserResponse getByUsernameForResponse(String username) {
        log.info("Get user by username = " + username);
        return userRepository.getByUsernameForResponse(username);
    }

}
