package com.example.drive.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class ActivityLogger {
    private ActivityLogger() {
    }

    public static void log(Long userId, String action, String entityType, Long entityId, String metadata) {
        String sql = "INSERT INTO activity (user_id, action, entity_type, entity_id, metadata) VALUES (?,?,?,?,?)";
        try (Connection conn = Db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, userId);
            ps.setString(2, action);
            ps.setString(3, entityType);
            ps.setObject(4, entityId);
            ps.setString(5, metadata);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Keep the app running even if logging fails
            e.printStackTrace();
        }
    }
}
