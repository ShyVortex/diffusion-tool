package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DiffusionApplication extends Application {
    private Parent rootNode;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private static User user;
    private static DiffusionApplication toolInstance;

    public DiffusionApplication() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static DiffusionApplication getToolInstance() {
        return toolInstance;
    }

    public Parent getRootNode() {
        return this.rootNode;
    }

    public Stage getStage() {
        return this.stage;
    }

    public User getUser() {
        return user;
    }

    public void setRootNode(Parent rootNode) {
        this.rootNode = rootNode;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        DiffusionApplication.user = user;
    }

    public void init() throws Exception {
        toolInstance = this;
        this.fxmlLoader = new FXMLLoader(this.getClass().getResource("/app-home-view.fxml"));
        this.rootNode = this.fxmlLoader.load();
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

    public void refresh() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/app-home-view.fxml"));
        this.rootNode = fxmlLoader.load();
        this.start(this.stage);
    }
}