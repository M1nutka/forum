package com.noname.forum.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noname.forum.jwt.JwtProvider;
import com.noname.forum.jwt.JwtRequest;
import com.noname.forum.jwt.JwtResponse;
import com.noname.forum.users.User;
import com.noname.forum.users.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> refreshStorage = new HashMap<>();
    


    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException{
        final User user = userRepository.getByUsername(authRequest.getLogin())
            .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())){
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);

            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Uncorrect password");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException{
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)){
                final User user = userRepository.getByUsername(login)
                    .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("unvalid token");
    }


    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException{
        if (jwtProvider.validateRefreshToken(refreshToken)){
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.getByUsername(login)
                    .orElseThrow(() -> new AuthException("User not found"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(accessToken, newRefreshToken);
                return  new JwtResponse(accessToken, newRefreshToken);
                
            }
        }
        throw new AuthException("unvalid token");
    }
    

   public User getAuthInfo(){
    return (User) SecurityContextHolder.getContext().getAuthentication();
    } 
}
