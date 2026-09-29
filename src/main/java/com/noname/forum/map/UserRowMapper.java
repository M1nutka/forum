package com.noname.forum.map;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.noname.forum.domain.User;

@Component 
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
       User user = new User();
       user.setId(rs.getLong("id"));
       user.setUsername(rs.getString("username"));
       user.setEmail(rs.getString("email"));
       user.setName(rs.getString("name"));
       user.setLastname(rs.getString("lastname"));
       user.setBornIs(rs.getDate("born_is").toLocalDate());
       user.setPhone(rs.getString("phone"));
       user.setIsActive(rs.getBoolean("is_active"));
       user.setDescription(rs.getString("description"));
       user.setUserRole(Set.of());
       return user;
    }
    
}
