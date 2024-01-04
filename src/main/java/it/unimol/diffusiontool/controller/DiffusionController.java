package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.DiffusionApplication;
import it.unimol.diffusiontool.application.LoginApplication;
import it.unimol.diffusiontool.entities.User;
import it.unimol.diffusiontool.entities.UserManager;
import it.unimol.diffusiontool.exceptions.*;
import it.unimol.diffusiontool.properties.FXMLProperties;
import it.unimol.diffusiontool.properties.GeneralProperties;
import it.unimol.diffusiontool.threads.StoppableThread;
import it.unimol.diffusiontool.validators.BirthdateValidator;
import it.unimol.diffusiontool.validators.EmailValidator;
import it.unimol.diffusiontool.validators.UsernameValidator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

public class DiffusionController {
    private final SimpleObjectProperty<Image> profilePicProperty = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Image> generatedImgProperty = new SimpleObjectProperty<>();
    private final DiffusionApplication diffApp = DiffusionApplication.getToolInstance();
    private String formattedDate;
    @FXML
    private Label userLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button generateButton;
    @FXML
    private Button upscaleButton;
    @FXML
    private Label genImgsLabel;
    @FXML
    private Label upscImgsLabel;
    @FXML
    private ImageView homeUserImage;
    @FXML
    private ImageView profileUserImage;
    @FXML
    private TextField usernameField;
    @FXML
    private Button usernameChangeButton;
    @FXML
    private Button usernameCancelButton;
    @FXML
    private Button usernameApplyButton;
    @FXML
    private TextField emailField;
    @FXML
    private Button emailChangeButton;
    @FXML
    private Button emailCancelButton;
    @FXML
    private Button emailApplyButton;
    @FXML
    private TextField birthdateField;
    @FXML
    private DatePicker birthdatePicker;
    @FXML
    private Button birthdateChangeButton;
    @FXML
    private Button birthdateCancelButton;
    @FXML
    private Button birthdateApplyButton;
    @FXML
    private TextField passwordField;
    @FXML
    private Button passwordChangeButton;
    @FXML
    private Button passwordCancelButton;
    @FXML
    private Button passwordApplyButton;
    @FXML
    private Button pictureDefaultButton;
    @FXML
    private Button pictureChangeButton;
    @FXML
    private Button profileDeleteButton;
    @FXML
    private TextArea promptArea;
    @FXML
    private TextField tagsField;
    @FXML
    private Button promptResetButton;
    @FXML
    private Button createButton;
    @FXML
    private Label processingLabel;
    @FXML
    private ImageView genImgPreview;
    @FXML
    private Button imageDeleteButton;
    @FXML
    private Button imageShowButton;

    private String getLoggedInUser() {
        return diffApp.getUser().getUsername();
    }

    private String getUserEmail() {
        return diffApp.getUser().getEmail();
    }

    private String getUserBirthdate() {
        return diffApp.getUser().getBirthDate().toString();
    }

    private String getUserPassword() {
        return diffApp.getUser().getPassword();
    }

    private String countGeneratedImgs() {
        User user = diffApp.getUser();
        int num = user.countGeneratedImgs();
        String startText = "You have generated ";
        String endText = " images so far.";

        return startText + num + endText;
    }

    private String countUpscaledImgs() {
        User user = diffApp.getUser();
        String startText = "You have upscaled ";
        String endText = " images so far.";

        return startText + user.getUpscImgsNum() + endText;
    }

    private String updateProcessingLabel() {
        return "Preview";
    }

    private boolean isImageFile(File file) {
        try {
            // Attempt to load the file as an image
            Image image = new Image(file.toURI().toString());
            return !image.isError(); // If loading succeeds, it's an image
        } catch (Exception e) {
            return false; // If loading fails, it's not an image
        }
    }

    private String callPythonGenerate(String prompt, String date) throws IOException, GenerationException {
        String activateScriptPath = "venv/bin/python";
        String pythonScriptPath = "src/main/python/it/unimol/diffusiontool/generate.py";
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();

        // Construct the command to execute
        List<String> command = List.of (
                activateScriptPath,      // Activate virtual environment
                pythonScriptPath,        // Path to the Python script
                prompt,                  // Prompt argument
                date                    // Date argument
        );

        // Start the process
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        // Capture and print the error stream
        try (
                InputStream inputStream = process.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader inputbufferedReader = new BufferedReader(inputStreamReader);
                InputStream errorStream = process.getErrorStream();
                InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
                BufferedReader errorBufferedReader = new BufferedReader(errorStreamReader)
        ) {
            String inputLine;
            while ((inputLine = inputbufferedReader.readLine()) != null) {
                output.append(inputLine).append("\n");
            }

            String errorLine;
            while ((errorLine = errorBufferedReader.readLine()) != null) {
                errorOutput.append(errorLine).append("\n");
            }
        }

        // Wait for the process to finish
        try {
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

            if (exitCode == 0) {
                // If there is no error, return the output as a String
            /*
                String absolutePath = output.toString();
                String relativePath = absolutePath.replace(System.getProperty("user.dir") + "/", "");
            */
                System.out.println(errorOutput.toString().trim());
                return output.toString().trim();
            } else {
                // If there is an error, print the error and throw an exception
                System.err.println("Error output:\n" + errorOutput.toString().trim());
                throw new GenerationException();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return GeneralProperties.ERROR.getValue();
    }

    @FXML
    private void initialize() {
        userLabel.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
        if (diffApp.getCurrentFXML().equals(FXMLProperties.getInstance().getHomeFXML()))
            initializeHomeView();
        if (diffApp.getCurrentFXML().equals(FXMLProperties.getInstance().getProfileFXML()))
            initializeProfileView();
        if (diffApp.getCurrentFXML().equals(FXMLProperties.getInstance().getGenerateFXML()))
            initializeGenerateView();
    }

    private void initializeHomeView() {
        profileButton.setBackground(null);
        genImgsLabel.textProperty().bind(Bindings.createStringBinding(this::countGeneratedImgs));
        upscImgsLabel.textProperty().bind(Bindings.createStringBinding(this::countUpscaledImgs));
        profilePicProperty.set(diffApp.getUser().getProfilePic());
        homeUserImage.imageProperty().bind(profilePicProperty);
    }

    private void initializeProfileView() {
        profilePicProperty.set(diffApp.getUser().getProfilePic());
        profileUserImage.imageProperty().bind(profilePicProperty);
        usernameField.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
        emailField.textProperty().bind(Bindings.createStringBinding(this::getUserEmail));
        birthdateField.textProperty().bind(Bindings.createStringBinding(this::getUserBirthdate));
        passwordField.textProperty().bind(Bindings.createStringBinding(this::getUserPassword));
    }

    private void initializeGenerateView() {
        profileButton.setBackground(null);
        profilePicProperty.set(diffApp.getUser().getProfilePic());
        homeUserImage.imageProperty().bind(profilePicProperty);
    }

    @FXML
    private void OnLogOutClick() throws Exception {
        diffApp.setUser(null);
        LoginApplication loginApplication = LoginApplication.getLoginInstance();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        loginApplication.init();
        loginApplication.start(new Stage());
    }

    @FXML
    private void OnProfileClick() throws IOException {
        diffApp.setCurrentFXML(new FXMLLoader(FXMLProperties.getInstance().getProfileFXML().getLocation()));
        Parent rootNode = diffApp.getCurrentFXML().load();
        diffApp.setRootNode(rootNode);
        diffApp.restart();
    }

    @FXML
    private void OnHomeClick() throws IOException {
        diffApp.setCurrentFXML(new FXMLLoader(FXMLProperties.getInstance().getHomeFXML().getLocation()));
        Parent rootNode = diffApp.getCurrentFXML().load();
        diffApp.setRootNode(rootNode);
        diffApp.restart();
    }

    @FXML
    private void OnGenerateClick() throws IOException {
        diffApp.setCurrentFXML(new FXMLLoader(FXMLProperties.getInstance().getGenerateFXML().getLocation()));
        Parent rootNode = diffApp.getCurrentFXML().load();
        diffApp.setRootNode(rootNode);
        diffApp.restart();
    }

    @FXML
    private void OnUpscaleClick() {

    }

    @FXML
    private void OnUsernameChangeClick() {
        usernameField.textProperty().unbind();
        usernameField.setEditable(true);
        usernameCancelButton.setVisible(true);
        usernameApplyButton.setVisible(true);
    }

    @FXML
    private void OnUsernameCancelClick() {
        usernameField.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
        usernameField.setEditable(false);
        usernameCancelButton.setVisible(false);
        usernameApplyButton.setVisible(false);
    }

    @FXML
    private void OnUsernameApplyClick() {
        User user = diffApp.getUser();
        String newUsername = usernameField.getText();

        try {
            if (user.getUsername().equals(newUsername))
                throw new InvalidUsernameException();
            if (!UserManager.getInstance().exists(newUsername)) {
                if (UsernameValidator.getInstance().isValid(newUsername))
                    user.setUsername(newUsername);
                else
                    throw new InvalidUsernameException();
            }
            else
                throw new DuplicatedUserException();

            userLabel.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
            usernameField.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
            usernameField.setEditable(false);
            usernameCancelButton.setVisible(false);
            usernameApplyButton.setVisible(false);

        } catch (InvalidUsernameException e) {
            Alert invNameAlert = new Alert(Alert.AlertType.ERROR);
            invNameAlert.setHeaderText("ERROR: Invalid Username");
            invNameAlert.setContentText("You have inserted an invalid username. Please retry");
            invNameAlert.showAndWait();
        } catch (DuplicatedUserException e) {
            Alert dupUserAlert = new Alert(Alert.AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Duplicated User");
            dupUserAlert.setContentText("An user with such username already exists.");
            dupUserAlert.showAndWait();
        }
    }

    @FXML
    private void OnEmailChangeClick() {
        emailField.textProperty().unbind();
        emailField.setEditable(true);
        emailCancelButton.setVisible(true);
        emailApplyButton.setVisible(true);
    }

    @FXML
    private void OnEmailCancelClick() {
        emailField.textProperty().bind(Bindings.createStringBinding(this::getUserEmail));
        emailField.setEditable(false);
        emailCancelButton.setVisible(false);
        emailApplyButton.setVisible(false);
    }

    @FXML
    private void OnEmailApplyClick() {
        User user = diffApp.getUser();
        String newEmail = emailField.getText();

        try {
            if (user.getEmail().equals(newEmail))
                throw new InvalidEmailException();
            if (!UserManager.getInstance().existsByEmail(newEmail)) {
                if (EmailValidator.getInstance().isValid(newEmail))
                    user.setEmail(newEmail);
                else
                    throw new InvalidEmailException();
            }
            else
                throw new DuplicatedUserException();

            emailField.textProperty().bind(Bindings.createStringBinding(this::getUserEmail));
            emailField.setEditable(false);
            emailCancelButton.setVisible(false);
            emailApplyButton.setVisible(false);

        } catch (InvalidEmailException e) {
            Alert invEmailAlert = new Alert(Alert.AlertType.ERROR);
            invEmailAlert.setHeaderText("ERROR: Invalid Email");
            invEmailAlert.setContentText("You have inserted an invalid email. Please retry.");
            invEmailAlert.showAndWait();
        } catch (DuplicatedUserException e) {
            Alert dupUserAlert = new Alert(Alert.AlertType.ERROR);
            dupUserAlert.setHeaderText("ERROR: Duplicated User");
            dupUserAlert.setContentText("An user with such username already exists.");
            dupUserAlert.showAndWait();
        }
    }

    @FXML
    private void OnBirthdateChangeClick() {
        birthdateField.textProperty().unbind();
        birthdateField.setEditable(true);
        birthdatePicker.setVisible(true);
        birthdateCancelButton.setVisible(true);
        birthdateApplyButton.setVisible(true);
    }

    @FXML
    private void OnBirthdateCancelClick() {
        birthdateField.textProperty().bind(Bindings.createStringBinding(this::getUserBirthdate));
        birthdateField.setEditable(false);
        birthdatePicker.setVisible(false);
        birthdateCancelButton.setVisible(false);
        birthdateApplyButton.setVisible(false);
    }

    @FXML
    private void OnBirthdateApplyClick() {
        User user = diffApp.getUser();
        LocalDate newBirthdate = birthdatePicker.getValue();

        try {
            if (user.getBirthDate().equals(newBirthdate))
                throw new InvalidDateException();
            if (BirthdateValidator.getInstance().isValid(newBirthdate))
                user.setBirthDate(newBirthdate);
            else
                throw new InvalidDateException();

            birthdateField.textProperty().bind(Bindings.createStringBinding(this::getUserBirthdate));
            birthdateField.setEditable(false);
            birthdatePicker.setVisible(false);
            birthdateCancelButton.setVisible(false);
            birthdateApplyButton.setVisible(false);

        } catch (InvalidDateException e) {
            Alert invDateAlert = new Alert(Alert.AlertType.ERROR);
            invDateAlert.setHeaderText("ERROR: Invalid Birthdate");
            invDateAlert.setContentText("You have inserted an invalid birthdate. Please retry");
            invDateAlert.showAndWait();
        }
    }

    @FXML
    private void OnPasswordChangeClick() {
        passwordField.textProperty().unbind();
        passwordField.setEditable(true);
        passwordCancelButton.setVisible(true);
        passwordApplyButton.setVisible(true);
    }

    @FXML
    private void OnPasswordCancelClick() {
        passwordField.textProperty().bind(Bindings.createStringBinding(this::getUserPassword));
        passwordField.setEditable(false);
        passwordCancelButton.setVisible(false);
        passwordApplyButton.setVisible(false);
    }

    @FXML
    private void OnPasswordApplyClick() {
        User user = diffApp.getUser();
        String newPassword = passwordField.getText();

        try {
            if (user.getPassword().equals(newPassword))
                throw new InvalidPasswordException();

            user.setPassword(newPassword);
            passwordField.textProperty().bind(Bindings.createStringBinding(this::getUserPassword));
            passwordField.setEditable(false);
            passwordCancelButton.setVisible(false);
            passwordApplyButton.setVisible(false);

        } catch (InvalidPasswordException e) {
            Alert invPassAlert = new Alert(Alert.AlertType.ERROR);
            invPassAlert.setHeaderText("ERROR: Invalid Password");
            invPassAlert.setContentText("Your new password is the same as the old one. Please retry.");
            invPassAlert.showAndWait();
        }
    }

    @FXML
    private void OnPictureDefaultClick() {
        User user = diffApp.getUser();
        profileUserImage.imageProperty().unbind();
        Image defaultPic = new Image("/default/new-user.png");
        user.setProfilePic(defaultPic);
        profilePicProperty.set(diffApp.getUser().getProfilePic());
        profileUserImage.imageProperty().bind(profilePicProperty);
    }

    @FXML
    private void OnPictureChangeClick() {
        User user = diffApp.getUser();
        profileUserImage.imageProperty().unbind();

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select your new profile picture");
            File file = fileChooser.showOpenDialog(new Stage());
            // Set a filter to only allow image files
            FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files",
                    "*.png", "*.jpg", "*.jpeg", "*.gif");
            fileChooser.getExtensionFilters().add(imageFilter);

            if (file != null) {
                // Double check to prevent errors
                if (isImageFile(file)) {
                    Image newPfp = new Image(file.toURI().toString());
                    user.setProfilePic(newPfp);
                }
                else
                    throw new InvalidObjectException("Not an image");
            }

            profilePicProperty.set(diffApp.getUser().getProfilePic());
            profileUserImage.imageProperty().bind(profilePicProperty);

        } catch (InvalidObjectException e) {
            Alert invObjAlert = new Alert(Alert.AlertType.ERROR);
            invObjAlert.setHeaderText("ERROR: Not an Image");
            invObjAlert.setContentText("The selected file is not an image. Please retry.");
            invObjAlert.showAndWait();
        }
    }

    @FXML
    private void OnProfileDeleteClick() throws Exception {
        Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
        warning.setHeaderText("WARNING: Are you sure?");
        warning.setContentText("Your profile and all its data will be deleted. Do you wish to continue?");
        Optional<ButtonType> result = warning.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            User user = diffApp.getUser();
            UserManager userManager = UserManager.getInstance();
            userManager.deleteByUsername(user.getUsername());
            User.free(user);
            OnLogOutClick();
        }
    }

    @FXML
    private void OnPromptResetClick() {
        promptArea.setText("");
        tagsField.setText("");
    }

    @FXML
    private void OnCreateClick() {
        String prompt = promptArea.getText();
        String tags = tagsField.getText();
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        formattedDate = currentDate.format(formatter);

        StoppableThread processingThread = new StoppableThread(() -> processingLabel.setVisible(true));
        processingThread.start(processingThread);

        try {
            processingThread.stop(processingThread);
            processingLabel.setVisible(true);
            AtomicReference<String> base64EncodedImage = new AtomicReference<>();
            StoppableThread pyThread = new StoppableThread(() -> {
                try {
                    base64EncodedImage.set(callPythonGenerate(prompt, formattedDate));
                    StoppableThread.currentThread().stop(StoppableThread.currentThread());
                } catch (IOException | GenerationException e) {
                    throw new RuntimeException(e);
                }
            });
            pyThread.start(pyThread);

            StoppableThread updateThread = new StoppableThread(() -> {
                while (true) {
                    try {
                        sleep(1000);
                        if (!pyThread.isAlive() && base64EncodedImage.toString() != null) {
                            Platform.runLater(() -> {
                                processingLabel.textProperty().bind(Bindings.createStringBinding(
                                        DiffusionController.this::updateProcessingLabel));
                                byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedImage.get());
                                ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                                Image outImage;
                                try {
                                    outImage = SwingFXUtils.toFXImage(ImageIO.read(bis), null);
                                    generatedImgProperty.set(outImage);
                                    genImgPreview.imageProperty().bind(generatedImgProperty);
                                    imageDeleteButton.setVisible(true);
                                    imageShowButton.setVisible(true);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            break;
                        }
                    } catch (InterruptedException e) {
                        processingLabel.setVisible(false);
                        throw new RuntimeException(e);
                    }
                }
            });
            updateThread.start(updateThread);

        } catch (Exception e) {
            Alert genAlert = new Alert(Alert.AlertType.ERROR);
            genAlert.setHeaderText("ERROR: Generation Failure");
            genAlert.setContentText("Something went wrong in the image creation. Please retry");
            genAlert.showAndWait();
            processingLabel.setVisible(false);
        }
    }

    @FXML
    private void OnImageDeleteClick() {

    }

    @FXML
    private void OnImageShowClick() {
        
    }
}
