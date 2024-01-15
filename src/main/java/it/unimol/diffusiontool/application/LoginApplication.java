package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.controller.LoginController;
import it.unimol.diffusiontool.entities.UserManager;
import it.unimol.diffusiontool.properties.FXMLProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class LoginApplication extends Application implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static UserManager userManager;
    private Parent rootNode;
    private Stage stage;
    private FXMLLoader currentFXML;
    private static boolean rememberSession;
    private static LoginApplication instance;

    public static void main(String[] args) {
        launch(args);
    }

    public static LoginApplication getInstance() {
        if (instance == null)
            instance = new LoginApplication();

        return instance;
    }

    public Parent getRootNode() {
        return this.rootNode;
    }

    public static boolean getRememberSession() {
        return rememberSession;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public Stage getStage() {
        return this.stage;
    }

    public FXMLLoader getCurrentFXML() {
        return currentFXML;
    }

    public void setRootNode(Parent rootNode) {
        this.rootNode = rootNode;
    }

    public void setRememberSession(boolean value) {
        rememberSession = value;
    }

    public static void setUserManager(UserManager manager) {
        userManager = manager;
    }

    public void setCurrentFXML(FXMLLoader currentFXML) {
        this.currentFXML = currentFXML;
    }

    public void init() {
        instance = this;
    }

    public void start(Stage stage) throws IOException {
        currentFXML = new FXMLLoader(FXMLProperties.getInstance().getLoginFXML().getLocation());
        this.rootNode = currentFXML.load();

        LoginController loginController = currentFXML.getController();
        loginController.setStage(this.stage);

        stage.setScene(new Scene(this.rootNode));
        stage.setResizable(false);
        stage.show();
        this.stage = stage;
    }

    public void restart() {
        this.stage.setScene(new Scene(rootNode));
        this.stage.show();
    }
}
