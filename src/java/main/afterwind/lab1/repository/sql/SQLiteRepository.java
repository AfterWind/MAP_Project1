package afterwind.lab1.repository.sql;

import afterwind.lab1.Utils;
import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.entity.Candidate;
import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.repository.PaginatedRepository;
import afterwind.lab1.repository.Repository;
import afterwind.lab1.validator.IValidator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A repository that uses SQLite for managing its data
 */
public abstract class SQLiteRepository<T extends IIdentifiable<Integer>> extends PaginatedRepository<T, Integer> {

    protected SQLiteDatabase database;
    protected PreparedStatement statementAdd;
    protected PreparedStatement statementRemove;
    protected PreparedStatement statementUpdate;
    protected PreparedStatement statementSelectAll;

    public SQLiteRepository(SQLiteDatabase database, IValidator<T> validator, int entitiesPerPage) {
        super(validator, entitiesPerPage);
        this.database = database;
    }

    protected abstract void initRemoveStatement();

    @Override
    public void remove(T e) {
        try {
            statementRemove.setInt(1, e.getId());
            statementRemove.executeUpdate();
            super.remove(e);
        } catch (SQLException ex) {
            if (ex.getMessage().contains("foreign key constraint failed")) {
                initRemoveStatement();
                Utils.showErrorMessage("You cannot delete this entity because it is linked\nto other entities in this program!");
            } else {
                Utils.showErrorMessage("An unexpected error occurred!");
                throw new RuntimeException(ex);
            }
        }
    }
}
