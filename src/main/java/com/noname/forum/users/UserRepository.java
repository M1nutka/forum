package com.noname.forum.users;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor  
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final UserMapper userMapper;
     
    public List<UserResponse> getAll(){
        String sql = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.is_active, u.description, r.role
                FROM users u
                JOIN userroles ur ON u.id = ur.user_id
                JOIN roles r ON ur.role_id = r.id
                """;

        return jdbcTemplate.query(sql, (ResultSetExtractor<List<UserResponse>>) rs -> {
            Map<Long, UserResponse> map = new LinkedHashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                UserResponse user = map.get(id);

                if (user == null) {
                    user = userMapper.mapUserResponse(rs);
                    map.put(id, user);
                }

                user.setUserRole(new HashSet<>());
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

        UserResponse user  = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> userMapper.mapUserResponse(rs), id);

        user.setUserRole(findRolesByUserId(user.getId()));

        return user;
    }

    public UserResponse getByUsernameForResponse(@NonNull String username){        
        String sql = """
                SELECT u.id, u.username, u.email, u.name, u.lastname, u.born_is, u.phone, u.password, u.is_active, u.description
                FROM users u
                WHERE u.username = ?
                """;
       
        UserResponse user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> userMapper.mapUserResponse(rs), username);

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
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> userMapper.mapUser(rs), username);

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


    public UserResponse create(UserRequestCreate userCreateDTO) {
        String sql = """
                INSERT INTO users(username, email, name, lastname, born_is, phone, password, is_active, description)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> userMapper.mapUserStatement(connection, userCreateDTO, sql), keyHolder);

        long id = keyHolder.getKey().intValue();
            
        String sqlRole = """
                INSERT INTO userroles (user_id, role_id)
                VALUES (?, 1)
                """;

        jdbcTemplate.update(sqlRole, id);

        return getById(id);
    }

    public UserResponse update(Long id, UserRequestUpdate updateDTO){
        String sql = """
                UPDATE users
                SET name = ?, email = ?, lastname = ?, born_is = ?, phone = ?, description = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, updateDTO.getName(), updateDTO.getEmail(), updateDTO.getLastname(), 
            updateDTO.getBornIs(), updateDTO.getPhone(),updateDTO.getDescription(), id);

        return getById(id);
    }

    public void delete(Long id){
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }
}

