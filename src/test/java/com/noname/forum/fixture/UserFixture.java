package com.noname.forum.fixture;

import java.time.LocalDate;

import com.noname.forum.domain.User;

public class UserFixture {

    private UserFixture() {}
    
    public static User defaultUser(){
        User user = new User();
        
        user.setId(1L);
        user.setUsername("NiAl");
        user.setEmail("test@gmail.com");
        user.setName("Nill");
        user.setLastname("Amstrong");
        user.setBornIs(LocalDate.of(1934,12,12));
        user.setPhone("+45556667788");
        user.setPassword("qwerty");
        user.setIsActive(true);
        user.setDescription("I am Nill Amstrong");
        user.setUserRole(UserRoleFixture.usersRoles());
        
        return user;
    }

    public static User defaultAdmin(){
        User user = new User();
        
        user.setId(2L);
        user.setUsername("admin");
        user.setEmail("admin@gmail.com");
        user.setName("Hatoshi");
        user.setLastname("Nakimura");
        user.setBornIs(LocalDate.of(1970,12,12));
        user.setPhone("+91256667788");
        user.setPassword("123456");
        user.setIsActive(true);
        user.setDescription("I am Hatoshi");
        user.setUserRole(UserRoleFixture.adminRoles());
        
        return user;
    }
}
