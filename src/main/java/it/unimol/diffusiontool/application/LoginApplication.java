package it.unimol.diffusiontool.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Getter
@Setter
@SpringBootApplication
@ComponentScan(basePackages = "it.unimol.diffusiontool")
public class LoginApplication extends Application {
    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private Stage stage;
    @Getter
    private static LoginApplication loginInstance;
    private boolean rememberSession;

    public static void main(String[] args) {
        LoginApplication.launch(args);
    }

    @Override
    public void init() throws Exception {
        this.springContext = SpringApplication.run(this.getClass());
        loginInstance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/login-view.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(this.rootNode));
        stage.show();
        this.stage = stage;
    }

    public void restart(Parent rootNode) {
        this.stage.setScene(new Scene(rootNode));
        this.stage.show();
    }
}
