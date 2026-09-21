package com.noname.forum.users;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.noname.forum.security.AuthController;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor  
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final JdbcTemplate jdbcTemplate;
     
    public List<User> getAll(){
        String sql = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description, r.role
                FROM users u
                JOIN userroles ur ON u.id = ur.user_id
                JOIN roles r ON ur.role_id = r.id
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
            user.setUserRole(Set.of(UserRole.valueOf(rs.getString("role"))));
            return user;
        });
        return users;
    }

    public User getById(int id){
        String sql = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description, r.role
                FROM users u
                JOIN userroles ur ON u.id = ur.user_id
                JOIN roles r ON ur.role_id = r.id
                WHERE u.id = ?
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
            user.setUserRole(Set.of(UserRole.valueOf(rs.getString("role"))));
            return user;
        },
        id
    );
        return current_user;
    }

    public Optional<User> getByUsername(@NonNull String username){
        log.info("Get user by username = {}", username);
        
        String sql = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.password, u.is_active, u.description
                FROM users u
                WHERE u.username = ?
                """;
       
        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapUser(rs), username);
            user.setUserRole(findRolesByUserId(user.getId()));
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Set<UserRole> findRolesByUserId(int id) {
        String sql = """
                SELECT r.role
                FROM userroles ur
                JOIN roles r ON ur.role_id = r.id
                WHERE ur.user_id = ?
                """;
        return new HashSet<>(jdbcTemplate.queryForList(sql, String.class, id)
            .stream()
            .map(UserRole::valueOf)
            .toList());
    }


    public User create(UserCreateDTO userCreateDTO) {
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
            userCreateDTO.getPassword(),
            true,
            userCreateDTO.getDescription(),
            Set.of(UserRole.USER)
        );
    }

    public User update(int id, UserUpdateDTO updateDTO){
        String sql = """
                UPDATE users
                SET username = ?, name = ?, email = ?, lastname = ?, born_is = ?, phone = ?, description = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(
            sql,
            updateDTO.getUsername(),
            updateDTO.getName(),
            updateDTO.getEmail(),
            updateDTO.getLastname(),
            updateDTO.getBornIs(),
            updateDTO.getPhone(),
            updateDTO.getDescription(),
            id
        );

        return new User(
            id,
            updateDTO.getUsername(),
            updateDTO.getEmail(),
            updateDTO.getName(),
            updateDTO.getLastname(),
            updateDTO.getBornIs(),
            updateDTO.getPhone(),
            null,
            true,
            updateDTO.getDescription(),
            null
        );
    }

    public void delete(int id){
        String sql = """
                    DELETE FROM users
                    WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    private static User mapUser(ResultSet rs) throws SQLException{
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setLastname(rs.getString("lastname"));

        Date date = (rs.getDate("born_is"));
        if (date != null){
                user.setBornIs(date.toLocalDate());
        }

        user.setPhone(rs.getString("phone"));
        user.setPassword(rs.getString("password"));
        user.setIsActive(rs.getBoolean("is_active"));
        user.setDescription(rs.getString("description"));
        // user.setUserRole(Set.of(UserRole.valueOf(rs.getString("role"))));
        return user;
    }
}

