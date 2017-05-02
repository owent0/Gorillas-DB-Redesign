package ROMdb.Controllers;

import ROMdb.Exceptions.InputFormatException;
import ROMdb.Models.LoginModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Created by chris on 4/13/2017.
 */
public class ChangeAdminPasswordController {

    @FXML private PasswordField passwordField_currentPass;
    @FXML private PasswordField passwordField_newPass;
    @FXML private PasswordField passwordField_confirmPass;
    @FXML private Button button_cancel;

    /**
     * Calls the changeAdminPassword in LoginModel from the ChangeAdminPasswordView
     */
    @FXML
    private void changeAdminPassword() {

        /* Calls the changeAdminPassword for the LoginModel where the password encryptor is located */
        LoginModel.changeAdminPassword(passwordField_currentPass.getText(), passwordField_newPass.getText(), passwordField_confirmPass.getText());

        if(LoginModel.passMatches) {

            /* If the passwords matched and the password was changed, the scene closes */
            closeScene();
        } else {

            /* Clears the password fields, allowing the user to try again if the passwords didn't match */
            passwordField_currentPass.clear();
            passwordField_newPass.clear();
            passwordField_confirmPass.clear();
        }

    }

    /**
     * Closes the changeAdminPassword scene.
     */
    @FXML
    private void closeScene() {
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.close();
    }
}
