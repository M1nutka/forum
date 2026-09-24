package com.noname.forum.posts;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter 
@Getter 
public class PostResponse{

    private String title;
    private String description;
    private LocalDateTime createdAt;
    private AuthorResponse author;

} 
