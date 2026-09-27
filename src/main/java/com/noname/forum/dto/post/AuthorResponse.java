package com.noname.forum.dto.post;

import com.noname.forum.domain.User;

public record AuthorResponse(
    Long id,
    String username
) {
    public static AuthorResponse from(User user) {
        if (user == null) return null; 
        return new AuthorResponse(user.getId(), user.getUsername());
    }
}