package com.noname.forum.map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.noname.forum.dto.post.AuthorResponse;
import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;

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

    public PreparedStatement mapPostStatement(Connection connection, PostRequest postRequest, Long author_id, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, postRequest.getTitle());
        ps.setString(2, postRequest.getDescription());
        ps.setLong(3, author_id);
        return ps;
    }
}
