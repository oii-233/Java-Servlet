package com.example.drive.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Db {
    private static final String JDBC_URL = "jdbc:h2:" + StorageConfig.getDbPath().toString() + "/drive;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private Db() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void init() {
        try {
            Path dbDir = StorageConfig.getDbPath();
            Files.createDirectories(dbDir);
            try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                        "id IDENTITY PRIMARY KEY," +
                        "email VARCHAR(255) UNIQUE NOT NULL," +
                        "password_hash VARCHAR(255) NOT NULL," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")");

                st.executeUpdate("CREATE TABLE IF NOT EXISTS folders (" +
                        "id IDENTITY PRIMARY KEY," +
                        "name VARCHAR(255) NOT NULL," +
                        "parent_id BIGINT," +
                        "owner_id BIGINT NOT NULL," +
                        "is_deleted BOOLEAN DEFAULT FALSE," +
                        "deleted_at TIMESTAMP," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (parent_id) REFERENCES folders(id)," +
                        "FOREIGN KEY (owner_id) REFERENCES users(id)" +
                        ")");

                st.executeUpdate("CREATE TABLE IF NOT EXISTS files (" +
                        "id IDENTITY PRIMARY KEY," +
                        "name VARCHAR(255) NOT NULL," +
                        "folder_id BIGINT," +
                        "owner_id BIGINT NOT NULL," +
                        "path VARCHAR(500) NOT NULL," +
                        "size BIGINT," +
                        "mime_type VARCHAR(255)," +
                        "is_deleted BOOLEAN DEFAULT FALSE," +
                        "deleted_at TIMESTAMP," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (folder_id) REFERENCES folders(id)," +
                        "FOREIGN KEY (owner_id) REFERENCES users(id)" +
                        ")");

                st.executeUpdate("CREATE TABLE IF NOT EXISTS shares (" +
                        "id IDENTITY PRIMARY KEY," +
                        "file_id BIGINT NOT NULL," +
                        "share_token VARCHAR(255) UNIQUE NOT NULL," +
                        "expires_at TIMESTAMP," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (file_id) REFERENCES files(id)" +
                        ")");

                st.executeUpdate("CREATE TABLE IF NOT EXISTS activity (" +
                        "id IDENTITY PRIMARY KEY," +
                        "user_id BIGINT," +
                        "action VARCHAR(100) NOT NULL," +
                        "entity_type VARCHAR(50)," +
                        "entity_id BIGINT," +
                        "metadata CLOB," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "FOREIGN KEY (user_id) REFERENCES users(id)" +
                        ")");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize database", e);
        }
    }
}
