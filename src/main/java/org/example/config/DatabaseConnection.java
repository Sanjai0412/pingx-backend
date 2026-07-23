package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
  private static final HikariDataSource dataSource;

  static {
    Dotenv dotenv = Dotenv.load();

    HikariConfig hikariConfig = new HikariConfig();

    hikariConfig.setJdbcUrl(dotenv.get("DB_URL"));
    dataSource = new HikariDataSource(hikariConfig);
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}