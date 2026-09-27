package com.noname.forum.fixture;

import java.time.LocalDate;

import com.noname.forum.dto.user.UserRequestUpdate;

public class UserRequestUpdateFixture {
    
        public static UserRequestUpdate defaultRequestCreate() {
        return new UserRequestUpdate("ogurchik@gmail.com",
            "Rick", "Sanches", LocalDate.of(1988, 12, 21),
            "+78889997766", "I am Ogurchik Rick");
    }
}
