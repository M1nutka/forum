package com.noname.forum.entity;

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
    User author;
    String title;
    String description;
    LocalDateTime createdAt;
}
