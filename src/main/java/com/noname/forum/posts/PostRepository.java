package com.noname.forum.posts;


import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class PostRepository {
    

    private final JdbcTemplate jdbcTemplate;

    private final PostMapper postMapper;

    public List<PostResponse> findAllPosts(){
         String sql = """
                SELECT p.id AS post_id, p.title, p.description, p.created_at, u.id AS user_id, u.username as author_username
                FROM posts p
                JOIN users u ON p.user_id = u.id
                """;

        List<PostResponse> postsResponse = jdbcTemplate.query(sql, (rs, rowNum) -> postMapper.mapPost(rs));

        return postsResponse;
    }

    public PostResponse findPostById(long id) {
        String sql = """
                SELECT p.id AS post_id, p.title, p.description, p.created_at, u.id AS user_id, u.username as author_username
                FROM posts p
                JOIN users u ON p.user_id = u.id
                WHERE p.id = ?
                """;

        PostResponse postResponse = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> postMapper.mapPost(rs), id);
    
        return postResponse;
    }

    public PostResponse createPost(){
        return new PostResponse();
    }

}
