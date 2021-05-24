package com.sscs.apitest.pinpad.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Service
@Slf4j
public class ConnectionPoolService {
    @Autowired
    HikariDataSource dataSource;

    public Connection connection() throws SQLException {
        return connection(true);
    }

    public Connection connection(boolean autoCommit) throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(autoCommit);
        return conn;
    }

    public Connection freeConnection(Connection conn) throws SQLException {
        dataSource.evictConnection(conn);
        return conn;
    }
}