package it.unimol.diffusiontool.entities;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String id = UUID.randomUUID().toString();
    private String email;
    private String username;
    private String password;
    private LocalDate birthDate;
    private transient Image profilePic; // transient = not serialized directly
    private int genImgsNum;
    private int upsImgsNum;
    public static boolean isTest;

    public User(String email, String username, String password, LocalDate birthDate) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        if (!isTest)
            this.profilePic = new Image("/default/new-user.png");
        this.genImgsNum = 0;
        this.upsImgsNum = 0;
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

    public int getGenImgsNum() {
        return genImgsNum;
    }

    public int getUpsImgsNum() {
        return upsImgsNum;
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

    public void incGeneratedImages() {this.genImgsNum++;}

    public void incUpscaledImages() {this.upsImgsNum++;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email)
                && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, username);
    }

    public String toString() {
        return "User {\n id='" + this.id + "',\n email='" + this.email + "',\n username='"
                + this.username + "',\n password='" + this.password + "',\n birthDate="
                + this.birthDate + ",\n profilePic=" + this.profilePic + ",\n generatedImages="
                + this.genImgsNum + "',\n upscaledImages=" + this.upsImgsNum + "\n}";
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        // Convert Image to byte array and write it to the stream
        if (!isTest) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ImageIO.write(SwingFXUtils.fromFXImage(profilePic, null), "png", byteStream);
            byte[] imageBytes = byteStream.toByteArray();
            out.writeObject(imageBytes);
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // Read the byte array from the stream and convert it back to Image
        if (!isTest) {
            byte[] imageBytes = (byte[]) in.readObject();
            ByteArrayInputStream byteStream = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(byteStream);
            profilePic = SwingFXUtils.toFXImage(bufferedImage, null);
        }
    }
}
