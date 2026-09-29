package com.noname.forum.map;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.noname.forum.dto.user.UserCredentials;

@Component 
public class UserCredentialsRowMapper implements RowMapper<UserCredentials>{

    @Override
    public UserCredentials mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserCredentials(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password")
        );
    }
    

}
