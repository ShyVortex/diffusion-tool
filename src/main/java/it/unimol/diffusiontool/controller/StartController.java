package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {
    @FXML
    private Button launchButton;

    public StartController() {
    }

    @FXML
    private void onLaunchButtonClick() throws IOException {
        HelloApplication hello = new HelloApplication();
        Stage stage = (Stage)this.launchButton.getScene().getWindow();
        stage.close();
        hello.start(new Stage());
    }
}
