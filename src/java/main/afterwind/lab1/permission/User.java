package afterwind.lab1.permission;

import afterwind.lab1.entity.IIdentifiable;

import java.util.ArrayList;
import java.util.List;

/**
 * A user with a set of permissions to organize and protect data.
 */
public class User implements IIdentifiable<Integer> {

    public final List<Permission> permissions = new ArrayList<>();

    public Integer id;
    private String username, password;

    public User(Integer id, String user, String password) {
        this.username = user;
        this.password = password;
        this.id = id;
        this.permissions.add(Permission.QUERY);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPermissions(Permission perm) {
        return permissions.contains(perm);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
