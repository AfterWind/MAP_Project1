package afterwind.lab1.database;

import java.sql.*;

/**
 * Simple SQLite database class
 */
public class SQLiteDatabase {

    private String filename;
    private Connection connection;

    public SQLiteDatabase(String filename) {
        this.filename = filename;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void init() {
        try {
            getStatement("PRAGMA foreign_keys = ON;").execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a PreparedStatement that can be used multiple times with different arguments
     * @param sql the sql query with '?' instead of actual values (if needed)
     * @return the PreparedStatement
     */
    public PreparedStatement getStatement(String sql) {
        try {
            return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
