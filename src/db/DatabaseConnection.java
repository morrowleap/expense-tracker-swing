package db;

import java.sql.*;

/**
 * Provides connections to the SQLite database.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:expense_tracker.db";

    public static Connection getConnection() throws DatabaseConnectionException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to database", e);
        }
    }
}
