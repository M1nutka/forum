package com.noname.forum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.noname.forum.domain.User;
import com.noname.forum.dto.user.UserRequestUpdate;
import com.noname.forum.dto.user.UserResponse;
import com.noname.forum.fixture.UserFixture;
import com.noname.forum.fixture.UserRequestUpdateFixture;
import com.noname.forum.repository.UserRepository;

@ExtendWith (MockitoExtension.class)
public class UserServiceTest {
    @Mock 
    private UserRepository userRepository;

    @InjectMocks  
    private UserService userService;

    @Test 
    void getAllUserTest() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(UserFixture.defaultUser());
        users.add(UserFixture.defaultAdmin());

        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAllUser();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("NiAl", result.get(0).username());
        assertEquals(2L, result.get(1).id());
        assertEquals("admin", result.get(1).username());

        verify(userRepository, times(1)).findAll();
    }

    @Test 
    void getUserTest() throws Exception {
        User user = UserFixture.defaultUser();
            
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserResponse result = userService.getUserById(user.getId());
        
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("NiAl", result.username());

        verify(userRepository, times(1)).findById(user.getId());
    }


    @Test 
    void updateUserTest() throws Exception {
        User user = UserFixture.defaultUser();

        UserRequestUpdate updateDTO = UserRequestUpdateFixture.defaultRequestUpdate();
            
        UserResponse result = userService.updateUser(user.getId(), updateDTO);
        
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Nill", result.name());

        verify(userRepository, times(1)).update(any(User.class));
    }

    @Test 
    void deleteUserTest() throws Exception {
        Long userId = 1L;            
        
        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(userId);
    }

}
