package com.noname.forum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.dto.jwt.JwtRequest;
import com.noname.forum.dto.jwt.JwtResponse;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.exception.UncorrectPasswordException;
import com.noname.forum.exception.UnvalidTokenException;
import com.noname.forum.exception.UserNotFoundException;
import com.noname.forum.fixture.JwtRequestFixture;
import com.noname.forum.fixture.UserRequestCreateFixture;
import com.noname.forum.repository.UserRepository;

@SpringBootTest 
@ActiveProfiles("test")
public class AuthServiceIT extends AbstractPostgresIT{
    
    @Autowired private UserRepository userRepository;
    @Autowired private AuthService authService;

    @BeforeEach 
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    void registerIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();

        UserResponse userResponse = authService.register(userRequestCreate);
        assertNotNull(userResponse);
        assertEquals(userResponse.id(), userResponse.id());
        assertEquals(userRequestCreate.username(), userResponse.username());
    }

    @Test
    void loginIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        authService.register(userRequestCreate);

        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();
        JwtResponse jwtResponse = authService.login(jwtRequest);
        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getAccessToken());
        assertNotNull(jwtResponse.getRefreshToken());
    }

    @Test
    void loginUserNotFoundIT() throws Exception{
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> authService.login(jwtRequest)
        );

        assert(exception.getMessage()).contains(String.valueOf(jwtRequest.getLogin()));
    }

    @Test
    void loginUncorrectPasswordIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        authService.register(userRequestCreate);

        JwtRequest jwtRequest = JwtRequestFixture.PasswordIncorrectJwtRequest();
        UncorrectPasswordException exception = assertThrows(
            UncorrectPasswordException.class, 
            () -> authService.login(jwtRequest)
        );

        assert(exception.getMessage()).contains(String.valueOf(jwtRequest.getLogin()));
    }

    @Test
    void getAccessTokenIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        authService.register(userRequestCreate);
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();
        JwtResponse jwtResponse = authService.login(jwtRequest);

        JwtResponse jwtAccessUpdated = authService.getAccessToken(jwtResponse.getRefreshToken());
        assertNotNull(jwtAccessUpdated);
        assertNotNull(jwtAccessUpdated.getAccessToken());
        assertNotNull(jwtAccessUpdated.getRefreshToken());
    }

    @Test
    void unvalidAccessTokenIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        authService.register(userRequestCreate);
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();
        JwtResponse jwtResponse = authService.login(jwtRequest);

        UnvalidTokenException exception = assertThrows(
            UnvalidTokenException.class, 
            () -> authService.getAccessToken(jwtResponse.getRefreshToken() + 1)
        );

        assert(exception.getMessage()).contains("Token unvalid");
    }

    @Test
    void UserDeletedAccessTokenIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = authService.register(userRequestCreate);
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();
        JwtResponse jwtResponse = authService.login(jwtRequest);

        userRepository.delete(userResponse.id());

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> authService.getAccessToken(jwtResponse.getRefreshToken())
        );

        System.out.println(exception);
        assert(exception.getMessage()).contains(userResponse.username());
    }

        @Test
    void getRefreshTokenIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        authService.register(userRequestCreate);
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();
        JwtResponse jwtResponse = authService.login(jwtRequest);

        JwtResponse jwtAccessUpdated = authService.refresh(jwtResponse.getRefreshToken());
        assertNotNull(jwtAccessUpdated);
        assertNotNull(jwtAccessUpdated.getAccessToken());
        assertNotNull(jwtAccessUpdated.getRefreshToken());
    }

    @Test
    void unvalidRefreshTokenIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        authService.register(userRequestCreate);
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();
        JwtResponse jwtResponse = authService.login(jwtRequest);

        UnvalidTokenException exception = assertThrows(
            UnvalidTokenException.class, 
            () -> authService.refresh(jwtResponse.getRefreshToken() + 1)
        );

        assert(exception.getMessage()).contains("Token unvalid");
    }

    @Test
    void UserDeletedRefreshTokenIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = authService.register(userRequestCreate);
        JwtRequest jwtRequest = JwtRequestFixture.defultJwtRequest();
        JwtResponse jwtResponse = authService.login(jwtRequest);

        userRepository.delete(userResponse.id());

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> authService.refresh(jwtResponse.getRefreshToken())
        );

        System.out.println(exception);
        assert(exception.getMessage()).contains(userResponse.username());
    }
} 
