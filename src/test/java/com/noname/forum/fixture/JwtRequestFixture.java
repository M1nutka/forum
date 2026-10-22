package com.noname.forum.fixture;

import com.noname.forum.dto.jwt.JwtRequest;

public class JwtRequestFixture {
        private JwtRequestFixture() {}

        public static JwtRequest defultJwtRequest() {
                return new JwtRequest(
                        "ogurchikRick",
                        "qwerty"
                );
        }

        public static JwtRequest defultJwtRequest2() {
                return new JwtRequest(
                        "mortyyy",
                        "qwerty"
                );
        }
}
