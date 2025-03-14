package com.dainsleif.hartebeest.database;

import com.dainsleif.hartebeest.helpers.GameInfo;

import java.sql.*;

public class PlayerPosition {

    // Create table if it doesn't exist
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_position ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "x REAL,"
            + "y REAL"
            + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // SQLite connection
    public static Connection connect() {
        String url = "jdbc:sqlite:playerXY.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public static void savePlayerPosition() {
        createTable();
        String sql = "UPDATE player_position SET x = ?, y = ? WHERE id = 1";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, GameInfo.playerX);
            pstmt.setDouble(2, GameInfo.playerY);
            int affectedRows = pstmt.executeUpdate();

            // If no rows were updated, insert a new row
            if (affectedRows == 0) {
                String insertSql = "INSERT INTO player_position(id, x, y) VALUES(1, ?, ?)";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    insertPstmt.setDouble(1, GameInfo.playerX);
                    insertPstmt.setDouble(2, GameInfo.playerY);
                    insertPstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static double getPlayerXFromDB() {
        String sql = "SELECT x FROM player_position WHERE id = 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("x");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static double getPlayerYFromDB() {
        String sql = "SELECT y FROM player_position WHERE id = 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("y");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
