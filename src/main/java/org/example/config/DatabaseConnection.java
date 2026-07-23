package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
  private static final HikariDataSource dataSource;

  static {
    HikariConfig hikariConfig = new HikariConfig();

    hikariConfig.setJdbcUrl(System.getenv("DB_URL"));
    dataSource = new HikariDataSource(hikariConfig);
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}