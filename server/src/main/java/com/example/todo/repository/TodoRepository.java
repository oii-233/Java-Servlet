package com.example.todo.repository;

import com.example.todo.model.Todo;
import com.example.todo.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TodoRepository encapsulates all SQL for the todos table.
 */
public class TodoRepository {
    public Todo createTodo(String title, int userId) throws SQLException {
        String sql = "INSERT INTO todos (title, completed, user_id) VALUES (?, false, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setInt(2, userId);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating todo failed, no rows affected");
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    return new Todo(id, title, false, userId);
                } else {
                    throw new SQLException("Creating todo failed, no ID obtained");
                }
            }
        }
    }

    public List<Todo> getTodosByUser(int userId) throws SQLException {
        String sql = "SELECT id, title, completed, user_id FROM todos WHERE user_id = ? ORDER BY id DESC";
        List<Todo> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Todo(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getBoolean("completed"),
                            rs.getInt("user_id")));
                }
            }
        }
        return list;
    }

    public boolean updateTodoCompletion(int id, boolean completed, int userId) throws SQLException {
        String sql = "UPDATE todos SET completed = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, completed);
            ps.setInt(2, id);
            ps.setInt(3, userId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteTodo(int id, int userId) throws SQLException {
        String sql = "DELETE FROM todos WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        }
    }
}
