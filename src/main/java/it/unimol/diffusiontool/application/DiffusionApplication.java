package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.entities.User;
import it.unimol.diffusiontool.properties.FXMLProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DiffusionApplication extends Application {
    private Parent rootNode;
    private Stage stage;
    private FXMLLoader currentFXML;
    private static User user;
    private static DiffusionApplication toolInstance;

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

    public FXMLLoader getCurrentFXML() {
        return currentFXML;
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

    public void setCurrentFXML(FXMLLoader currentFXML) {
        this.currentFXML = currentFXML;
    }

    public void setUser(User user) {
        DiffusionApplication.user = user;
    }

    public void init() throws Exception {
        toolInstance = this;
        currentFXML = new FXMLLoader(FXMLProperties.getInstance().getHomeFXML().getLocation());
        this.rootNode = currentFXML.load();
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