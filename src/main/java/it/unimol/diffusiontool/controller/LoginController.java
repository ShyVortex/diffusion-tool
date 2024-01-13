package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.DiffusionApplication;
import it.unimol.diffusiontool.application.LoginApplication;
import it.unimol.diffusiontool.entities.User;
import it.unimol.diffusiontool.entities.UserManager;
import it.unimol.diffusiontool.exceptions.*;
import it.unimol.diffusiontool.properties.FXMLProperties;
import it.unimol.diffusiontool.validators.BirthdateValidator;
import it.unimol.diffusiontool.validators.EmailValidator;
import it.unimol.diffusiontool.validators.UsernameValidator;
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
    private final LoginApplication loginApp = LoginApplication.getInstance();
    private final UserManager userManager = LoginApplication.getUserManager();
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

    @FXML
    private void onSignInClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLProperties.getInstance().getLoginFXML().getLocation());
        Parent rootNode = fxmlLoader.load();
        loginApp.setRootNode(rootNode);
        loginApp.restart();
    }

    @FXML
    private void onSignUpClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXMLProperties.getInstance().getSignupFXML().getLocation());
        Parent rootNode = fxmlLoader.load();
        loginApp.setRootNode(rootNode);
        loginApp.restart();
    }

    @FXML
    private void onRememberMeClick() {
        loginApp.setRememberSession(true);
    }

    @FXML
    private void onConfirmSignInClick() {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        Optional<User> user = Optional.ofNullable(this.userManager.findByUsername(username));

        try {
            if (user.isEmpty())
                throw new UserNotFoundException("User not found: " + username);
            if (!password.equals((user.get()).getPassword()))
                throw new IncorrectPasswordException();

            DiffusionApplication diffusionApp = new DiffusionApplication();
            Stage stage = (Stage) this.confirmSignInButton.getScene().getWindow();
            stage.close();
            DiffusionApplication.setUser(user.get());
            diffusionApp.init();
            diffusionApp.start(new Stage());

        } catch (UserNotFoundException e) {
            Alert userMissingAlert = new Alert(AlertType.ERROR);
            userMissingAlert.setHeaderText("ERROR: User not found");
            userMissingAlert.setContentText("No user has been found with such credentials.");
            userMissingAlert.showAndWait();
        } catch (IncorrectPasswordException e) {
            Alert incorrectPassAlert = new Alert(AlertType.ERROR);
            incorrectPassAlert.setHeaderText("ERROR: Incorrect password");
            incorrectPassAlert.setContentText("Login has failed due to credentials not matching.");
            incorrectPassAlert.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onConfirmSignUpClick() {
        String email = this.emailField.getText();
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        String editorText = this.birthdatePicker.getEditor().getText();
        LocalDate birthDate = this.birthdatePicker.getValue();
        Optional<User> duplicated = Optional.ofNullable(this.userManager.findByUsername(username));
        EmailValidator emailValidator = EmailValidator.getInstance();
        UsernameValidator usernameValidator = UsernameValidator.getInstance();
        BirthdateValidator birthdateValidator = BirthdateValidator.getInstance();

        try {
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || editorText.isEmpty())
                throw new BlankFieldException();
            if (!emailValidator.isValid(email))
                throw new InvalidEmailException();
            if (!usernameValidator.isValid(username))
                throw new InvalidUsernameException();
            if (birthDate == null) {
                birthDate = LocalDate.parse(editorText);
                if (!birthdateValidator.isValid(birthDate))
                    throw new InvalidDateException();
            }
            if (!birthdateValidator.isValid(birthDate))
                throw new InvalidDateException();
            if (duplicated.isPresent())
                throw new DuplicatedUserException();


            User user = new User(email, username, password, birthDate);
            this.userManager.addUser(user);
            DiffusionApplication diffusionApp = new DiffusionApplication();
            Stage stage = (Stage) this.confirmSignUpButton.getScene().getWindow();
            stage.close();
            DiffusionApplication.setUser(user);
            diffusionApp.init();
            diffusionApp.start(new Stage());

        } catch (BlankFieldException e) {
            Alert blankFieldAlert = new Alert(AlertType.ERROR);
            blankFieldAlert.setHeaderText("ERROR: Blank field detected");
            blankFieldAlert.setContentText("You have left at least one field empty. Please complete the whole form.");
            blankFieldAlert.showAndWait();
        } catch (InvalidEmailException e) {
            Alert invEmailAlert = new Alert(AlertType.ERROR);
            invEmailAlert.setHeaderText("ERROR: Invalid Email");
            invEmailAlert.setContentText("You have inserted an invalid email. Please retry.");
            invEmailAlert.showAndWait();
        } catch (InvalidUsernameException e) {
            Alert invNameAlert = new Alert(AlertType.ERROR);
            invNameAlert.setHeaderText("ERROR: Invalid Username");
            invNameAlert.setContentText("You have inserted an invalid username. Please retry");
            invNameAlert.showAndWait();
        } catch (InvalidDateException e) {
            Alert invDateAlert = new Alert(AlertType.ERROR);
            invDateAlert.setHeaderText("ERROR: Invalid Birthdate");
            invDateAlert.setContentText("You have inserted an invalid birthdate. Please retry");
            invDateAlert.showAndWait();
        } catch (DuplicatedUserException e) {
            Alert dupUserAlert = new Alert(AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Duplicated User");
            dupUserAlert.setContentText("An user with such username already exists.");
            dupUserAlert.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}