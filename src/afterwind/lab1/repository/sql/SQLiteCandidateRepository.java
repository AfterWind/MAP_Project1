package afterwind.lab1.repository.sql;

import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.validator.IValidator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteCandidateRepository extends SQLiteRepository<Candidate> {
    public SQLiteCandidateRepository(SQLiteDatabase database, IValidator<Candidate> validator) {
        super(database, validator);
        statementAdd = database.getStatement("INSERT INTO Candidates VALUES(?, ?, ?, ?)");
        statementRemove = database.getStatement("DELETE FROM Candidates WHERE ID = ?");
        statementUpdate = database.getStatement("UPDATE Candidates SET Name = ?, Address = ?, Telephone = ? WHERE ID = ?");
        statementSelectAll = database.getStatement("SELECT * FROM Candidates");

        load();
    }

    private void load() {
        try {
            ResultSet result = statementSelectAll.executeQuery();
            while(result.next()) {
                Candidate c = new Candidate(result.getInt(1), result.getString(2), result.getString(4), result.getString(3));
                try {
                    super.add(c);
                } catch (ValidationException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Candidate c) throws ValidationException {
        try {
            statementAdd.setInt(1, c.getId());
            statementAdd.setString(2, c.getName());
            statementAdd.setString(3, c.getAddress());
            statementAdd.setString(4, c.getTelephone());
            statementAdd.execute();
            super.add(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Integer key, Candidate data) {
        try {
            statementUpdate.setString(1, data.getName());
            statementUpdate.setString(2, data.getAddress());
            statementUpdate.setString(3, data.getTelephone());
            statementUpdate.setInt(4, key);
            statementUpdate.execute();
            super.update(key, data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
