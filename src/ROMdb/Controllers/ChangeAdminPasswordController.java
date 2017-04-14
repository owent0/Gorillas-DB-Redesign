package ROMdb.Controllers;

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
    @FXML private Button button_changePass;
    @FXML private Button button_cancel;

    @FXML
    private void changeAdminPassword(){
        LoginModel.changeAdminPassword(passwordField_currentPass.getText(), passwordField_newPass.getText(), passwordField_confirmPass.getText());
        closeScene();
    }

    public void clearPasswordFields() {

        passwordField_currentPass.clear();
        passwordField_newPass.clear();
        passwordField_confirmPass.clear();

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
