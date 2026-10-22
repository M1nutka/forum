package com.noname.forum.fixture;

import java.time.LocalDate;

import com.noname.forum.dto.user.UserRequestCreate;

public class UserRequestCreateFixture {
    
    public static UserRequestCreate defaultRequestCreate() {
        return new UserRequestCreate("ogurchikRick", "ogurchik@gmail.com",
            "Rick", "Sanches", LocalDate.of(1988, 12, 21),
            "+78889997766", "qwerty", "I am Ogurchik Rick");
    }

    public static UserRequestCreate defaultRequestCreate2() {
        return new UserRequestCreate("mortyyy", "morrrty@gmail.com",
            "Morty", "Smit", LocalDate.of(2005, 12, 21),
            "+78881297766", "qwerty", "I am Morty smit");
    }

    public static UserRequestCreate defaultRequestCreate3() {
        return new UserRequestCreate("yogoooo", "yuo@gmail.com",
            "Yogo", "neiro", LocalDate.of(2001, 12, 21),
            "+78881297799", "qwerty", "I am AI");
    }
}
