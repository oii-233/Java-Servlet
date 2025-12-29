package com.example.todo.repository;

import com.example.todo.model.User;
import com.example.todo.util.DBUtil;

import java.sql.*;
import java.util.Optional;

/**
 * UserRepository contains JDBC + SQL for users table.
 * No business logic here.
 */
public class UserRepository {
    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")));
                }
                return Optional.empty();
            }
        }
    }

    public Optional<User> findById(int id) throws SQLException {
        String sql = "SELECT id, username, password FROM users WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password")));
                }
                return Optional.empty();
            }
        }
    }

    public User createUser(String username, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating user failed, no rows affected");
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new User(id, username, passwordHash);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained");
                }
            }
        }
    }
}
