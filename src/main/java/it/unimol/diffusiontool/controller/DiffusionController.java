package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.DiffusionApplication;
import it.unimol.diffusiontool.application.LoginApplication;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DiffusionController {
    @FXML
    private Label userLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button profileButton;
    @FXML
    private ImageView userImage;
    @FXML
    private Button homeButton;
    @FXML
    private Button generateButton;
    @FXML
    private Button upscaleButton;

    private String getLoggedInUser() {
        return DiffusionApplication.getToolInstance().getUser().getUsername();
    }

    @FXML
    private void initialize() {
        userLabel.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
        profileButton.setBackground(null);
    }

    @FXML
    private void OnLogOutClick() throws Exception {
        DiffusionApplication.getToolInstance().setUser(null);
        LoginApplication loginApplication = LoginApplication.getLoginInstance();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        loginApplication.init();
        loginApplication.start(new Stage());
    }

    @FXML
    private void OnProfileClick() {

    }

    @FXML
    private void OnHomeClick() {

    }

    @FXML
    private void OnGenerateClick() {

    }

    @FXML
    private void OnUpscaleClick() {

    }
}
