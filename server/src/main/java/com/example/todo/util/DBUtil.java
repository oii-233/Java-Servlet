package com.example.todo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBUtil is responsible solely for establishing JDBC connections.
 * It uses env vars: DB_URL, DB_USER, DB_PASSWORD.
 */
public final class DBUtil {
    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        String url = EnvUtil.getJdbcUrl();
        String user = EnvUtil.get("DB_USER");
        if (user == null || user.trim().isEmpty()) {
            user = EnvUtil.getRequired("PGUSER");
        }
        String password = EnvUtil.get("DB_PASSWORD");
        if (password == null || password.trim().isEmpty()) {
            password = EnvUtil.getRequired("PGPASSWORD");
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL driver not found", e);
        }
        return DriverManager.getConnection(url, user, password);
    }
}
