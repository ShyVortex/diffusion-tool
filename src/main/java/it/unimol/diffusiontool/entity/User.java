package it.unimol.diffusiontool.entity;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class User {
    private String id = UUID.randomUUID().toString();
    private String email;
    private String username;
    private String password;
    private LocalDate birthDate;
    private Image profilePic;
    private Collection<GeneratedImage> generatedImages;

    public User(String email, String username, String password, LocalDate birthDate) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.profilePic = new Image("/default/new-user.png");
        this.generatedImages = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Image getProfilePic() {
        return this.profilePic;
    }

    public Collection<GeneratedImage> getGeneratedImages() {
        return this.generatedImages;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    public String toString() {
        return "User {\n id='" + this.id + "',\n email='" + this.email + "',\n username='"
                + this.username + "',\n password='" + this.password + "',\n birthDate="
                + this.birthDate + ",\n profilePic=" + this.profilePic + ",\n generatedImages="
                + this.generatedImages + "\n}";
    }
}
