package com.noname.forum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.noname.forum.db.AbstractPostgresIT;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.entity.User;
import com.noname.forum.exception.UserNotFoundException;
import com.noname.forum.fixture.UserRequestCreateFixture;
import com.noname.forum.fixture.UserRequestUpdateFixture;
import com.noname.forum.repository.UserRepository;



@SpringBootTest 
@ActiveProfiles("test")
public class UserServiceIT extends AbstractPostgresIT{
    
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private AuthService authService;

    @BeforeEach 
    void clean(){
        userRepository.deleteAll();
    }

    @Test 
    void getAllUserIT() throws Exception {
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserRequestCreate userRequestCreate2 = UserRequestCreateFixture.defaultRequestCreate2();
        UserResponse userResponse = authService.register(userRequestCreate);
        UserResponse userResponse2 = authService.register(userRequestCreate2);

        List<UserResponse> result = userService.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userResponse.id(), result.get(0).id());
        assertEquals(userRequestCreate.username(), result.get(0).username());
        assertEquals(userResponse2.id(), result.get(1).id());
        assertEquals(userRequestCreate2.username(), result.get(1).username());
    }

    @Test 
    void getUserIT() throws Exception {
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = authService.register(userRequestCreate);

        UserResponse result = userService.get(userResponse.id());
        assertNotNull(result);
        assertEquals(userResponse.id(), result.id());
        assertEquals(userRequestCreate.username(), result.username());
    }

    @Test 
    void getUserNotFoundIT() throws Exception {
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> userService.get(1L)
        );

        assert(exception.getMessage()).contains(String.valueOf(1L));
    }

    @Test 
    void updateUserIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = authService.register(userRequestCreate);

        UserRequestUpdate userRequestUpdate = UserRequestUpdateFixture.defaultRequestUpdate();
        UserResponse result = userService.update(userResponse.id(), userRequestUpdate);
        assertNotNull(result);
        assertEquals(userResponse.id(), result.id());
        assertEquals(userRequestUpdate.name(), result.name());

        UserResponse userUpdated = userService.get(userResponse.id());

        assertNotNull(userUpdated);
        assertEquals(userResponse.id(), userUpdated.id());
        assertEquals(userRequestCreate.username(), userUpdated.username());
        assertEquals(userRequestUpdate.name(), userUpdated.name());
    }

    @Test 
    void updateUserNotFoundIT() throws Exception{        
        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> userService.update(1L, UserRequestUpdateFixture.defaultRequestUpdate())
        );

        assert(exception.getMessage()).contains(String.valueOf(1L));
    }

    @Test 
    void deleteUserIT() throws Exception{
        UserResponse userResponse = authService.register(UserRequestCreateFixture.defaultRequestCreate());
        
        userService.delete(userResponse.id());

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class, 
            () -> userService.get(userResponse.id())
        );

        assert(exception.getMessage()).contains(String.valueOf(userResponse.id()));
    }

    @Test 
    void getByUsernameIT() throws Exception{
        UserRequestCreate userRequestCreate = UserRequestCreateFixture.defaultRequestCreate();
        UserResponse userResponse = authService.register(userRequestCreate);

        Optional<User> user = userService.getByUsername(userResponse.username());
        assertTrue(!user.isEmpty());
        assertEquals(userResponse.id(), user.get().getId());
        assertEquals(userRequestCreate.username(), user.get().getUsername());

    }

    @Test 
    void UserNotFoundByUsernameIT() throws Exception{
        Optional<User> user = userService.getByUsername("testdata");
        assertTrue(user.isEmpty());
    }

}
