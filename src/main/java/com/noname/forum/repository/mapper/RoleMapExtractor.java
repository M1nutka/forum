package com.noname.forum.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component 
public class RoleMapExtractor implements ResultSetExtractor<Map<String, Long>> {

    @Override
    public Map<String, Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String, Long> map = new HashMap<>();
        while (rs.next()) {
            map.put(rs.getString("role"), rs.getLong("id"));
        }
        return map;
    }
}
