package com.noname.forum.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noname.forum.domain.User;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class SecurityService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UserResponse register(UserRequestCreate userCreateDTO) {
        String hashPassword = passwordEncoder.encode(userCreateDTO.password());
        UserResponse user = repository.create(userCreateDTO, hashPassword);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.getByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        return user;
    }
}
