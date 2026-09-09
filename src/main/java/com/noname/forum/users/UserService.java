package com.noname.forum.users;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUser(){
        return userRepository.getAllUser();
    }

    public User getUserById(int id){
        return  userRepository.getUserById(id);
    }
}
