package com.noname.forum.fixture;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import com.noname.forum.domain.UserRole;

public final class UserRoleFixture {

    private UserRoleFixture() {}
    
    public static Set<UserRole> usersRoles(){
        return EnumSet.of(UserRole.USER);
    }

    public static Set<UserRole> adminRoles(){
        return EnumSet.of(UserRole.ADMIN);
    }

    public static Set<UserRole> allRoles(){
        return EnumSet.of(UserRole.USER, UserRole.ADMIN);
    }

    public static Set<UserRole> roles (UserRole... roles){
        return roles.length == 0
            ? EnumSet.noneOf(UserRole.class)
            : EnumSet.copyOf(Arrays.asList(roles));
    }
}
