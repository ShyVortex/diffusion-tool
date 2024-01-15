package it.unimol.diffusiontool.application;

import it.unimol.diffusiontool.controller.DiffusionController;
import it.unimol.diffusiontool.entities.User;
import it.unimol.diffusiontool.entities.UserManager;
import it.unimol.diffusiontool.properties.FXMLProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiffusionApplication extends Application {
    private Parent rootNode;
    private Stage stage;
    private FXMLLoader currentFXML;
    private static User user;
    private final UserManager userManager = LoginApplication.getUserManager();
    private static DiffusionApplication toolInstance;
    private static final Path data = Paths.get("data");

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

    public static void setUser(User given) {
        user = given;
    }

    public void init() {
        toolInstance = this;
    }

    public void start(Stage stage) throws IOException {
        currentFXML = new FXMLLoader(FXMLProperties.getInstance().getHomeFXML().getLocation());
        this.rootNode = currentFXML.load();

        DiffusionController diffController = currentFXML.getController();
        diffController.setStage(this.stage);

        stage.setScene(new Scene(this.rootNode));
        stage.setResizable(false);
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        stage.show();
        this.stage = stage;
    }

    public void restart() {
        this.stage.setScene(new Scene(rootNode));
        this.stage.show();
    }

    private void closeWindowEvent(WindowEvent windowEvent) {
        try {
            if (LoginApplication.getRememberSession())
                this.saveSession();
            userManager.saveUsers();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSession() throws IOException {
        if (!Files.exists(data))
            Files.createDirectories(data);

        try (
                FileOutputStream fileOutStr = new FileOutputStream(data + "/session.sr");
                ObjectOutputStream objOutStr = new ObjectOutputStream(fileOutStr)
        ) {
            objOutStr.writeObject(user);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static User loadSession() throws FileNotFoundException {
        if (!Files.exists(data))
            throw new FileNotFoundException();

        try (
                FileInputStream fileInStr = new FileInputStream(data + "/session.sr");
                ObjectInputStream objInStr = new ObjectInputStream(fileInStr)
        ) {
            Object o = objInStr.readObject();
            return (User) o;
        }
        catch (IOException e) { return null; }
        catch (ClassNotFoundException ignored) {
            // the class always exists, can't be caught
            return null;
        }
    }
}