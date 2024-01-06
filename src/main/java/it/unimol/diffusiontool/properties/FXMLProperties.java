package it.unimol.diffusiontool.properties;

import javafx.fxml.FXMLLoader;

public class FXMLProperties {
    private FXMLLoader loginFXML;
    private FXMLLoader signupFXML;
    private FXMLLoader homeFXML;
    private FXMLLoader profileFXML;
    private FXMLLoader generateFXML;
    private FXMLLoader viewerFXML;
    private FXMLLoader upscaleFXML;
    private static FXMLProperties instance;

    public FXMLProperties() {
        loginFXML = new FXMLLoader(this.getClass().getResource("/login-view.fxml"));
        signupFXML = new FXMLLoader(this.getClass().getResource("/signup-view.fxml"));
        homeFXML = new FXMLLoader(this.getClass().getResource("/app-home-view.fxml"));
        profileFXML = new FXMLLoader(this.getClass().getResource("/app-profile-view.fxml"));
        generateFXML = new FXMLLoader(this.getClass().getResource("/app-generate-view.fxml"));
        viewerFXML = new FXMLLoader(this.getClass().getResource("/image-view.fxml"));
        upscaleFXML = new FXMLLoader(this.getClass().getResource("/app-upscale-view.fxml"));
    }

    public static FXMLProperties getInstance() {
        if (instance == null)
            instance = new FXMLProperties();

        return instance;
    }

    public FXMLLoader getLoginFXML() {
        return loginFXML;
    }

    public FXMLLoader getSignupFXML() {
        return signupFXML;
    }

    public FXMLLoader getHomeFXML() {
        return homeFXML;
    }

    public FXMLLoader getProfileFXML() {
        return profileFXML;
    }

    public FXMLLoader getGenerateFXML() {
        return generateFXML;
    }

    public FXMLLoader getViewerFXML() {
        return viewerFXML;
    }

    public FXMLLoader getUpscaleFXML() {
        return upscaleFXML;
    }
}
