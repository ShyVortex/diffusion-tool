package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.ViewerApplication;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ViewerController {
    private final SimpleObjectProperty<Image> exportedImgProperty = new SimpleObjectProperty<>();
    @FXML
    private ImageView expImgView;

    @FXML
    public void initialize() {
        this.exportedImgProperty.set(ViewerApplication.getInstance().getExportedImage());
        this.expImgView.imageProperty().bind(exportedImgProperty);
    }
}
