package com.noname.forum.map;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.noname.forum.domain.User;
import com.noname.forum.dto.user.UserRequestCreate;
import com.noname.forum.dto.user.UserResponse;

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
        Date date = rs.getDate("born_is");
        LocalDate bornIs = (date == null) ? null : date.toLocalDate();
        try {
            return new UserResponse(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("lastname"),
                bornIs,
                rs.getString("phone"),
                rs.getBoolean("is_active"),
                rs.getString("description"),
                Set.of()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement mapUserStatement(Connection connection, UserRequestCreate userCreateDTO, String hashPassword, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, userCreateDTO.username());
        ps.setString(2, userCreateDTO.email());
        ps.setString(3, userCreateDTO.lastname());
        ps.setString(4, userCreateDTO.lastname());
        ps.setDate(5, Date.valueOf(userCreateDTO.bornIs()));
        ps.setString(6, userCreateDTO.phone());
        ps.setString(7, hashPassword);
        ps.setBoolean(8, true);
        ps.setString(9, userCreateDTO.description());
        return ps;
    }
}
