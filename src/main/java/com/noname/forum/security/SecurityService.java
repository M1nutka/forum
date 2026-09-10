package com.noname.forum.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noname.forum.users.User;
import com.noname.forum.users.UserCreateDTO;
import com.noname.forum.users.UserRepository;

@Service 
public class SecurityService implements UserDetailsService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;


    public SecurityService(UserRepository repository, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserCreateDTO userCreateDTO) {
        String hashPassword = passwordEncoder.encode(userCreateDTO.getPassword());
        userCreateDTO.setPassword(hashPassword);
        User user = repository.registerUser(userCreateDTO);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = repository.getUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found " + email);
        }
        return user;

        
    };
}
