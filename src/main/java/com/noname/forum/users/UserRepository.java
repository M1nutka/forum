package com.noname.forum.users;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
     
    public List<UserResponse> getAll(){
        String sql = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description, r.role
                FROM users u
                JOIN userroles ur ON u.id = ur.user_id
                JOIN roles r ON ur.role_id = r.id
                """;
        return jdbcTemplate.query(sql, (ResultSetExtractor<List<UserResponse>>) rs -> {
            Map<Integer, UserResponse> map = new LinkedHashMap<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                UserResponse user = map.get(id);

                if (user == null) {
                    user = mapAllUser(rs);
                    map.put(id, user);
                }
                user.getUserRole().add(UserRole.valueOf(rs.getString("role")));
            }
            return new ArrayList<>(map.values());
        });
    }

    public UserResponse getById(Long id){
        String sql = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.password, u.is_active, u.description
                FROM users u
                WHERE u.id = ?
                """;
        UserResponse user  = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapUserForResponse(rs), id);

        user.setUserRole(findRolesByUserId(user.getId()));

        return user;
    }

    public UserResponse getByUsernameForResponse(@NonNull String username){        
        String sql = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.password, u.is_active, u.description
                FROM users u
                WHERE u.username = ?
                """;
       
        
        UserResponse user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapUserForResponse(rs), username);

        user.setUserRole(findRolesByUserId(user.getId()));

        return user;
    }

    public Optional<User> getByUsername(@NonNull String username){        
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

    private Set<UserRole> findRolesByUserId(Long id) {
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


    public User create(UserRequestToCreate userCreateDTO) {
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

    public User update(Long id, UserRequestToUpdate updateDTO){
        String sql = """
                UPDATE users
                SET name = ?, email = ?, lastname = ?, born_is = ?, phone = ?, description = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(
            sql,
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
            null,
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

    public void delete(Long id){
        String sql = """
                    DELETE FROM users
                    WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }

    private static User mapUser(ResultSet rs) throws SQLException{
        User u = new User();
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

        private static UserResponse mapUserForResponse(ResultSet rs) throws SQLException{
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

    private UserResponse mapAllUser(ResultSet rs) throws SQLException {
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
        u.setUserRole(new HashSet<>());
        return u;
    }
}

