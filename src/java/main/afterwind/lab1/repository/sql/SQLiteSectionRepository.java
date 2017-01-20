package afterwind.lab1.repository.sql;

import afterwind.lab1.Utils;
import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.entity.Section;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.validator.IValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteSectionRepository extends SQLiteRepository<Section> {

    public SQLiteSectionRepository(SQLiteDatabase database, IValidator<Section> validator, int entitiesPerPage) {
        super(database, validator, entitiesPerPage);
        this.database = database;
        initTable();
        initRemoveStatement();
        statementAdd = database.getStatement("INSERT INTO Sections VALUES(?, ?, ?)");
        statementUpdate = database.getStatement("UPDATE Sections SET Name = ?, Seats = ? WHERE ID = ?");
        statementSelectAll = database.getStatement("SELECT * FROM Sections");

        load();
    }

    @Override
    protected void initRemoveStatement() {
        statementRemove = database.getStatement("DELETE FROM Sections WHERE ID = ?");
    }

    private void initTable() {
        try {
            database.getStatement(
                    "CREATE TABLE IF NOT EXISTS Sections(" +
                            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "Name VARCHAR(300) NOT NULL," +
                            "Seats INTEGER NOT NULL)").execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        try {
            ResultSet result = statementSelectAll.executeQuery();
            while(result.next()) {
                Section s = new Section(result.getInt(1), result.getString(2), result.getInt(3));
                try {
                    super.add(s);
                } catch (ValidationException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Section s) throws ValidationException {
        try {
            statementAdd.setInt(1, s.getId());
            statementAdd.setString(2, s.getName());
            statementAdd.setInt(3, s.getNrLoc());
            statementAdd.execute();
            super.add(s);
        } catch (SQLException e) {
            Utils.showErrorMessage("An unexpected error occurred!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Integer key, Section data) {
        try {
            statementUpdate.setString(1, data.getName());
            statementUpdate.setInt(2, data.getNrLoc());
            statementUpdate.setInt(3, key);
            statementUpdate.execute();
            super.update(key, data);
        } catch (SQLException e) {
            Utils.showErrorMessage("An unexpected error occurred!");
            throw new RuntimeException(e);
        }
    }
}
