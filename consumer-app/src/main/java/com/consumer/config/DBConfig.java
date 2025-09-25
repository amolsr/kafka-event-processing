package com.consumer.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DBConfig {

    private Connection single_instance = null;
    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;

    @Value("${spring.clickhouse.jdbc-url}")
    private String clickhouseJdbcUrl;

    @Value("${spring.clickhouse.username}")
    private String clickhouseUsername;

    @Value("${spring.clickhouse.password}")
    private String clickhousePassword;

    @Value("${spring.clickhouse.connection-pool}")
    private int clickhouseConnectionPool;

    @PostConstruct
    public void initialize() {
        config.setMaximumPoolSize(clickhouseConnectionPool); // Adjust based on your needs
        config.setJdbcUrl(clickhouseJdbcUrl);
        config.setUsername(clickhouseUsername);
        config.setPassword(clickhousePassword);
        // config.addDataSourceProperty("use_server_time_zone", "true");
        // config.addDataSourceProperty("async_insert", "1");
        // config.addDataSourceProperty("wait_for_async_insert", "0");
        ds = new HikariDataSource(config);
    }

    @Bean
    public HikariDataSource dataSource() {
        return ds;
    }

    public Connection getInstance() throws SQLException {
        if (single_instance == null || single_instance.isClosed()) {
            if (ds == null) {
                throw new IllegalStateException("DataSource not initialized");
            }
            single_instance = ds.getConnection();
        }
        return single_instance;
    }
}
