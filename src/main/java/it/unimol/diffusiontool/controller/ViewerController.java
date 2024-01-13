package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.ViewerApplication;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ViewerController {
    private final ViewerApplication viewerApp = ViewerApplication.getInstance();
    private final SimpleObjectProperty<Image> exportedImgProperty = new SimpleObjectProperty<>();
    @FXML
    private VBox parentBox;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView expImgView;

    @FXML
    public void initialize() {
        this.exportedImgProperty.set(ViewerApplication.getInstance().getExportedImage());
        this.expImgView.imageProperty().bind(exportedImgProperty);

        // Set black background
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        this.parentBox.setBackground(background);

        // Fixed resolutions for generated images, upscaled or not
        int ups_generatedWH = 768;
        int generatedWH = 512;

        // Force UI components to match app resolution
        if (!viewerApp.isGenerated()) {
            Platform.runLater(() -> {
                this.borderPane.setMinSize(viewerApp.getValidWidth(), viewerApp.getValidHeight());
                this.borderPane.setMaxSize(viewerApp.getValidWidth(), viewerApp.getValidHeight());
                this.expImgView.setFitWidth(viewerApp.getValidWidth());
                this.expImgView.setFitHeight(viewerApp.getValidHeight());

                // Listen for stage size changes
                viewerApp.getStage().widthProperty().addListener((obs, oldWidth, newWidth) -> {
                    this.borderPane.setMinWidth(newWidth.doubleValue());
                    this.borderPane.setMaxWidth(newWidth.doubleValue());
                    this.expImgView.setFitWidth(newWidth.doubleValue());
                });
                viewerApp.getStage().heightProperty().addListener((obs, oldHeight, newHeight) -> {
                    this.borderPane.setMinHeight(newHeight.doubleValue());
                    this.borderPane.setMaxHeight(newHeight.doubleValue());
                    this.expImgView.setFitHeight(newHeight.doubleValue());
                });
            });

        } else {
            if (viewerApp.isUpscaled()) {
                Platform.runLater(() -> {
                    this.borderPane.setMinSize(ups_generatedWH, ups_generatedWH);
                    this.borderPane.setMaxSize(ups_generatedWH, ups_generatedWH);
                    this.expImgView.setFitWidth(ups_generatedWH);
                    this.expImgView.setFitHeight(ups_generatedWH);
                });
            } else {
                Platform.runLater(() -> {
                    this.borderPane.setMinSize(generatedWH, generatedWH);
                    this.borderPane.setMaxSize(generatedWH, generatedWH);
                    this.expImgView.setFitWidth(generatedWH);
                    this.expImgView.setFitHeight(generatedWH);
                });
            }
        }
    }
}
