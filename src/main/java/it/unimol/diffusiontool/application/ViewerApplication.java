package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.properties.FXMLProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ViewerApplication extends Application {
    private Image exportedImg;
    private Parent rootNode;
    private Stage stage;
    private static ViewerApplication instance;

    public ViewerApplication() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static ViewerApplication getInstance() {
        return instance;
    }

    public Image getExportedImage() {
        return exportedImg;
    }

    public Parent getRootNode() {
        return rootNode;
    }

    public Stage getStage() {
        return stage;
    }

    public void setExportedImage(Image exportedImg) {
        this.exportedImg = exportedImg;
    }

    public void setRootNode(Parent rootNode) {
        this.rootNode = rootNode;
    }

    public void init() throws Exception {
        instance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLProperties.getInstance().getViewerFXML().getLocation());
        this.rootNode = fxmlLoader.load();
    }

    public void start(Stage stage) {
        stage.setScene(new Scene(this.rootNode));
        stage.setResizable(false);
        stage.show();
        this.stage = stage;
    }
}
