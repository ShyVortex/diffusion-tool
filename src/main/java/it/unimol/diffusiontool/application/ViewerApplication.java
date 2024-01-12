package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.properties.FXMLProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ViewerApplication extends Application {
    private double validWidth;
    private double validHeight;
    private boolean isGenerated;
    private boolean isUpscaled;
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
        if (instance == null)
            instance = new ViewerApplication();

        return instance;
    }

    public double getValidWidth() {
        return validWidth;
    }

    public double getValidHeight() {
        return validHeight;
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

    public void setValidWidth(double width) {
        this.validWidth = width;
    }

    public void setValidHeight(double height) {
        this.validHeight = height;
    }

    public void setGenerated(boolean generated) {
        isGenerated = generated;
    }

    public void setUpscaled(boolean upscaled) {
        isUpscaled = upscaled;
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
        Scene scene = new Scene(this.rootNode);
        stage.setScene(scene);

        if (!isGenerated) {
            // Get monitor resolution
            Screen primaryScreen = Screen.getPrimary();
            double screenWidth = primaryScreen.getBounds().getWidth();
            double screenHeight = primaryScreen.getBounds().getHeight();

            // If image exceeds maximum resolution, resize it
            validWidth = exportedImg.getWidth();
            validHeight = exportedImg.getHeight();
            while (validHeight > screenWidth || validHeight > screenHeight) {
                validWidth -= (exportedImg.getWidth() - exportedImg.getWidth() / 1.35);
                validHeight -= (exportedImg.getHeight() - exportedImg.getHeight() / 1.35);
            }

            // Set stage resolution accordingly
            stage.setMinWidth(100);
            stage.setMinHeight(100);
            stage.setMaxWidth(screenWidth);
            stage.setMaxHeight(screenHeight);
            stage.setWidth(validWidth);
            stage.setHeight(validHeight);

        } else {
            if (isUpscaled) {
                stage.setWidth(768);
                stage.setHeight(768);
            } else {
                stage.setWidth(512);
                stage.setHeight(512);
            }
        }

        stage.setResizable(!isGenerated);
        stage.show();
        this.stage = stage;
    }
}
