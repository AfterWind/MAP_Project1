package afterwind.lab1.permission;

import afterwind.lab1.FancyMain;

public enum Permission {

    QUERY(1),
    MODIFY(2),
    MANAGE(3);

    public final int id;

    Permission(int id) {
        this.id = id;
    }

    public static Permission get(int id) {
        switch (id) {
            case 1:
                return QUERY;
            case 2:
                return MODIFY;
            case 3:
                return MANAGE;
            default:
                return null;
        }
    }

    public boolean check() {
        return FancyMain.user.permissions.contains(this);
    }
}
