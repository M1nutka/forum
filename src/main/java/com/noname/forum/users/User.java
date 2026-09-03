package com.noname.forum.users;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String lastname;
    private LocalDate bornIs;
    private String phone;
    private String password;
    private Boolean isActive;
    private String description;
}
