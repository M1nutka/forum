package com.noname.forum.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.noname.forum.domain.User;
import com.noname.forum.domain.UserRole;
import com.noname.forum.map.UserRowMapper;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class UserListExtractor implements ResultSetExtractor<List<User>>{
    
    private final UserRowMapper userRowMapper;

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {

            Map<Long, User> userById = new LinkedHashMap<>();
            Map<Long, Set<UserRole>> rolesById = new HashMap<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                userById.computeIfAbsent(id, k -> {
                    try { return userRowMapper.mapRow(rs, 0); }
                    catch (SQLException e) { throw new RuntimeException(e); }
                });

                String role = rs.getString("role");
                if (role != null) {
                    rolesById.computeIfAbsent(id, k -> new HashSet<>())
                        .add(UserRole.valueOf(role));
                }
            }
            userById.forEach((id, user) -> 
                user.setUserRole(rolesById.getOrDefault(id, Set.of())));

            return new ArrayList<>(userById.values());
    }
}
