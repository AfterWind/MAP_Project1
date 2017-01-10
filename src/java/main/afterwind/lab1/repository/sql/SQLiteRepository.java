package afterwind.lab1.repository.sql;

import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.IValidator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A repository that uses SQLite for managing its data
 */
public abstract class SQLiteRepository<T extends IIdentifiable<Integer>> extends Repository<T, Integer> {

    protected SQLiteDatabase database;
    protected PreparedStatement statementAdd;
    protected PreparedStatement statementRemove;
    protected PreparedStatement statementUpdate;
    protected PreparedStatement statementSelectAll;

    public SQLiteRepository(SQLiteDatabase database, IValidator<T> validator) {
        super(validator);
        this.database = database;
    }

    @Override
    public void remove(T e) {
        try {
            statementRemove.setInt(1, e.getId());
            statementRemove.execute();
            super.remove(e);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
