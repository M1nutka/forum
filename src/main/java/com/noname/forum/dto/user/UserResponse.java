package com.noname.forum.dto.user;

import java.time.LocalDate;
import java.util.Set;

import com.noname.forum.domain.User;
import com.noname.forum.domain.UserRole;

public record UserResponse(
    Long id,
    String username,
    String email,
    String name,
    String lastname,
    LocalDate bornIs,
    String phone,
    Boolean isActive,
    String description,
    Set<UserRole> userRole
) {
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getName(),
            user.getLastname(),
            user.getBornIs(),
            user.getPhone(),
            user.getIsActive(),
            user.getDescription(),
            user.getUserRole()
        );
    }

    public UserResponse{
        userRole = userRole == null ? Set.of() : Set.copyOf(userRole);
    }

    public UserResponse withRoles (Set<UserRole>role){
        return new UserResponse(id, username, email, name, lastname, bornIs, phone, isActive, description, role);
    }
} 