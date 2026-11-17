package com.noname.forum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.entity.User;
import com.noname.forum.exception.UserNotFoundException;
import com.noname.forum.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAll(){
        
        return userRepository.findAll().stream()
            .map(UserResponse::from)
            .toList();
    }

    public UserResponse get(Long id){
        return userRepository.findById(id)
            .map(UserResponse::from)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional 
    public UserResponse update(Long id, UserRequestUpdate updateRequest){
        Optional<User> userFound = userRepository.findById(id);
        if (userFound.isEmpty()){
            throw new  UserNotFoundException(id);
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

    public void delete(Long userId){
        userRepository.delete(userId);
    } 

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
