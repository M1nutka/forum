package com.noname.forum.dto.post;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter 
@Getter 
public class PostResponse{

    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private AuthorResponse author;

} 
