package com.noname.forum.users;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.noname.forum.security.SecurityController;


@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);


    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
     
    public UserRepository(JdbcTemplate jdbcTemplate,
        PasswordEncoder passwordEncoder
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }



    public List<User> getAllUser(){
        String sql = """
                "SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description, r.role
                FROM users u
                JOIN userroles ur ON u.id = ur.user_id
                JOIN roles r ON ur.role_id = r.id"
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
            user.setUserRole(UserRole.valueOf(rs.getString("role")));
            return user;
        });
        return users;
    }

    public User getUserById(int id){
        String sql = """
                "SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description, r.role
                FROM users u
                JOIN userroles ur ON u.id = ur.user_id
                JOIN roles r ON ur.role_id = r.id
                WHERE u.id = ?", 
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
            user.setUserRole(UserRole.valueOf(rs.getString("role")));
            return user;
        },
        id
    );
        return current_user;
    }

    public User getUserByEmail(String email){
        String sql = """
                "SELECT id, username, email, name, lastname, born_is, phone, is_active, description
                FROM users
                WHERE email = ?", 
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
        email
    );
        return current_user;
    }


    public User registerUser(UserCreateDTO userCreateDTO) {
        String hashPassword = passwordEncoder.encode(userCreateDTO.getPassword());
        userCreateDTO.setPassword(hashPassword);
        String sql = """
                INSERT INTO users(username, email, name, lastname, born_is, phone, password, is_active, description)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        log.info("" + keyHolder);

        jdbcTemplate.update(connection -> {
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
        }, keyHolder);

        log.info("" + keyHolder);
        int id = keyHolder.getKey().intValue();
            
        String sqlRole = """
                    INSERT INTO userroles (user_id, role_id)
                    VALUES (?, 1)
                """;
        jdbcTemplate.update(sqlRole, id);


        return new User(
            null,
            userCreateDTO.getUsername(),
            userCreateDTO.getEmail(),
            userCreateDTO.getName(),
            userCreateDTO.getLastname(),
            userCreateDTO.getBornIs(),
            userCreateDTO.getPhone(),
            null,
            true,
            userCreateDTO.getDescription(),
            UserRole.USER
        );
    }
}
