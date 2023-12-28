package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.DiffusionApplication;
import it.unimol.diffusiontool.application.LoginApplication;
import it.unimol.diffusiontool.entities.User;
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
    @FXML
    private Label genImgsLabel;
    @FXML
    private Label upscImgsLabel;

    private String getLoggedInUser() {
        return DiffusionApplication.getToolInstance().getUser().getUsername();
    }

    private String countGeneratedImgs() {
        User user = DiffusionApplication.getToolInstance().getUser();
        int num = user.countGeneratedImgs();
        String startText = "You have generated ";
        String endText = " images so far.";

        return startText + num + endText;
    }

    private String countUpscaledImgs() {
        User user = DiffusionApplication.getToolInstance().getUser();
        String startText = "You have upscaled ";
        String endText = " images so far.";

        return startText + user.getUpscImgsNum() + endText;
    }

    @FXML
    private void initialize() {
        userLabel.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
        profileButton.setBackground(null);
        genImgsLabel.textProperty().bind(Bindings.createStringBinding(this::countGeneratedImgs));
        upscImgsLabel.textProperty().bind(Bindings.createStringBinding(this::countUpscaledImgs));
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
