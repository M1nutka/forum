package com.noname.forum.posts;

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

    public List<Post> getAllPosts(){
         String sql = """
                SELECT p.id, p.title, p.description, p.created_at, p.user_id
                FROM posts p
                """;
        List<Post> posts  = jdbcTemplate.query(
            sql, 
            (rs, rowNum) -> {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setUserId(rs.getLong("user_id"));
            post.setTitle(rs.getString("title"));
            post.setDescription(rs.getString("description"));
            Timestamp date = (rs.getTimestamp("created_is"));
            post.setCreated_at(date.toLocalDateTime());
            return post;
        });
        return posts;
    }
    
}
