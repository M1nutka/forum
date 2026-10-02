package com.noname.forum.fixture;

import java.time.LocalDateTime;

import com.noname.forum.dto.post.AuthorResponse;
import com.noname.forum.dto.post.PostResponse;

public class PostResponseFixture {
    
    public static PostResponse defaultPostResponse1() {
        return new PostResponse(
            1L,
            "My first post!",
            "Tu tu ru",
            LocalDateTime.of(2026, 2, 20, 1, 1),
            new AuthorResponse(1L, "Cubs")
        );
    }

    public static PostResponse defaultPostResponse2() {
        return new PostResponse(
            2L,
            "key title",
            "my second post",
            LocalDateTime.of(2026, 2, 22, 2, 2),
            new AuthorResponse(2L, "Yoyyo")
        );
    }
}
