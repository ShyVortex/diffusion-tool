package it.unimol.diffusiontool;

import it.unimol.diffusiontool.application.DiffusionApplication;
import it.unimol.diffusiontool.application.LoginApplication;
import it.unimol.diffusiontool.entities.User;
import it.unimol.diffusiontool.entities.UserManager;
import javafx.scene.control.Alert;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        User lastSessionUser;

        try {
            userManager = UserManager.loadUsers();
            lastSessionUser = DiffusionApplication.loadSession();
            LoginApplication.setUserManager(userManager);

            if (lastSessionUser != null) {
                DiffusionApplication.setUser(lastSessionUser);
                DiffusionApplication.main(args);
            } else
                LoginApplication.main(args);

        } catch (FileNotFoundException e) {
            LoginApplication.setUserManager(userManager);
            LoginApplication.main(args);
        } catch (IncompatibleClassChangeError e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR: Incompatible Changes");
            alert.setContentText("Can't start the application because saved data is incompatible or corrupted. Please" +
                    " delete all files in 'data', then try again.");
        }
    }
}
