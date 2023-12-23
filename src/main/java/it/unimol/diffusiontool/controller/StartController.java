package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class StartController {
    @FXML
    private Button launchButton;

    @FXML
    private void onLaunchButtonClick() throws IOException {
        HelloApplication hello = new HelloApplication();
        Stage stage = (Stage) launchButton.getScene().getWindow();
        stage.close();
        hello.start(new Stage());
    }
}
