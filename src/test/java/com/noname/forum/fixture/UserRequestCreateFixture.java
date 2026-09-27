package com.noname.forum.fixture;

import java.time.LocalDate;

import com.noname.forum.dto.user.UserRequestCreate;

public class UserRequestCreateFixture {
    
    public static UserRequestCreate defaultRequestCreate() {
        return new UserRequestCreate("ogurchikRick", "ogurchik@gmail.com",
            "Rick", "Sanches", LocalDate.of(1988, 12, 21),
            "+78889997766", "qwerty", "I am Ogurchik Rick");
    }
}
