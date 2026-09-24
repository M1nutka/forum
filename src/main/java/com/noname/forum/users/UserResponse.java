package com.noname.forum.users;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String lastname;
    private LocalDate bornIs;
    private String phone;
    private Boolean isActive;
    private String description;
    private Set<UserRole> userRole;
}
