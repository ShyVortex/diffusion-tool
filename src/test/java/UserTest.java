import it.unimol.diffusiontool.entities.User;
import it.unimol.diffusiontool.validators.BirthdateValidator;
import it.unimol.diffusiontool.validators.EmailValidator;
import it.unimol.diffusiontool.validators.UsernameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for the User class")
public class UserTest {
    private String email1, email2, email3;
    private String username1, username2, username3;
    private String password1, password2, password3;
    private LocalDate birthDate1, birthDate2, birthDate3;
    private User user1, user2, user3;

    @BeforeEach
    public void init() {
        User.isTest = true;

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
    public void mainTest() {
        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);

        assertNotEquals(user1, user2); // different IDs
        assertNotEquals(user1, user3);
        assertNotEquals(user2, user3);

        UsernameValidator usernameValidator = UsernameValidator.getInstance();
        BirthdateValidator birthdateValidator = BirthdateValidator.getInstance();
        EmailValidator emailValidator = EmailValidator.getInstance();

        assertTrue(usernameValidator.isValid(username1));
        assertTrue(usernameValidator.isValid(username3));
        assertTrue(birthdateValidator.isValid(birthDate1));
        assertTrue(birthdateValidator.isValid(birthDate3));
        assertTrue(emailValidator.isValid(email1));
        assertTrue(emailValidator.isValid(email3));

        // try illegal values
        user2.setBirthDate(LocalDate.parse("1800-01-01"));
        user2.setEmail("12d8ajoaic!#1æðł#akaxgianf.compare");
        user2.setUsername("109d9a'0pa,x.,aòlxao.aò");
        assertFalse(usernameValidator.isValid(user2.getUsername()));
        assertFalse(birthdateValidator.isValid(user2.getBirthDate()));
        assertFalse(emailValidator.isValid(user2.getEmail()));

        user2 = user1;
        assertEquals(user1, user2); // now they should be equal

        incGeneratedImages(user1);
        incUpscaledImages(user1);
        assertEquals(user1.getGenImgsNum(), user2.getGenImgsNum());
        assertEquals(user1.getUpsImgsNum(), user2.getUpsImgsNum());

        user1.setEmail("nowthis@gmail.com");
        assertEquals(user1.getEmail(), user2.getEmail());

        user1.setUsername("lastTest");
        assertEquals(user1.getUsername(), user2.getUsername());

        System.out.println("TEST SUCCESSFUL.\n");
    }

    public void incGeneratedImages(User user) {user.incGeneratedImages();}

    public void incUpscaledImages(User user) {user.incGeneratedImages();}
}
