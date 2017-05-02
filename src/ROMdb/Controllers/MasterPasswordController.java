package ROMdb.Controllers;


import ROMdb.Models.LoginModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by chris on 5/2/2017.
 */
public class MasterPasswordController {

    @FXML private Label label_enterMasterPass;
    @FXML private PasswordField passField_masterPassword;
    @FXML private Button button_forgotConfirm;
    @FXML private Button button_forgotCancel;


    /**
     * When the user enters the Master Password and clicks the confirm button the plain text master password is checked
     * against the one stored in the DB. If it is correct the admin password is set to the default password. Then they
     * are prompted to change the admin password using the default password.
     *
     * If the plain text master password does not match, the user is prompted to try again or go to IT for help.
     */
    @FXML
    private void confirmButtonClicked() {

        try {

            if(LoginModel.checkInputMasterPassword(passField_masterPassword.getText())){
                LoginModel.writeInitAdminPasswordToDB();
                exit();
                callChangePasswordView();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The Master Password you have entered does not match!\nTry again or talk with IT.", ButtonType.OK);
                passField_masterPassword.clear();
                alert.showAndWait();
            }

        }  catch(Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Error checking the password.", ButtonType.OK);
            alert.showAndWait();

        }
    }

    /**
     * Calls the Change Admin Password window so the user can change the admin password after successfully entering the
     * master password.
     * @throws IOException if there is an error calling the window
     */
    private void callChangePasswordView() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/ChangeAdminPasswordView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

//        /* Focus on this current window to prevent clicking on windows behind it. */
//        Stage owner = (Stage) .getScene().getWindow();
//        stage.initOwner(owner);
//        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Changing Password");
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Exits the window when the user presses the cancel button.
     */
    @FXML
    private void exit()
    {
        Stage stage = (Stage) button_forgotConfirm.getScene().getWindow();
        stage.close();
    }
}
