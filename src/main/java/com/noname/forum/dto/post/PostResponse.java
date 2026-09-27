package com.noname.forum.dto.post;

import java.time.LocalDateTime;
 
public record PostResponse(
    Long id,
    String title,
    String description,
    LocalDateTime createdAt,
    AuthorResponse author
) {
} 