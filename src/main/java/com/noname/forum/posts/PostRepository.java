package com.noname.forum.posts;


import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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

    public PostResponse findPostId(long id) {
        String sql = """
                SELECT p.id AS post_id, p.title, p.description, p.created_at, u.id AS user_id, u.username as author_username
                FROM posts p
                JOIN users u ON p.user_id = u.id
                WHERE p.id = ?
                """;

        PostResponse postResponse = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> postMapper.mapPost(rs), id);
    
        return postResponse;
    }

    public PostResponse createPost(PostRequest request, Long authorId){
        String sql = """
                INSERT INTO posts (title, description, user_id)
                VALUES (?, ?, ?)
                RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> postMapper.mapPostStatement(connection, request, authorId, sql), keyHolder);

        long id = keyHolder.getKey().intValue();

        return findPostId(id);

    }

    public boolean isAuthorPost(Long id, Long userId) {
        String sql = """
                SELECT user_id
                FROM posts
                WHERE id = ?
                """;

        try {
            Long authorId = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("user_id"), id);
            return userId.equals(authorId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public PostResponse updatePost(PostRequest request, Long id) {
        String sql = """
                UPDATE posts
                SET title = ?, description = ?
                WHERE id = ?
                """;
                
        jdbcTemplate.update(sql, request.getTitle(), request.getDescription(), id);

        return findPostId(id);
    }
}
