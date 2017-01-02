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
        PreparedStatement createCandidatesTable = getStatement(
                "CREATE TABLE IF NOT EXISTS Candidates(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Name VARCHAR(300) NOT NULL," +
                        "Address VARCHAR(300) NOT NULL," +
                        "Telephone VARCHAR(300) NOT NULL)");
        PreparedStatement createSectionsTable = getStatement(
                "CREATE TABLE IF NOT EXISTS Sections(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Name VARCHAR(300) NOT NULL," +
                        "Seats INTEGER NOT NULL)");
        PreparedStatement createOptionsTable = getStatement(
                "CREATE TABLE IF NOT EXISTS Options(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "CandidateID INTEGER," +
                        "SectionID INTEGER, " +
                        "FOREIGN KEY(CandidateID) REFERENCES Candidates(ID)," +
                        "FOREIGN KEY(SectionID) REFERENCES Sections(ID))");
        try {
            getStatement("BEGIN").execute();

            createCandidatesTable.execute();
            createSectionsTable.execute();
            createOptionsTable.execute();

            getStatement("COMMIT").execute();

            System.out.println("Successfully initialized tables!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public PreparedStatement getStatement(String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
