package com.noname.forum.posts;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
public class Post {
    Long id;
    Long userId;
    String title;
    String description;
    LocalDateTime created_at;
}
