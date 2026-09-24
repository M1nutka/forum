package com.noname.forum.posts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component 
public class PostMapper {
    
    public PostResponse mapPost (ResultSet rs) throws SQLException{
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
