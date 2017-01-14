package afterwind.lab1.database;

import java.sql.*;

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


    public PreparedStatement getStatement(String sql) {
        try {
            return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
