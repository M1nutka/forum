package com.noname.forum.users;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.noname.forum.security.SecurityController;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUser(){
        log.info("Get all users");
        return userRepository.getAllUser();
    }

    public User getUserById(int id){
        log.info("Get user by id = " + id);
        return  userRepository.getUserById(id);
    }

    public User updateUser(int id, UserUpdateDTO updateDTO){
        log.info("Update user " + id);
        return userRepository.updateUser(id, updateDTO);
    }

    public void deleteUser(int id){
        log.info("Delete user " + id);
        userRepository.deleteUser(id);
    } 
}
