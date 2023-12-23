package it.unimol.diffusiontool.entity;

import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Document(collection = "users")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;
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
}
