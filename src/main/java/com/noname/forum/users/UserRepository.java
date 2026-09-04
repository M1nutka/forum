package com.noname.forum.users;

import java.sql.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
     
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    public List<User> getAllUser(){
        String sql = """
                "SELECT id, username, email, name, lastname, born_is, phone, is_active, description
                FROM users"
                """;
        List<User> users  = jdbcTemplate.query(
            sql, 
            (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setLastname(rs.getString("lastname"));

            Date date = (rs.getDate("born_is"));
            user.setBornIs(date.toLocalDate());

            user.setPhone(rs.getString("phone"));
            user.setIsActive(rs.getBoolean("is_active"));
            user.setDescription(rs.getString("description"));
            return user;
        });
        return users;
    }

    public User getUserById(int id){
        String sql = """
                "SELECT id, username, email, name, lastname, born_is, phone, is_active, description
                FROM users
                WHERE id = ?", 
                """;
        User current_user  = jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setLastname(rs.getString("lastname"));

            Date date = (rs.getDate("born_is"));
            user.setBornIs(date.toLocalDate());

            user.setPhone(rs.getString("phone"));
            user.setIsActive(rs.getBoolean("is_active"));
            user.setDescription(rs.getString("description"));
            return user;
        },
        id
    );
        return current_user;
    }
}
