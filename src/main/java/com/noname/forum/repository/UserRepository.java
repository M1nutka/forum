package com.noname.forum.repository;

import com.noname.forum.repository.mapper.RoleMapExtractor;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.noname.forum.dto.user.UserCredentials;
import com.noname.forum.entity.User;
import com.noname.forum.entity.UserRole;
import com.noname.forum.repository.mapper.UserCredentialsRowMapper;
import com.noname.forum.repository.mapper.UserListExtractor;
import com.noname.forum.repository.mapper.UserRowMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor  
public class UserRepository {

    private final RoleMapExtractor roleMapExtractor;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final UserRowMapper userRowMapper;

    private final UserCredentialsRowMapper userCredentialsRowMapper;

    private final UserListExtractor userListExtractor;

    private static final String SELECT_ALL = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description, r.role
                FROM users u
                JOIN userroles ur ON u.id = ur.user_id
                JOIN roles r ON ur.role_id = r.id
                """;

    private static final String SELECT_ID = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description
                FROM users u
                WHERE u.id = ?
                """;
    private static final String SELECT_USERNAME = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description
                FROM users u
                WHERE u.username = ?
                """;

    private static final String SELECT_USERNAME_CREDITAILS = """
                SELECT u.id, u.username, u.password
                FROM users u
                WHERE u.username = ?
                """;

    private static final String SELECT_ROLE_USER = """
                SELECT r.role
                FROM userroles ur
                JOIN roles r ON ur.role_id = r.id
                WHERE ur.user_id = ?
                """;

    private static final String INSERT_USER = """
                INSERT INTO users(username, email, name, lastname, born_is, phone, password, is_active, description)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

    private static final String INSERT_USER_ROLE = """
                INSERT INTO userroles (user_id, role_id)
                VALUES (?, ?)
                """;

    private static final String UPDATE = """
                UPDATE users
                SET name = ?, email = ?, lastname = ?, born_is = ?, phone = ?, description = ?
                WHERE id = ?
                """;

    private static final String DELETE = """
                DELETE FROM users
                WHERE id = ?
                """;

    private static final String DELETE_ALL = "DELETE FROM users";
    private static final String DELETE_ALL_USERROLES = "DELETE FROM userroles";
    private static final String DELETE_ALL_POSTS = "DELETE FROM posts";

    private static final String SELECT_ROLE_ID = "SELECT id, role FROM roles WHERE role IN (:names)";
     
    public List<User> findAll(){
        return jdbcTemplate.query(SELECT_ALL, userListExtractor);
    }

    public Optional<User> findById(Long id){
        try {
            User user = jdbcTemplate.queryForObject(SELECT_ID, userRowMapper, id);
            user.setUserRole(findRolesByUserId(id));
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(@NonNull String username){        
        try {
            User user = jdbcTemplate.queryForObject(SELECT_USERNAME, userRowMapper, username);
            user.setUserRole(findRolesByUserId(user.getId()));
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<UserCredentials> findByUsernameCredentials(@NonNull String username){        
        try {
            UserCredentials user = jdbcTemplate.queryForObject(SELECT_USERNAME_CREDITAILS, userCredentialsRowMapper, username);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Set<UserRole> findRolesByUserId(Long id) {
        return new HashSet<>(jdbcTemplate.queryForList(SELECT_ROLE_USER, String.class, id)
            .stream()
            .map(UserRole::valueOf)
            .toList());
    }


    public User create(User user) {
        Long id = jdbcTemplate.queryForObject( INSERT_USER, Long.class,
            user.getUsername(), user.getEmail(), user.getName(), user.getLastname(), user.getBornIs(),
            user.getPhone(), user.getPassword(), user.getIsActive(), user.getDescription());
        user.setId(id);
        return user;
    }

    public void assignRolesBatch(Long userId, Collection<Long> roleIds){

        List<Object[]> batchArgs = roleIds.stream()
            .map(roleId -> new Object[]{userId, roleId})
            .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(INSERT_USER_ROLE, batchArgs);
    }

    public void update(User user){
        jdbcTemplate.update(UPDATE, user.getName(), user.getEmail(), user.getLastname(), 
            user.getBornIs(), user.getPhone(),user.getDescription(), user.getId());
    }

    public void delete(Long id){
        jdbcTemplate.update(DELETE, id);
    }

    public void deleteAll(){
        jdbcTemplate.update(DELETE_ALL_USERROLES);
        jdbcTemplate.update(DELETE_ALL_POSTS);
        jdbcTemplate.update(DELETE_ALL);
    }

    public Map<String, Long> findRoleIdsByNames(Collection<String> rolesName){
        MapSqlParameterSource params = new MapSqlParameterSource("names", rolesName);
        return namedParameterJdbcTemplate.query(SELECT_ROLE_ID, params, roleMapExtractor);
    }
}

