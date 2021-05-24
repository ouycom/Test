package com.sscs.apitest.pinpad.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceConfig {

    /**
     * Hikari connection pool
     * @return
     */
//    @Bean
//    @ConfigurationProperties("spring.datasource")
//    public HikariDataSource dataSource() {
//        return (HikariDataSource) DataSourceBuilder.create().build();
//    }
}
