package com.noname.forum.posts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository 
public class PostRepository {
    

    private final JdbcTemplate jdbcTemplate;
    
    public PostRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PostResponse> findAllPosts(){
         String sql = """
                SELECT p.id AS post_id, p.title, p.description, p.created_at, u.id AS user_id, u.username as author_username
                FROM posts p
                JOIN users u ON p.user_id = u.id
                """;

        List<PostResponse> postsResponse = jdbcTemplate.query(sql, (rs, rowNum) -> mapPost(rs));

        return postsResponse;
    }

    public PostResponse findPostById(long id) {
        String sql = """
                SELECT p.id AS post_id, p.title, p.description, p.created_at, u.id AS user_id, u.username as author_username
                FROM posts p
                JOIN users u ON p.user_id = u.id
                WHERE p.id = ?
                """;

        PostResponse postResponse = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapPost(rs), id);
    
        return postResponse;
    }

    public Post createPost(){
        return new Post();
    }

    private static PostResponse mapPost (ResultSet rs) throws SQLException{
        AuthorResponse authRes = new AuthorResponse();
        authRes.setId(rs.getLong("user_id"));
        authRes.setUsername(rs.getString("author_username"));

        PostResponse currentPost = new PostResponse();
        currentPost.setId(rs.getLong("post_id"));
        currentPost.setTitle(rs.getString("title"));
        currentPost.setDescription(rs.getString("description"));
        Timestamp date = (rs.getTimestamp("created_at"));
        currentPost.setCreatedAt(date.toLocalDateTime());
        currentPost.setAuthor(authRes);
        return currentPost;
    }

    
    
}
