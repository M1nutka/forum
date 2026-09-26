package com.noname.forum.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.noname.forum.controller.AuthController;
import com.noname.forum.domain.User;
import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.repository.UserRepository;

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

    public UserResponse updateUser(Long id, UserRequestUpdate updateDTO){
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
