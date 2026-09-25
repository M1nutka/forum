package com.noname.forum.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noname.forum.users.User;
import com.noname.forum.users.UserRequestCreate;
import com.noname.forum.users.UserResponse;
import com.noname.forum.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class SecurityService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UserResponse register(UserRequestCreate userCreateDTO) {
        String hashPassword = passwordEncoder.encode(userCreateDTO.getPassword());
        userCreateDTO.setPassword(hashPassword);
        UserResponse user = repository.create(userCreateDTO);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.getByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        return user;
    }
}
