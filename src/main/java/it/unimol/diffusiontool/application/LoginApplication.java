package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.entities.UserManager;
import it.unimol.diffusiontool.properties.FXMLProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginApplication extends Application {
    private Parent rootNode;
    private Stage stage;
    private static LoginApplication instance;

    public static void main(String[] args) {
        launch(args);
    }

    public static LoginApplication getInstance() {
        return instance;
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
        instance = this;
        UserManager userManager = UserManager.getInstance();
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
