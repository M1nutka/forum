package com.noname.forum.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noname.forum.dto.jwt.JwtRequest;
import com.noname.forum.dto.jwt.JwtResponse;
import com.noname.forum.dto.user.UserCredentials;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.entity.User;
import com.noname.forum.entity.UserRole;
import com.noname.forum.exception.RoleNotFoundException;
import com.noname.forum.exception.UncorrectPasswordException;
import com.noname.forum.exception.UnvalidTokenException;
import com.noname.forum.exception.UserNotFoundException;
import com.noname.forum.repository.UserRepository;
import com.noname.forum.security.provider.JwtProvider;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService implements UserDetailsService{

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> refreshStorage = new HashMap<>();
    
    @Transactional 
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

        User user = userRepository.create(userCreate);

        Set<String> roleNames = user.getUserRole().stream()
            .map(role -> role.name())
            .collect(Collectors.toSet());

        Map<String, Long> rolesMap = userRepository.findRoleIdsByNames(roleNames);

        for (String roleName : roleNames){
            if (!rolesMap.containsKey(roleName)){
                throw new RoleNotFoundException(roleName);
            }  
        }
        
        Collection<Long> roleIds = rolesMap.values();
        userRepository.assignRolesBatch(user.getId(), roleIds);
        return UserResponse.from(user);
    }

    public JwtResponse login(@NonNull JwtRequest authRequest){
        final UserCredentials user = userRepository.findByUsernameCredentials(authRequest.getLogin())
            .orElseThrow(() -> new UserNotFoundException(authRequest.getLogin()));
        if (passwordEncoder.matches(authRequest.getPassword(), user.passwordHash())){
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.username(), refreshToken);

            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new UncorrectPasswordException(authRequest.getLogin());
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken){
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)){
                final UserCredentials user = userRepository.findByUsernameCredentials(login)
                    .orElseThrow(() -> new UserNotFoundException(login));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.username(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new UnvalidTokenException();
    }

    public JwtResponse refresh(@NonNull String refreshToken){
        if (jwtProvider.validateRefreshToken(refreshToken)){
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserCredentials user = userRepository.findByUsernameCredentials(login)
                    .orElseThrow(() -> new UserNotFoundException(login));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(accessToken, newRefreshToken);
                return  new JwtResponse(accessToken, newRefreshToken);
                
            }
        }
        throw new UnvalidTokenException();
    }
    
    public User getAuthInfo(){
        return (User) SecurityContextHolder.getContext().getAuthentication();
    } 

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
        return user;
    }
}
