package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.ViewerApplication;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ViewerController {
    private final ViewerApplication viewerApp = ViewerApplication.getInstance();
    private final SimpleObjectProperty<Image> exportedImgProperty = new SimpleObjectProperty<>();
    @FXML
    private ImageView expImgView;

    @FXML
    public void initialize() {
        this.exportedImgProperty.set(ViewerApplication.getInstance().getExportedImage());
        this.expImgView.imageProperty().bind(exportedImgProperty);

        // Set ImageView component to match app resolution
        Platform.runLater(() -> {
            this.expImgView.setFitWidth(viewerApp.getValidWidth());
            this.expImgView.setFitHeight(viewerApp.getValidHeight());

            // Listen for stage size changes
            viewerApp.getStage().widthProperty().addListener((obs, oldWidth, newWidth)
                    -> this.expImgView.setFitWidth(newWidth.doubleValue()));
            viewerApp.getStage().heightProperty().addListener((obs, oldHeight, newHeight)
                    -> this.expImgView.setFitHeight(newHeight.doubleValue()));
        });
    }
}
