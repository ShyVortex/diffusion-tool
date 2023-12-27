package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.DiffusionApplication;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DiffusionController {
    @FXML
    private Label userLabel;

    private String getLoggedInUser() {
        return DiffusionApplication.getToolInstance().getUser().toString();
    }

    @FXML
    private void init() {
        userLabel.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
    }
}
