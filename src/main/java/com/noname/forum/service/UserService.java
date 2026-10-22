package com.noname.forum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noname.forum.domain.User;
import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.exception.UserNotFoundException;
import com.noname.forum.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUser(){
        
        return userRepository.findAll().stream()
            .map(UserResponse::from)
            .toList();
    }

    public UserResponse getUserById(Long id){
        return userRepository.findById(id)
            .map(UserResponse::from)
            .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

    @Transactional 
    public UserResponse updateUser(Long id, UserRequestUpdate updateRequest){
        Optional<User> userFound = userRepository.findById(id);
        if (userFound.isEmpty()){
            throw new  UserNotFoundException(String.valueOf(id));
        }
        User user = new User();
        user.setId(id);
        user.setEmail(updateRequest.email());
        user.setName(updateRequest.name());
        user.setLastname(updateRequest.lastname());
        user.setBornIs(updateRequest.bornIs());
        user.setPhone(updateRequest.phone());
        user.setDescription(updateRequest.description());

        userRepository.update(user);
        return UserResponse.from(user);
    }

    public void deleteUser(Long userId){
        userRepository.delete(userId);
    } 

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserResponse getByUsernameForResponse(String username) {
        return userRepository.findByUsername(username)
            .map(UserResponse::from)
            .orElseThrow(() -> new UserNotFoundException(username));
    }

}
