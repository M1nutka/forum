package com.noname.forum.users;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Component;

@Component 
public class UserMapper {
    
    public User mapUser(ResultSet rs) throws SQLException{
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setName(rs.getString("name"));
        u.setLastname(rs.getString("lastname"));

        Date date = rs.getDate("born_is");
        u.setBornIs(date == null ? null : date.toLocalDate());

        u.setPassword(rs.getString("password"));
        u.setPhone(rs.getString("phone"));
        u.setIsActive(rs.getBoolean("is_active"));
        u.setDescription(rs.getString("description"));
        return u;
    }

    public UserResponse mapUserResponse(ResultSet rs) throws SQLException{
        UserResponse u = new UserResponse();
        u.setId(rs.getLong("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setName(rs.getString("name"));
        u.setLastname(rs.getString("lastname"));

        Date date = rs.getDate("born_is");
        u.setBornIs(date == null ? null : date.toLocalDate());

        u.setPhone(rs.getString("phone"));
        u.setIsActive(rs.getBoolean("is_active"));
        u.setDescription(rs.getString("description"));
        return u;
    }

    public PreparedStatement mapUserStatement(Connection connection, UserRequestCreate userCreateDTO, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, userCreateDTO.getUsername());
        ps.setString(2, userCreateDTO.getEmail());
        ps.setString(3, userCreateDTO.getName());
        ps.setString(4, userCreateDTO.getLastname());
        ps.setDate(5, Date.valueOf(userCreateDTO.getBornIs()));
        ps.setString(6, userCreateDTO.getPhone());
        ps.setString(7, userCreateDTO.getPassword());
        ps.setBoolean(8, true);
        ps.setString(9, userCreateDTO.getDescription());
        return ps;
    }
}
