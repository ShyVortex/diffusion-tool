package it.unimol.diffusiontool.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApplication extends Application {
	private Parent rootNode;

	public StartApplication() {
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void init() throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/start.fxml"));
		this.rootNode = fxmlLoader.load();
	}

	public void start(Stage stage) {
		stage.setScene(new Scene(this.rootNode));
		stage.show();
	}
}
