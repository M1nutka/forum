package com.noname.forum.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.noname.forum.entity.Post;
import com.noname.forum.entity.User;

@Component 
public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(rs.getLong("post_id"));
        post.setTitle(rs.getString("title"));
        post.setDescription(rs.getString("description"));
        post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        User author = new User();
        author.setId(rs.getLong("user_id"));
        author.setUsername(rs.getString("author_username"));
        post.setAuthor(author);

        return post;
    }
    
}
