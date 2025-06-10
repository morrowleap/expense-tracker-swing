package tracker;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:expense_tracker.db";

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
