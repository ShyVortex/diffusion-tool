package it.unimol.diffusiontool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unimol.diffusiontool.application.LoginApplication;
import it.unimol.diffusiontool.entity.User;
import it.unimol.diffusiontool.exceptions.BlankFieldException;
import it.unimol.diffusiontool.exceptions.DuplicatedUserException;
import it.unimol.diffusiontool.exceptions.IncorrectPasswordException;
import it.unimol.diffusiontool.exceptions.UserNotFoundException;
import it.unimol.diffusiontool.repository.UserRepository;
import it.unimol.diffusiontool.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class LoginController {
    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label birthdateLabel;
    @FXML
    private DatePicker birthdatePicker;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField emailField;
    @FXML
    private CheckBox rememberMeCheck;
    @FXML
    private Button confirmSignInButton;
    @FXML
    private Button confirmSignUpButton;
    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @FXML
    private void onSignInClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/login-view.fxml"));
        LoginApplication loginApplication = LoginApplication.getLoginInstance();
        fxmlLoader.setControllerFactory(loginApplication.getSpringContext()::getBean);
        Parent rootNode = fxmlLoader.load();
        loginApplication.setRootNode(rootNode);
        loginApplication.restart(rootNode);
    }

    @FXML
    private void onSignUpClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/signup-view.fxml"));
        LoginApplication loginApplication = LoginApplication.getLoginInstance();
        fxmlLoader.setControllerFactory(loginApplication.getSpringContext()::getBean);
        Parent rootNode = fxmlLoader.load();
        loginApplication.setRootNode(rootNode);
        loginApplication.restart(rootNode);
    }

    @FXML
    private void onRememberMeClick() {
        LoginApplication loginApplication = LoginApplication.getLoginInstance();
        loginApplication.setRememberSession(true);
    }

    @FXML
    private void onConfirmSignInClick() {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        Optional<User> user = userRepository.findByUsername(username);

        try {
            if (user.isPresent()) {
                if (password.equals(user.get().getPassword())) {
                    // todo
                }
                else
                    throw new IncorrectPasswordException();
            }
            else
                throw new UserNotFoundException("User not found: " + username);

        } catch (UserNotFoundException e) {
            Alert notFoundAlert = new Alert(Alert.AlertType.ERROR);
            notFoundAlert.setHeaderText("ERROR: User not found");
            notFoundAlert.setContentText("No user has been found with such credentials.");
            notFoundAlert.showAndWait();
        } catch (IncorrectPasswordException e) {
            Alert incorrectPassAlert = new Alert(Alert.AlertType.ERROR);
            incorrectPassAlert.setHeaderText("ERROR: Incorrect password");
            incorrectPassAlert.setContentText("Login has failed due to not matching credentials.");
        }
    }

    @FXML
    private void onConfirmSignUpClick() {
        String email = this.emailField.getText();
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        LocalDate birthDate = this.birthdatePicker.getValue();

        try {
            // Check for blank fields
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || birthDate == null) {
                throw new BlankFieldException();
            }

            // Create JSON payload
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode requestPayload = objectMapper.createObjectNode();
            requestPayload.put("email", email);
            requestPayload.put("username", username);
            requestPayload.put("password", password);
            requestPayload.put("birthDate", birthDate.toString());

            // Make HTTP request to createUser endpoint
            userController.registerUser(requestPayload);
        }/* catch (DuplicatedUserException e) {
            Alert dupUserAlert = new Alert(Alert.AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Duplicated User");
            dupUserAlert.setContentText("An user with such username already exists.");
            dupUserAlert.showAndWait();
        }*/ catch (BlankFieldException e) {
            Alert blankAlert = new Alert(Alert.AlertType.ERROR);
            blankAlert.setHeaderText("ERROR: Blank field detected");
            blankAlert.setContentText("You have left at least one field empty. Please complete the whole form.");
            blankAlert.showAndWait();
        }
    }
}
