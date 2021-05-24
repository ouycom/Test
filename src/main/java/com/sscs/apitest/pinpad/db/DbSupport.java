package com.sscs.apitest.pinpad.db;

import com.sscs.apitest.pinpad.config.SqlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class DbSupport {
    @Autowired
    private SqlConfig sqlConfig;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int update(Connection conn, String sqlName, Object[] param) throws SQLException {

        String sql = sqlConfig.getSql(sqlName);
        int result = jdbcTemplate.update(sql, param);
        return result;
    }

    public List<Map<String, Object>> select(String sqlName, Object[] param){
        String sql = sqlConfig.getSql(sqlName);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, param);
        return result;
    }
}
