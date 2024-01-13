package it.unimol.diffusiontool.entities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Map<String, User> users = new HashMap<>();
    private static final Path data = Paths.get("data");

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

    public void saveUsers() throws IOException {
        // If data folder doesn't exist, create it
        if (!Files.exists(data))
            Files.createDirectories(data);

        try (
                FileOutputStream fileOutStr = new FileOutputStream(data + "/users.sr");
                ObjectOutputStream objOutStr = new ObjectOutputStream(fileOutStr)
        ) {
            objOutStr.writeObject(this);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static UserManager loadUsers() throws FileNotFoundException {
        if (!Files.exists(data))
            throw new FileNotFoundException();

        try (
                FileInputStream fileInStr = new FileInputStream(data + "/users.sr");
                ObjectInputStream objInStr = new ObjectInputStream(fileInStr)
        ) {
            Object o = objInStr.readObject();
            return (UserManager) o;

        } catch (IOException e) {
            return new UserManager();
        } catch (ClassNotFoundException ignored) {
            // the class always exists, can't be caught
            return null;
        }
    }
}
