import it.unimol.diffusiontool.entities.User;
import it.unimol.diffusiontool.entities.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for the UserManager")
public class UserManagerTest implements Serializable {
    private UserManager users;
    private String email1, email2, email3;
    private String username1, username2, username3;
    private String password1, password2, password3;
    private LocalDate birthDate1, birthDate2, birthDate3;
    private User user1, user2, user3;
    private static final Path data = Paths.get("src/test/data");

    @BeforeEach
    public void init() {
        User.isTest = true;

        try {
            users = loadUsers();
        } catch (FileNotFoundException e) {
            users = new UserManager();
        }

        email1 = "testemail@gmail.com";
        username1 = "TestUser";
        password1 = "test";
        birthDate1 = LocalDate.parse("2000-01-01");
        user1 = new User(email1, username1, password1, birthDate1);

        email2 = email1;
        username2 = username1;
        password2 = password1;
        birthDate2 = birthDate1;
        user2 = new User(email2, username2, password2, birthDate2);

        email3 = "differentemail@gmail.com";
        username3 = "DifferentUser";
        password3 = "difftest";
        birthDate3 = LocalDate.parse("2001-01-01");
        user3 = new User(email3, username3, password3, birthDate3);
    }

    @Test
    public void mainTest() throws IOException {
        assertNotNull(users);
        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);
        assertNotEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user2, user3);

        boolean preliminaryExistence = exists(username1) && exists(username2) && exists(username3);

        if (!preliminaryExistence) {
            addUser(user1);
            addUser(user2); // shouldn't be added as it is equal to user1
            addUser(user3);
        }

        assertTrue(existsByEmail(email1));
        assertTrue(existsByEmail(email2));
        assertTrue(existsByEmail(email3));

        saveUsers();

        if (preliminaryExistence)
            System.out.println("TEST SUCCESSFUL --> loadUsers()\n");
        else
            System.out.println("TEST SUCCESSFUL --> new UserManager()\n");
    }

    public void addUser(User user) {
        if (user != null && !exists(user.getUsername())) {
            this.users.getUsers().put(user.getId(), user);
        }
    }

    public boolean exists(String username) {
        Iterator<User> iterator = users.getUsers().values().iterator();
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
        Iterator<User> iterator = users.getUsers().values().iterator();
        User user;

        do {
            if (!iterator.hasNext())
                return false;

            user = iterator.next();
        }
        while (!email.equals(user.getEmail()));

        return true;
    }

    public void saveUsers() throws IOException {
        // If data folder doesn't exist, create it
        if (!Files.exists(data))
            Files.createDirectories(data);

        try (
                FileOutputStream fileOutStr = new FileOutputStream(data + "/users.sr");
                ObjectOutputStream objOutStr = new ObjectOutputStream(fileOutStr)
        ) {
            objOutStr.writeObject(users);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public UserManager loadUsers() throws FileNotFoundException {
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
