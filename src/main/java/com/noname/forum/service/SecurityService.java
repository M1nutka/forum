package com.noname.forum.service;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noname.forum.domain.User;
import com.noname.forum.domain.UserRole;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class SecurityService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public UserResponse register(UserRequestCreate userCreateCommands) {
        String hashPassword = passwordEncoder.encode(userCreateCommands.password());
        User userCreate = new User();
        userCreate.setUsername(userCreateCommands.username());
        userCreate.setEmail(userCreateCommands.email());
        userCreate.setName(userCreateCommands.name());
        userCreate.setLastname(userCreateCommands.lastname());
        userCreate.setBornIs(userCreateCommands.bornIs());
        userCreate.setPhone(userCreateCommands.phone());
        userCreate.setPassword(hashPassword);
        userCreate.setIsActive(true);
        userCreate.setDescription(userCreateCommands.description());
        userCreate.setUserRole(Set.of(UserRole.USER));
        User user = repository.create(userCreate);
        return UserResponse.from(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        return user;
    }
}
