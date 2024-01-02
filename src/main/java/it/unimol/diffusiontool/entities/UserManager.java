package it.unimol.diffusiontool.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>();
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
        Iterator<User> iterator = this.users.values().iterator();
        User user;

        do {
            if (!iterator.hasNext())
                return false;

            user = iterator.next();
        }
        while (!username.equals(user.getUsername()));

        return true;
    }

    public boolean existsByEmail(String email) {
        Iterator<User> iterator = this.users.values().iterator();
        User user;

        do {
            if (!iterator.hasNext())
                return false;

            user = iterator.next();
        }
        while (!email.equals(user.getEmail()));

        return true;
    }

    public User findByUsername(String username) {
        Iterator<User> iterator = this.users.values().iterator();
        User user;

        do {
            if (!iterator.hasNext())
                return null;

            user = iterator.next();
        }
        while (!username.equals(user.getUsername()));

        return user;
    }

    public void deleteByUsername(String username) {
        this.users.entrySet().removeIf(entry -> entry.getValue().getUsername().equals(username));
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (User user : this.users.values())
            result.append(user.toString()).append("\n");

        return result.toString();
    }
}
