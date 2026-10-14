package com.noname.forum.fixture;

import java.time.LocalDateTime;

import com.noname.forum.domain.Post;

public class PostFixture {
    
    public static Post defaultPost1() {
        return new Post(
            1L,
            UserFixture.defaultUser(),
            "My first post!",
            "Tu tu ru",
            LocalDateTime.of(2026, 2, 20, 1, 1)
        );
    }

    public static Post defaultPost2() {
        return new Post(
            2L,
            UserFixture.defaultAdmin(),
            "key title",
            "my second post",
            LocalDateTime.of(2026, 2, 22, 2, 2)
        );
    }
}
