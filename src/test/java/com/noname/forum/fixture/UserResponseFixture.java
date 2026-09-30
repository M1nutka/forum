package com.noname.forum.fixture;

import java.time.LocalDate;

import com.noname.forum.dto.user.UserResponse;

public class UserResponseFixture {
    
    private UserResponseFixture() {}
    
    public static UserResponse defaultUser(){
        UserResponse user = new UserResponse(
            1L,
            "NiAl",
            "test@gmail.com",
            "Nill",
            "Amstrong",
            LocalDate.of(1934,12,12),
            "+45556667788",
            true,
            "I am Nill Amstrong",
            UserRoleFixture.usersRoles()
        );
        
        return user;
    }

    public static UserResponse defaultAdmin(){
        UserResponse user = new UserResponse(
            2L,
            "admin",
            "admin@gmail.com",
            "Hatoshi",
            "Nakimura",
            LocalDate.of(1970,12,12),
            "+91256667788",
            true,
            "I am Hatoshi",
            UserRoleFixture.adminRoles()
        );
        
        return user;
    }

}
