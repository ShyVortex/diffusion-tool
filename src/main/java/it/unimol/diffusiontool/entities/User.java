package it.unimol.diffusiontool.entities;

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
    private Collection<Image> generatedImages;
    private int upscImgsNum;

    public User(String email, String username, String password, LocalDate birthDate) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.profilePic = new Image("/default/new-user.png");
        this.generatedImages = new ArrayList<>();
        this.upscImgsNum = 0;
    }

    public static void free(User user) {
        user = null;
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

    public Collection<Image> getGeneratedImages() {
        return this.generatedImages;
    }

    public int getUpscImgsNum() {
        return upscImgsNum;
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

    public int countGeneratedImgs() {
        int num = 0;
        for (Image img : this.generatedImages)
            num++;

        return num;
    }

    public void addGeneratedImage(Image image) {
        this.generatedImages.add(image);
    }

    public void incUpscaledImages() {this.upscImgsNum++;}

    public String toString() {
        return "User {\n id='" + this.id + "',\n email='" + this.email + "',\n username='"
                + this.username + "',\n password='" + this.password + "',\n birthDate="
                + this.birthDate + ",\n profilePic=" + this.profilePic + ",\n generatedImages="
                + this.generatedImages + "\n}";
    }
}
