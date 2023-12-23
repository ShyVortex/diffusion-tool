package it.unimol.diffusiontool.entity;

import javafx.scene.image.Image;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gen-images")
public class GeneratedImage {
    @Id
    private ObjectId id;
    private User generatedBy;
    private Prompt prompt;
    private Image result;
}
