package com.noname.forum.repository;


import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.noname.forum.domain.Post;
import com.noname.forum.map.PostRowMapper;

import lombok.RequiredArgsConstructor;

@Repository 
@RequiredArgsConstructor 
public class PostRepository {
    
    private final JdbcTemplate jdbcTemplate;

    private final PostRowMapper postRowMapper;

    private static final String SELECT_ALL = """
                SELECT p.id AS post_id, p.title, p.description, p.created_at, u.id AS user_id, u.username as author_username
                FROM posts p
                JOIN users u ON p.user_id = u.id
                """;

    private static final String CREATE = """
                INSERT INTO posts (title, description, user_id)
                VALUES (?, ?, ?)
                RETURNING id
                """;

    private static final String IS_AUTHOR = """
                SELECT user_id
                FROM posts
                WHERE id = ?
                """;

    private static final String UPDATE = """
                UPDATE posts
                SET title = ?, description = ?
                WHERE id = ?
                """;

    private static final String DELETE = """
                DELETE FROM posts
                WHERE id = ?
                """;

    public List<Post> findAllPosts(){
        List<Post> posts = jdbcTemplate.query(SELECT_ALL, postRowMapper);
        return posts;
    }

    public Post findPostId(long id) {
        Post post = jdbcTemplate.queryForObject(SELECT_ALL + " WHERE p.id = ?", postRowMapper, id);
        return post;
    }

    public Post createPost(Post post){
        Long id = jdbcTemplate.queryForObject(
            CREATE,
            Long.class,
            post.getTitle(),
            post.getDescription(),
            post.getAuthor().getId()
        );

        post.setId(id);
        return post;
    }

    public Long isAuthorPost(Long id) {
        return jdbcTemplate.queryForObject(IS_AUTHOR, Long.class, id); 
    }

    public Post updatePost(Post post) { 
        jdbcTemplate.update(UPDATE, post.getTitle(), post.getDescription(), post.getId());
        return post;
    }

    public void deletePost(Post post) {
        jdbcTemplate.update(DELETE, post.getId());
    }
}
