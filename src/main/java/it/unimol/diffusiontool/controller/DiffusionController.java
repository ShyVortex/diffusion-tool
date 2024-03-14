package it.unimol.diffusiontool.controller;

import it.unimol.diffusiontool.application.DiffusionApplication;
import it.unimol.diffusiontool.application.LoginApplication;
import it.unimol.diffusiontool.application.ViewerApplication;
import it.unimol.diffusiontool.entities.User;
import it.unimol.diffusiontool.entities.UserManager;
import it.unimol.diffusiontool.exceptions.*;
import it.unimol.diffusiontool.interfaces.Pythonable;
import it.unimol.diffusiontool.properties.FXMLProperties;
import it.unimol.diffusiontool.threads.StoppableThread;
import it.unimol.diffusiontool.validators.BirthdateValidator;
import it.unimol.diffusiontool.validators.EmailValidator;
import it.unimol.diffusiontool.validators.UsernameValidator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static it.unimol.diffusiontool.properties.GeneralProperties.*;

public class DiffusionController implements Pythonable {
    private final DiffusionApplication diffApp = DiffusionApplication.getToolInstance();
    private final ViewerApplication viewerApp = ViewerApplication.getInstance();
    private final SimpleObjectProperty<Image> profilePicProperty = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Image> generatedImgProperty = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Image> firstUpsImgProperty = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Image> secondUpsImgProperty = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Image> thirdUpsImgProperty = new SimpleObjectProperty<>();
    private final List<Image> upscaledImages = new ArrayList<>(3);
    private final UserManager userManager = LoginApplication.getUserManager();
    private int activeImages;
    private int mutex;
    private int pythonCalledBy;
    private boolean includeUpscaling;
    private String formattedDate;
    private Image lastGeneratedImage;
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
    private ComboBox<String> styleComboBox;
    @FXML
    private CheckBox upscaleCheckBox;
    @FXML
    private Label processingLabel;
    @FXML
    private ImageView genImgPreview;
    @FXML
    private Button imageDeleteButton;
    @FXML
    private Button imageShowButton;
    @FXML
    private Pane firstImagePane;
    @FXML
    private ImageView firstImgView;
    @FXML
    private TextArea firstTextArea;
    @FXML
    private Button firstStartButton;
    @FXML
    private Button firstDeleteButton;
    @FXML
    private Label firstProcessLabel;
    @FXML
    private Button firstShowButton;
    @FXML
    private Pane secondImagePane;
    @FXML
    private ImageView secondImgView;
    @FXML
    private TextArea secondTextArea;
    @FXML
    private Button secondStartButton;
    @FXML
    private Button secondDeleteButton;
    @FXML
    private Label secondProcessLabel;
    @FXML
    private Button secondShowButton;
    @FXML
    private Pane thirdImagePane;
    @FXML
    private ImageView thirdImgView;
    @FXML
    private TextArea thirdTextArea;
    @FXML
    private Button thirdStartButton;
    @FXML
    private Button thirdDeleteButton;
    @FXML
    private Label thirdProcessLabel;
    @FXML
    private Button thirdShowButton;

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

    @FXML
    private void initialize() {
        pythonCalledBy = NULL.getValue();
        userLabel.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));

        if (diffApp.getCurrentFXML().equals(FXMLProperties.getInstance().getHomeFXML()))
            initHomeView();
        if (diffApp.getCurrentFXML().equals(FXMLProperties.getInstance().getProfileFXML()))
            initProfileView();
        if (diffApp.getCurrentFXML().equals(FXMLProperties.getInstance().getGenerateFXML()))
            initGenerateView();
        if (diffApp.getCurrentFXML().equals(FXMLProperties.getInstance().getUpscaleFXML()))
            initUpscaleView();
    }

    @FXML
    private void initHomeView() {
        Platform.runLater(() -> {
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setTitle("Home");
        });
        profileButton.setBackground(null);
        genImgsLabel.textProperty().bind(Bindings.createStringBinding(this::countGeneratedImgs));
        upscImgsLabel.textProperty().bind(Bindings.createStringBinding(this::countUpscaledImgs));
        profilePicProperty.set(diffApp.getUser().getProfilePic());
        homeUserImage.imageProperty().bind(profilePicProperty);
    }

    @FXML
    private void initProfileView() {
        Platform.runLater(() -> {
            Stage stage = (Stage) profileUserImage.getScene().getWindow();
            stage.setTitle("Your Profile");
        });
        profilePicProperty.set(diffApp.getUser().getProfilePic());
        profileUserImage.imageProperty().bind(profilePicProperty);
        usernameField.textProperty().bind(Bindings.createStringBinding(this::getLoggedInUser));
        emailField.textProperty().bind(Bindings.createStringBinding(this::getUserEmail));
        birthdateField.textProperty().bind(Bindings.createStringBinding(this::getUserBirthdate));
        passwordField.textProperty().bind(Bindings.createStringBinding(this::getUserPassword));
    }

    @FXML
    private void initGenerateView() {
        Platform.runLater(() -> {
            Stage stage = (Stage) generateButton.getScene().getWindow();
            stage.setTitle("Image Generator");
        });
        profileButton.setBackground(null);
        profilePicProperty.set(diffApp.getUser().getProfilePic());
        homeUserImage.imageProperty().bind(profilePicProperty);
        styleComboBox.getItems().addAll(
                "General",
                "Pixel Art"
        );
        styleComboBox.setPromptText(styleComboBox.getItems().get(0));
        styleComboBox.setValue("General");
    }

    @FXML
    private void initUpscaleView() {
        Platform.runLater(() -> {
            Stage stage = (Stage) upscaleButton.getScene().getWindow();
            stage.setTitle("Image Upscaler");
        });
        activeImages = 0;
        mutex = AVAILABLE.getValue();

        initImagesList();
        profileButton.setBackground(null);
        profilePicProperty.set(diffApp.getUser().getProfilePic());
        homeUserImage.imageProperty().bind(profilePicProperty);
    }

    private void initImagesList() {
        for (int i = 0; i < MAX_CAPACITY.getValue(); i++)
            upscaledImages.add(null);
    }

    @FXML
    private void OnLogOutClick() throws Exception {
        DiffusionApplication.setUser(null);
        deleteSessionData();
        LoginApplication loginApplication = LoginApplication.getInstance();
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
    private void OnUpscaleClick() throws IOException {
        diffApp.setCurrentFXML(new FXMLLoader(FXMLProperties.getInstance().getUpscaleFXML().getLocation()));
        Parent rootNode = diffApp.getCurrentFXML().load();
        diffApp.setRootNode(rootNode);
        diffApp.restart();
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
            if (userManager.exists(newUsername)) {
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
            if (!userManager.existsByEmail(newEmail)) {
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
        String editorText = birthdatePicker.getEditor().getText();
        LocalDate newBirthdate = birthdatePicker.getValue();

        try {
            if (editorText.isEmpty())
                throw new BlankFieldException();
            if (user.getBirthDate().equals(newBirthdate))
                throw new InvalidDateException();
            if (newBirthdate == null) {
                newBirthdate = LocalDate.parse(editorText);
                if (!BirthdateValidator.getInstance().isValid(newBirthdate))
                    throw new InvalidDateException();
            }
            if (BirthdateValidator.getInstance().isValid(newBirthdate))
                user.setBirthDate(newBirthdate);
            else
                throw new InvalidDateException();

            birthdateField.textProperty().bind(Bindings.createStringBinding(this::getUserBirthdate));
            birthdateField.setEditable(false);
            birthdatePicker.setVisible(false);
            birthdateCancelButton.setVisible(false);
            birthdateApplyButton.setVisible(false);

        } catch (BlankFieldException e) {
            Alert blankFieldAlert = new Alert(Alert.AlertType.ERROR);
            blankFieldAlert.setHeaderText("ERROR: Blank field detected");
            blankFieldAlert.setContentText("You have left the date field empty. Please insert one.");
            blankFieldAlert.showAndWait();
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
            userManager.deleteByUsername(user.getUsername());
            User.free(user);
            deleteSessionData();
            OnLogOutClick();
        }
    }

    @FXML
    private void OnStyleSelect() {
        if (styleComboBox.getValue().equals("General"))
            upscaleCheckBox.setVisible(true);
        else
            upscaleCheckBox.setVisible(false);
    }

    @FXML
    private void OnCheckBoxClick() {
        // if checkbox is selected, includeUpscaling = true, false otherwise
        includeUpscaling = upscaleCheckBox.isSelected();
    }

    @FXML
    private void OnPromptResetClick() {
        promptArea.setText("");
        tagsField.setText("");
    }

    @FXML
    private void OnCreateClick() {
        StoppableThread deleteThread = new StoppableThread(() -> {
            if (processingLabel.getText().equals("Preview") && genImgPreview.isVisible()) {
                Platform.runLater(() -> {
                    processingLabel.textProperty().unbind();
                    processingLabel.setText("Processing...");
                    processingLabel.setVisible(false);
                    genImgPreview.imageProperty().unbind();
                    genImgPreview.setVisible(false);
                    imageDeleteButton.setVisible(false);
                    imageShowButton.setVisible(false);
                });
            }
        });
        deleteThread.start();

        String prompt = promptArea.getText();
        String tags = tagsField.getText();

        try {
            if (prompt.isEmpty())
                throw new BlankFieldException();

            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            formattedDate = currentDate.format(formatter);

            StoppableThread processingThread = new StoppableThread(() -> {
                try {
                    deleteThread.join();
                    Platform.runLater(() -> processingLabel.setVisible(true));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            processingThread.start();

            try {
                processingThread.stop(processingThread);
                pythonCalledBy = GENERATE.getValue();
                AtomicReference<String> base64EncodedImage = new AtomicReference<>();
                StoppableThread pyThread = new StoppableThread(() -> {
                    try {
                        processingThread.join();
                        base64EncodedImage.set(callPyScript(prompt, tags, formattedDate));
                        StoppableThread.currentThread().stop(StoppableThread.currentThread());
                    } catch (IOException | GenerationException e) {
                        e.printStackTrace();
                        throwGenericAlert();
                        pythonCalledBy = NULL.getValue();
                        processingLabel.setVisible(false);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                pyThread.start();

                StoppableThread updateThread = new StoppableThread(() -> {
                    while (true) {
                        try {
                            pyThread.join();
                            if (base64EncodedImage.get() != null) {
                                // join method above assumes pyThread is terminated already
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
                                        genImgPreview.setVisible(true);
                                        lastGeneratedImage = outImage;
                                        diffApp.getUser().incGeneratedImages();
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
                updateThread.start();

            } catch (Exception e) {
                e.printStackTrace();
                throwGenericAlert();
                pythonCalledBy = NULL.getValue();
                processingLabel.setVisible(false);
            }

        } catch (BlankFieldException e) {
            Alert blankFieldAlert = new Alert(Alert.AlertType.ERROR);
            blankFieldAlert.setHeaderText("ERROR: Blank field detected");
            blankFieldAlert.setContentText("You have left the prompt field empty. Please insert it.");
            blankFieldAlert.showAndWait();
        }
    }

    @FXML
    private void OnGenerateDeleteClick() throws IOException {
        File imageFile = new File(String.valueOf(lastGeneratedImage));
        boolean isDeleted = imageFile.delete();

        try {
            if (isDeleted) {
                processingLabel.textProperty().unbind();
                processingLabel.setVisible(false);
                genImgPreview.imageProperty().unbind();
                genImgPreview.setVisible(false);
                imageDeleteButton.setVisible(false);
                imageShowButton.setVisible(false);
                OnPromptResetClick();
            } else
                throw new FileNotFoundException();

        } catch (FileNotFoundException e) {
            Alert FNFAlert = new Alert(Alert.AlertType.ERROR);
            FNFAlert.setHeaderText("ERROR: File not found");
            FNFAlert.setContentText("An error has occurred and the image file can't be found. \n" +
                    "You can search it in the folder 'result/generated/' and delete that for yourself.");
            FNFAlert.showAndWait();
            OnGenerateClick();
        }
    }

    @FXML
    private void OnGenerateShowClick() throws Exception {
        viewerApp.setGenerated(true);
        viewerApp.setUpscaled(includeUpscaling);
        viewerApp.setExportedImage(lastGeneratedImage);
        viewerApp.init();
        viewerApp.start(new Stage());
    }

    @FXML
    private void OnSelectClick() {
        try {
            if (activeImages < MAX_CAPACITY.getValue()) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select an image");
                    File file = fileChooser.showOpenDialog(new Stage());
                    FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files",
                            "*.png", "*.jpg", "*.jpeg", "*.gif");
                    fileChooser.getExtensionFilters().add(imageFilter);

                    if (file != null) {
                        if (isImageFile(file)) {
                            double size = getFileSize(file);
                            if (size <= 5120) {
                                activeImages++;
                                Image image = new Image(file.toURI().toString());
                                String name = file.getName();
                                int isSpaceAvailable = checkAvailableSpace();

                                switch (isSpaceAvailable) {
                                    case 0:
                                        firstImagePane.setVisible(true);
                                        firstUpsImgProperty.set(image);
                                        firstTextArea.textProperty().bind(Bindings.createStringBinding(
                                                () -> getImageProperties(name, image, size)));
                                        firstImgView.imageProperty().bind(firstUpsImgProperty);
                                        break;

                                    case 1:
                                        secondImagePane.setVisible(true);
                                        secondUpsImgProperty.set(image);
                                        secondTextArea.textProperty().bind(Bindings.createStringBinding(
                                                () -> getImageProperties(name, image, size)));
                                        secondImgView.imageProperty().bind(secondUpsImgProperty);
                                        break;

                                    case 2:
                                        thirdImagePane.setVisible(true);
                                        thirdUpsImgProperty.set(image);
                                        thirdTextArea.textProperty().bind(Bindings.createStringBinding(
                                                () -> getImageProperties(name, image, size)));
                                        thirdImgView.imageProperty().bind(thirdUpsImgProperty);
                                        break;

                                    case 3:
                                        throw new MaxCapacityException();
                                }

                            } else
                                throw new SizeLimitException();

                        } else
                            throw new InvalidObjectException("Not an image");
                    }

                } catch (InvalidObjectException e) {
                    Alert invObjAlert = new Alert(Alert.AlertType.ERROR);
                    invObjAlert.setHeaderText("ERROR: Not an Image");
                    invObjAlert.setContentText("The selected file is not an image. Please retry.");
                    invObjAlert.showAndWait();
                } catch (SizeLimitException e) {
                    Alert invObjAlert = new Alert(Alert.AlertType.ERROR);
                    invObjAlert.setHeaderText("ERROR: File Size Exceeded");
                    invObjAlert.setContentText("The selected file is too big. Please compress it.");
                    invObjAlert.showAndWait();
                } catch (FileNotFoundException ignored) {
                    // If the file is null no image is selected, so we can just skip
                }

            } else
                throw new MaxCapacityException();

        } catch (MaxCapacityException e) {
            Alert mcAlert = new Alert(Alert.AlertType.ERROR);
            mcAlert.setHeaderText("ERROR: Max Capacity Reached");
            mcAlert.setContentText("As stated before, you can't load more than three images.");
            mcAlert.showAndWait();
        }
    }

    @FXML
    private void OnUpscaleStartClick(ActionEvent event) {
        try {
            if (mutex == AVAILABLE.getValue()) {
                mutex = BUSY.getValue();
                Button clickedButton = (Button) event.getSource();
                Button validButton;
                Label validLabel;
                Image validImage;
                int index;

                if (clickedButton.equals(firstStartButton)) {
                    validButton = firstShowButton;
                    validLabel = firstProcessLabel;
                    validImage = firstImgView.getImage();
                    index = 0;
                }
                else if (clickedButton.equals(secondStartButton)) {
                    validButton = secondShowButton;
                    validLabel = secondProcessLabel;
                    validImage = secondImgView.getImage();
                    index = 1;
                }
                else if (clickedButton.equals(thirdStartButton)) {
                    validButton = thirdShowButton;
                    validLabel = thirdProcessLabel;
                    validImage = thirdImgView.getImage();
                    index = 2;
                } else {
                    validButton = null;
                    validLabel = null;
                    validImage = null;
                    index = -1;
                }

                // These are pointers, each will point to one existing object
                assert validButton != null;
                assert validLabel != null;
                assert validImage != null;

                // this image has already been upscaled
                if (upscaledImages.get(index) != null)
                    throw new DuplicatedActionException();

                String imgPath = getImagePath(validImage);

                StoppableThread processingThread = new StoppableThread(() -> {
                    validLabel.setVisible(true);
                    disableAllButtons();
                    StoppableThread.currentThread().stop(StoppableThread.currentThread());
                });
                processingThread.start();

                // boolean variables for exception handling
                AtomicBoolean isAlertShown = new AtomicBoolean();
                AtomicBoolean isOutOfMemory = new AtomicBoolean();

                try {
                    pythonCalledBy = UPSCALE.getValue();
                    AtomicReference<String> base64EncodedImage = new AtomicReference<>();
                    StoppableThread pyThread = new StoppableThread(() -> {
                        try {
                            processingThread.join();
                            while (!isOutOfMemory.get() && !isAlertShown.get()) {
                                try {
                                    if (!processingThread.isAlive()) {
                                        base64EncodedImage.set(callPyScript(imgPath));
                                        if (base64EncodedImage.get().equals("OUT OF MEMORY")) {
                                            isOutOfMemory.set(true);
                                            throw new OutOfMemoryError();
                                        }
                                        StoppableThread.currentThread().stop(StoppableThread.currentThread());
                                        break;
                                    }
                                } catch (IOException | UpscalingException e) {
                                    isAlertShown.set(true);
                                    Platform.runLater(() -> {
                                        e.printStackTrace();
                                        throwGenericAlert();
                                        validLabel.setVisible(false);
                                        pythonCalledBy = NULL.getValue();
                                        enableAllButtons();
                                        mutex = AVAILABLE.getValue();
                                    });
                                } catch (OutOfMemoryError e) {
                                    Platform.runLater(() -> {
                                        Alert memAlert = new Alert(Alert.AlertType.ERROR);
                                        memAlert.setHeaderText("ERROR: Out of Memory");
                                        memAlert.setContentText(
                                                "The app has tried to allocate more VRAM than what was available." +
                                                " Make sure your system meets minimum requirements and that the image" +
                                                " you're trying to upscale isn't too detailed or already at an high" +
                                                " resolution"
                                        );
                                        memAlert.showAndWait();
                                        validLabel.setVisible(false);
                                        pythonCalledBy = NULL.getValue();
                                        enableAllButtons();
                                        mutex = AVAILABLE.getValue();
                                    });
                                }
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    pyThread.start();

                    StoppableThread updateThread = new StoppableThread(() -> {
                        while (true) {
                            try {
                                pyThread.join();
                                if (base64EncodedImage.get() != null && !isOutOfMemory.get()) {
                                    Platform.runLater(() -> {
                                        validLabel.setVisible(false);
                                        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedImage.get());
                                        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
                                        Image outImage;
                                        try {
                                            outImage = SwingFXUtils.toFXImage(ImageIO.read(bis), null);
                                            validButton.setVisible(true);
                                            diffApp.getUser().incUpscaledImages();
                                            upscaledImages.set(index, outImage);
                                            enableAllButtons();
                                            mutex = AVAILABLE.getValue();
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
                    updateThread.start();

                } catch (Exception e) {
                    if (!isAlertShown.get()) {
                        e.printStackTrace();
                        throwGenericAlert();
                        validLabel.setVisible(false);
                        pythonCalledBy = NULL.getValue();
                        enableAllButtons();
                        mutex = AVAILABLE.getValue();
                    }
                }

            } else
                throw new BusyMutexException();

        } catch (BusyMutexException e) {
            Alert bmAlert = new Alert(Alert.AlertType.ERROR);
            bmAlert.setHeaderText("ERROR: Busy Mutex");
            bmAlert.setContentText("You can only upscale one image at once. Please wait for the process to end.");
            bmAlert.showAndWait();
        } catch (DuplicatedActionException e) {
            Alert daAlert = new Alert(Alert.AlertType.ERROR);
            daAlert.setHeaderText("ERROR: Duplicated Action");
            daAlert.setContentText("This image has already been upscaled. If you wish to repeat the process, please" +
                    " delete it first.");
            daAlert.showAndWait();
            mutex = AVAILABLE.getValue();
        }
    }

    @FXML
    private void OnUpscaleDeleteClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        if (clickedButton.equals(firstDeleteButton)) {
            if (upscaledImages.get(0) != null) {
                upscaledImages.set(0, null);
                firstShowButton.setVisible(false);
            }
            firstTextArea.textProperty().unbind();
            firstTextArea.textProperty().set("");
            firstImgView.imageProperty().unbind();
            firstImagePane.setVisible(false);
            activeImages--;
        } else if (clickedButton.equals(secondDeleteButton)) {
            if (upscaledImages.get(1) != null) {
                upscaledImages.set(1, null);
                secondShowButton.setVisible(false);
            }
            secondTextArea.textProperty().unbind();
            secondTextArea.textProperty().set("");
            secondImgView.imageProperty().unbind();
            secondImagePane.setVisible(false);
            activeImages--;
        } else if (clickedButton.equals(thirdDeleteButton)) {
            if (upscaledImages.get(2) != null) {
                upscaledImages.set(2, null);
                thirdShowButton.setVisible(false);
            }
            thirdTextArea.textProperty().unbind();
            thirdTextArea.textProperty().set("");
            thirdImgView.imageProperty().unbind();
            thirdImagePane.setVisible(false);
            activeImages--;
        }
    }

    @FXML
    private void OnUpscaleShowClick(ActionEvent event) throws Exception {
        Button clickedButton = (Button) event.getSource();
        Image relativeImg = null;

        if (clickedButton.equals(firstShowButton))
            relativeImg = upscaledImages.get(0);
        else if (clickedButton.equals(secondShowButton))
            relativeImg = upscaledImages.get(1);
        else if (clickedButton.equals(thirdShowButton))
            relativeImg = upscaledImages.get(2);

        try {
            if (relativeImg == null)
                throw new NullPointerException();

            viewerApp.setGenerated(false);
            viewerApp.setExportedImage(relativeImg);
            viewerApp.init();
            viewerApp.start(new Stage());

        } catch (NullPointerException e) {
            Alert npAlert = new Alert(Alert.AlertType.ERROR);
            npAlert.setHeaderText("ERROR: Image is Null");
            npAlert.setContentText("Due to an inexplicable error, no image has been found on this layer.");
            npAlert.showAndWait();
        }
    }

    @Override
    public String callPyScript(String prompt, String tags, String date, String path) throws IOException,
            GenerationException, UpscalingException
    {
        // Get all necessary paths
        File pythonVenv = findPyVenv();
        String activateScriptPath = pythonVenv.getPath();
        File pythonScript = findPyScript();
        String pythonScriptPath = pythonScript.getPath();

        // Initialize logs
        StringBuilder output = new StringBuilder();
        StringBuilder execOutput = new StringBuilder();

        // Construct the command to execute
        List<String> generateCommand;
        List<String> upscaleCommand;
        ProcessBuilder processBuilder;

        // Check that pixel art style is being used, if so apply token
        if (styleComboBox.getValue().equals("Pixel Art"))
            prompt = prompt + ", pixelartstyle";

        if (pythonCalledBy == GENERATE.getValue()) {
            generateCommand = List.of (
                    activateScriptPath,      // Activate virtual environment
                    pythonScriptPath,        // Path to the Python script
                    prompt,                  // Prompt argument
                    tags,                   // Tags argument
                    date                    // Date argument
            );
            processBuilder = new ProcessBuilder(generateCommand);
        }
        else {
            upscaleCommand = List.of (
                    activateScriptPath,
                    pythonScriptPath,
                    path, // Image path argument
                    date
            );
            processBuilder = new ProcessBuilder(upscaleCommand);
        }

        // Start the process
        Process process = processBuilder.start();

                /* Concurrently read execution output
            (what is being printed automatically by the algorithm during its execution) */
        Thread executionPrinter = new Thread(() -> {
            try (
                    BufferedReader execBufferedReader = new BufferedReader(new InputStreamReader(
                            process.getErrorStream()))
            ) {
                String executionLine;

                // ANSI escape codes (may not work on all shells)
                String yellowColor = "\u001B[33m";
                String resetColor = "\u001B[0m";

                while ((executionLine = execBufferedReader.readLine()) != null) {
                    execOutput.append(executionLine).append("\n");
                    System.out.println(yellowColor + executionLine.trim() + resetColor);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        executionPrinter.start();

        // Capture the input stream (what is being manually printed to console by the Python script)
        try (
                InputStream inputStream = process.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader inputbufferedReader = new BufferedReader(inputStreamReader);
        ) {
            String inputLine;
            while ((inputLine = inputbufferedReader.readLine()) != null) {
                output.append(inputLine).append("\n");
            }
        }

        // Wait for the process to finish
        try {
            int exitCode = process.waitFor();
            System.out.println("\nPython script exited with code: " + exitCode);

            if (exitCode == 0) {
                // If there is no error, return the output as a String
                return output.toString().trim();
            } else {
                // If there is an error, throw the appropriate exception
                if (pythonCalledBy == GENERATE.getValue())
                    throw new GenerationException();
                else
                    throw new UpscalingException();
            }

        } catch (InterruptedException e) {
            if (pythonCalledBy == GENERATE.getValue())
                throw new GenerationException();
            else
                throw new UpscalingException();
        }
    }

    @Override
    public String callPyScript(String prompt, String tags, String date) throws IOException, GenerationException {
        try {
            return callPyScript(prompt, tags, date, null);

            // called by OnCreateClick(), you can't get an UpscalingException
        } catch (UpscalingException ignored) {}

        return ERROR.getCode();
    }

    @Override
    public String callPyScript(String imgPath) throws IOException, UpscalingException {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        formattedDate = currentDate.format(formatter);

        try {
            return callPyScript("", null, formattedDate, imgPath);

            // called by OnUpscaleStartClick(), you can't get a GenerationException
        } catch (GenerationException ignored) {}

        return ERROR.getCode();
    }

    @Override
    public File findPyVenv() {
        // Get user's home directory and virtual environment folder
        String userHome = System.getProperty("user.home");
        File directory = new File(userHome, "venv");

        return searchFile(directory, "python");
    }

    @Override
    public File findPyScript() {
        // Get the working directory
        String workingDirectory = System.getProperty("user.dir");
        File directory = new File(workingDirectory);

        String fileName;
        switch (pythonCalledBy) {
            case 1:
                if (styleComboBox.getValue().equals("General"))
                    // if (includeUpscaling) -> generate_upscale.py, else -> generate.py
                    fileName = includeUpscaling ? "generate_upscale.py" : "generate.py";
                else
                    fileName = "generate_pixart.py";
                break;
            case 2:
                fileName = "upscale.py";
                break;
            default:
                throw new IllegalArgumentException("Invalid pythonCalledBy value: " + pythonCalledBy);
        }

        return searchFile(directory, fileName);
    }

    private File searchFile(File directory, String fileName) {
        File[] files = directory.listFiles(); // similar to 'tree' in shell, it scans for all files
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively search in subdirectories
                    File foundScript = searchFile(file, fileName);
                    if (foundScript != null)
                        return foundScript;
                } else if (Objects.equals(file.getName(), fileName))
                    return file;
            }
        }

        return null;
    }

    @SuppressWarnings("automatic")
    public void setStage(Stage stage) {
        // no action needed, stage is passed from DiffusionApplication
    }

    private String countGeneratedImgs() {
        User user = diffApp.getUser();
        int num = user.getGenImgsNum();
        String startText = "You have generated ";
        String endText;

        if (num == 1)
            endText = " image so far.";
        else
            endText = " images so far.";

        return startText + num + endText;
    }

    private String countUpscaledImgs() {
        User user = diffApp.getUser();
        String startText = "You have upscaled ";
        String endText = " images so far.";

        return startText + user.getUpsImgsNum() + endText;
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

    private double getFileSize(File file) throws FileNotFoundException {
        if (file.exists()) {
            long fileSizeInBytes = file.length();

            // Convert bytes to kilobytes, megabytes, or gigabytes if needed
            double fileSizeInKB = fileSizeInBytes / 1024.0;
          /*  double fileSizeInMB = fileSizeInKB / 1024.0;
            double fileSizeInGB = fileSizeInMB / 1024.0; */

            return fileSizeInKB;
        } else
            throw new FileNotFoundException();
    }

    private String getImageProperties(String name, Image image, double size) {
        StringBuilder stringBuilder = new StringBuilder();

        String resX = Integer.toString((int) image.getWidth());
        String resY = Integer.toString((int) image.getHeight());
        String sizeStr = String.format("%.2f", size); // sets precision to 2nd decimal digit

        stringBuilder.append(resX)
                .append("x")
                .append(resY)
                .append("px")
                .append(" | ")
                .append(sizeStr)
                .append(" KB | ")
                .append(name);

        return stringBuilder.toString();
    }

    public void throwGenericAlert() {
        Alert genAlert = new Alert(Alert.AlertType.ERROR);

        if (pythonCalledBy == GENERATE.getValue()) {
            genAlert.setHeaderText("ERROR: Generation Failure");
            genAlert.setContentText("Something went wrong in the image creation. Please retry");
        } else {
            genAlert.setHeaderText("ERROR: Upscaling Failure");
            genAlert.setContentText("Something went wrong in the image upscaling. Please retry");
        }
        genAlert.showAndWait();
    }

    private int checkAvailableSpace() {
        if (!firstImagePane.isVisible())
            return 0;
        if (!secondImagePane.isVisible())
            return 1;
        if (!thirdImagePane.isVisible())
            return 2;

        return MAX_CAPACITY.getValue();
    }

    private void enableAllButtons() {
        firstStartButton.setVisible(true);
        firstDeleteButton.setVisible(true);
        secondStartButton.setVisible(true);
        secondDeleteButton.setVisible(true);
        thirdStartButton.setVisible(true);
        thirdDeleteButton.setVisible(true);
    }

    private void disableAllButtons() {
        firstStartButton.setVisible(false);
        firstDeleteButton.setVisible(false);
        secondStartButton.setVisible(false);
        secondDeleteButton.setVisible(false);
        thirdStartButton.setVisible(false);
        thirdDeleteButton.setVisible(false);
    }

    private String getImagePath(Image image) {
        String imageUrl = image.getUrl();
        String[] pathArgs = imageUrl.split("file:");

        return pathArgs[1];
    }

    private String encodeImage(Image image) throws IOException {
        String imageUrl = image.getUrl();
        String[] pathArgs = imageUrl.split("file:");
        Path imagePath = Paths.get(pathArgs[1]);

        byte[] imageBytes = Files.readAllBytes(imagePath);
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        return encodedImage;
    }

    private void deleteSessionData() {
        Path dataPath = Paths.get("data");

        if (!Files.exists(dataPath))
            return;

        File sessionData = new File(dataPath + "/session.sr");
        sessionData.delete();
    }
}
