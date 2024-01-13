package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.entities.UserManager;
import it.unimol.diffusiontool.properties.FXMLProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Serial;
import java.io.Serializable;

public class LoginApplication extends Application implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static UserManager userManager;
    private Parent rootNode;
    private Stage stage;
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

    public void setRootNode(Parent rootNode) {
        this.rootNode = rootNode;
    }

    public void setRememberSession(boolean value) {
        rememberSession = value;
    }

    public static void setUserManager(UserManager manager) {
        userManager = manager;
    }

    public void init() throws Exception {
        instance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLProperties.getInstance().getLoginFXML().getLocation());
        this.rootNode = fxmlLoader.load();
    }

    public void start(Stage stage) {
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
