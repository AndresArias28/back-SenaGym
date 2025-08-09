package com.gym.gym_ver2.infraestructure.auth;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        String dbUrl = System.getenv("DATABASE_URL"); // postgres://user:pass@host:5432/db
        if (dbUrl == null || dbUrl.isBlank()) {
            return new HikariDataSource(); // Deja que Spring use otra config (local, etc.)
        }
        URI uri = URI.create(dbUrl);

        String[] userInfo = uri.getUserInfo().split(":");
        String username = userInfo[0];
        String password = userInfo.length > 1 ? userInfo[1] : "";

        String jdbcUrl = String.format(
                "jdbc:postgresql://%s:%d%s?sslmode=require",
                uri.getHost(),
                (uri.getPort() == -1 ? 5432 : uri.getPort()),
                uri.getPath()
        );

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }
}
