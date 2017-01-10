package afterwind.lab1.repository.sql;

import afterwind.lab1.database.SQLiteDatabase;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.permission.Permission;
import afterwind.lab1.permission.User;
import afterwind.lab1.validator.IValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteUserRepository extends SQLiteRepository<User> {

    private PreparedStatement statementPermissionAdd;
    private PreparedStatement statementPermissionRemove;
    private PreparedStatement statementPermissionSelectAll;

    public SQLiteUserRepository(SQLiteDatabase database, IValidator<User> validator) {
        super(database, validator);
        init();
        statementAdd = database.getStatement("INSERT INTO Users(Name, Password) VALUES(?, ?)");
        statementRemove = database.getStatement("DELETE FROM Users WHERE ID=?");
        statementUpdate = database.getStatement("UPDATE Users SET Name=?, Password=? WHERE ID=?");
        statementSelectAll = database.getStatement("SELECT * FROM Users");
        statementPermissionAdd = database.getStatement("INSERT INTO Permissions VALUES(?, ?)");
        statementPermissionRemove = database.getStatement("DELETE FROM Permissions WHERE UserID=? AND Permission=?");
        statementPermissionSelectAll = database.getStatement("SELECT * FROM Permissions WHERE UserID=?");
        load();
    }

    private void load() {
        try {
            ResultSet result = statementSelectAll.executeQuery();
            while (result.next()) {
                User user = new User(result.getInt(1), result.getString(2), result.getString(3));
                try {
                    super.add(user);

                    statementPermissionSelectAll.setInt(1, user.getId());
                    ResultSet permissions = statementPermissionSelectAll.executeQuery();
                    while (permissions.next()) {
                        user.permissions.add(Permission.get(result.getInt(2)));
                    }
                } catch (ValidationException e) {
                    System.out.println("Skipped loading entity with reason: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() {
        try {
            database.getStatement("BEGIN").execute();
            database.getStatement("CREATE TABLE IF NOT EXISTS Users(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Name VARCHAR(300)," +
                    "Password VARCHAR(300))").execute();
            database.getStatement("CREATE TABLE IF NOT EXISTS Permissions(" +
                    "UserID INTEGER," +
                    "Permission INTEGER," +
                    "PRIMARY KEY(UserID, Permission)," +
                    "FOREIGN KEY(UserID) REFERENCES Users(ID))").execute();
            database.getStatement("COMMIT").execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(User e) throws ValidationException {
        try {
            statementAdd.setString(1, e.getUsername());
            statementAdd.setString(2, e.getPassword());
            if (statementAdd.executeUpdate() != 0) {
                ResultSet keys = statementAdd.getGeneratedKeys();
                e.setId(keys.getInt(1));
            }
            super.add(e);
        } catch (SQLException e1) {
            throw new RuntimeException(e1);
        }
    }

    @Override
    public void update(Integer integer, User data) {
        try {
            statementUpdate.setString(1, data.getUsername());
            statementUpdate.setString(2, data.getPassword());
            statementUpdate.setInt(3, integer);
            statementUpdate.execute();
            super.update(integer, data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPermission(User user, Permission permission) {
        try {
            statementPermissionAdd.setInt(1, user.getId());
            statementPermissionAdd.setInt(2, permission.id);
            statementPermissionAdd.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePermission(User user, Permission permission) {
        try {
            statementPermissionRemove.setInt(1, user.getId());
            statementPermissionRemove.setInt(2, permission.id);
            statementPermissionRemove.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User get(String username) {
        for(User u : data) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
}
