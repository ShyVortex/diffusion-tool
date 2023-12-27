package it.unimol.diffusiontool.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap();
    private static UserManager instance;

    protected UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }

        return instance;
    }

    public Map<String, User> getUsers() {
        return this.users;
    }

    public void addUser(User user) {
        if (!this.exists(user.getUsername())) {
            this.users.put(user.getId(), user);
        }
    }

    public boolean exists(String username) {
        Iterator var2 = this.users.values().iterator();

        User x;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            x = (User)var2.next();
        } while(!username.equals(x.getUsername()));

        return true;
    }

    public User findByUsername(String username) {
        Iterator var2 = this.users.values().iterator();

        User x;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            x = (User)var2.next();
        } while(!username.equals(x.getUsername()));

        return x;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        Iterator var2 = this.users.values().iterator();

        while(var2.hasNext()) {
            User user = (User)var2.next();
            result.append(user.toString()).append("\n");
        }

        return result.toString();
    }
}
