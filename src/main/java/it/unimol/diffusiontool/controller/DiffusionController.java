package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.DiffusionApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DiffusionController {
    @FXML
    private Label userLabel;
    private DiffusionApplication applicationInstance;
    private static DiffusionController diffusionController;

    public DiffusionController() {
    }

    public static DiffusionController getControllerInstance() {
        return diffusionController;
    }

    public DiffusionApplication getApplicationInstance() {
        return this.applicationInstance;
    }

    public void setApplicationInstance(DiffusionApplication instance) {
        this.applicationInstance = instance;
        this.updateUserLabel();
    }

    private void updateUserLabel() {
        if (this.applicationInstance != null && this.applicationInstance.getUser() != null) {
            Platform.runLater(() -> {
                this.userLabel.setText(this.applicationInstance.getUser().getUsername());
            });
        }

    }
}
