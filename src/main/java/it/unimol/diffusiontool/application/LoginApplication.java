package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.entity.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApplication extends Application {
    private Parent rootNode;
    private Stage stage;
    private static LoginApplication loginInstance;

    public LoginApplication() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static LoginApplication getLoginInstance() {
        return loginInstance;
    }

    public Parent getRootNode() {
        return this.rootNode;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setRootNode(Parent rootNode) {
        this.rootNode = rootNode;
    }

    public void setRememberSession(boolean rememberSession) {
    }

    public void init() throws Exception {
        loginInstance = this;
        UserManager userManager = UserManager.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/login-view.fxml"));
        this.rootNode = (Parent)fxmlLoader.load();
    }

    public void start(Stage stage) {
        stage.setScene(new Scene(this.rootNode));
        stage.setResizable(false);
        stage.show();
        this.stage = stage;
    }

    public void restart(Parent rootNode) {
        this.stage.setScene(new Scene(rootNode));
        this.stage.show();
    }
}
