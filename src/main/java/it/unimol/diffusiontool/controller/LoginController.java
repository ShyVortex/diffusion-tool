package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.DiffusionApplication;
import it.unimol.diffusiontool.application.LoginApplication;
import it.unimol.diffusiontool.entity.User;
import it.unimol.diffusiontool.entity.UserManager;
import it.unimol.diffusiontool.exceptions.*;
import it.unimol.diffusiontool.validator.BirthdateValidator;
import it.unimol.diffusiontool.validator.EmailValidator;
import it.unimol.diffusiontool.validator.UsernameValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

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
    private UserManager userManager = UserManager.getInstance();

    public LoginController() {
    }

    @FXML
    private void onSignInClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/login-view.fxml"));
        LoginApplication loginApplication = LoginApplication.getLoginInstance();
        Parent rootNode = (Parent)fxmlLoader.load();
        loginApplication.setRootNode(rootNode);
        loginApplication.restart(rootNode);
    }

    @FXML
    private void onSignUpClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/signup-view.fxml"));
        LoginApplication loginApplication = LoginApplication.getLoginInstance();
        Parent rootNode = (Parent)fxmlLoader.load();
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
        Optional<User> user = Optional.ofNullable(this.userManager.findByUsername(username));

        Alert incorrectPassAlert;
        try {
            if (!user.isPresent()) {
                throw new UserNotFoundException("User not found: " + username);
            }

            if (!password.equals(((User)user.get()).getPassword())) {
                throw new IncorrectPasswordException();
            }

            DiffusionApplication diffusionApp = new DiffusionApplication();
            Stage stage = (Stage)this.confirmSignInButton.getScene().getWindow();
            stage.close();
            diffusionApp.init();
            diffusionApp.setUser((User)user.get());
            diffusionApp.start(new Stage());
        } catch (UserNotFoundException var6) {
            incorrectPassAlert = new Alert(AlertType.ERROR);
            incorrectPassAlert.setHeaderText("ERROR: User not found");
            incorrectPassAlert.setContentText("No user has been found with such credentials.");
            incorrectPassAlert.showAndWait();
        } catch (IncorrectPasswordException var7) {
            incorrectPassAlert = new Alert(AlertType.ERROR);
            incorrectPassAlert.setHeaderText("ERROR: Incorrect password");
            incorrectPassAlert.setContentText("Login has failed due to credentials not matching.");
            incorrectPassAlert.showAndWait();
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }

    }

    @FXML
    private void onConfirmSignUpClick() {
        String email = this.emailField.getText();
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        LocalDate birthDate = (LocalDate)this.birthdatePicker.getValue();
        Optional<User> duplicated = Optional.ofNullable(this.userManager.findByUsername(username));
        EmailValidator emailValidator = EmailValidator.getInstance();
        UsernameValidator usernameValidator = UsernameValidator.getInstance();
        BirthdateValidator birthdateValidator = BirthdateValidator.getInstance();

        Alert dupUserAlert;
        try {
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || birthDate == null) {
                throw new BlankFieldException();
            }

            if (!emailValidator.isValid(email)) {
                throw new InvalidEmailException();
            }

            if (!usernameValidator.isValid(username)) {
                throw new InvalidUsernameException();
            }

            if (!birthdateValidator.isValid(birthDate)) {
                throw new InvalidDateException();
            }

            if (duplicated.isPresent()) {
                throw new DuplicatedUserException();
            }

            User user = new User(email, username, password, birthDate);
            this.userManager.addUser(user);
            DiffusionApplication diffusionApp = new DiffusionApplication();
            Stage stage = (Stage)this.confirmSignUpButton.getScene().getWindow();
            stage.close();
            diffusionApp.init();
            diffusionApp.setUser(user);
            diffusionApp.start(new Stage());
        } catch (BlankFieldException var12) {
            dupUserAlert = new Alert(AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Blank field detected");
            dupUserAlert.setContentText("You have left at least one field empty. Please complete the whole form.");
            dupUserAlert.showAndWait();
        } catch (InvalidEmailException var13) {
            dupUserAlert = new Alert(AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Invalid Email");
            dupUserAlert.setContentText("You have inserted an invalid email. Please retry.");
            dupUserAlert.showAndWait();
        } catch (InvalidUsernameException var14) {
            dupUserAlert = new Alert(AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Invalid Username");
            dupUserAlert.setContentText("You have inserted an invalid username. Please retry");
            dupUserAlert.showAndWait();
        } catch (InvalidDateException var15) {
            dupUserAlert = new Alert(AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Invalid Birthdate");
            dupUserAlert.setContentText("You have inserted an invalid birthdate. Please retry");
            dupUserAlert.showAndWait();
        } catch (DuplicatedUserException var16) {
            dupUserAlert = new Alert(AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Duplicated User");
            dupUserAlert.setContentText("An user with such username already exists.");
            dupUserAlert.showAndWait();
        } catch (Exception var17) {
            throw new RuntimeException(var17);
        }

    }
}