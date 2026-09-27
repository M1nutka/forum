package com.noname.forum.dto.post;

import java.time.LocalDateTime;

import com.noname.forum.domain.Post;
 
public record PostResponse(
    Long id,
    String title,
    String description,
    LocalDateTime createdAt,
    AuthorResponse author
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getDescription(),
            post.getCreatedAt(),
            AuthorResponse.from(post.getAuthor())
        );
    }
} 