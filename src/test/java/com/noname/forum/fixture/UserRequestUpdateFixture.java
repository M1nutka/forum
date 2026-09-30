package com.noname.forum.fixture;

import java.time.LocalDate;

import com.noname.forum.dto.user.UserRequestUpdate;

public class UserRequestUpdateFixture {
    
        public static UserRequestUpdate defaultRequestUpdate() {
        return new UserRequestUpdate("test@gmail.com",
            "Nill", "Amstrong", LocalDate.of(1934,12,12),
            "+45556667788", "I am Nill Amstrong");
    }
}
