package com.noname.forum.map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

import com.noname.forum.dto.post.AuthorResponse;
import com.noname.forum.dto.post.PostRequest;
import com.noname.forum.dto.post.PostResponse;

@Component 
public class PostMapper {
    
    public PostResponse mapPost (ResultSet rs) throws SQLException{
        return new PostResponse(
            rs.getLong("post_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            new AuthorResponse(            
                rs.getLong("user_id"),
                rs.getString("author_username"))
        );
    }

    public PreparedStatement mapPostStatement(Connection connection, PostRequest postRequest, Long author_id, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, postRequest.title());
        ps.setString(2, postRequest.description());
        ps.setLong(3, author_id);
        return ps;
    }
}
