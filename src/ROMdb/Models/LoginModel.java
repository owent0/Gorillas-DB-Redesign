package ROMdb.Models;

import ROMdb.Controllers.ChangeAdminPasswordController;
import ROMdb.Controllers.EstimationBaseController;
import ROMdb.Controllers.MainMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Created by chrisrmckane on 4/11/17.
 */
public class LoginModel {

    public static MainMenuController mainMenuController;
    public static EstimationBaseController estimationBaseController;
    public static boolean isAdmin = false;
    public static boolean isLoggedIn = false;
    private static String adminPassword = "";

    public static String getAdminPassword() {
        return adminPassword;
    }

    public static void changeAdminPassword(String oldPassword, String newAdminPassword, String confirmNewPassword) {
        if(oldPassword.equals(adminPassword) && newAdminPassword.equals(confirmNewPassword)) {
            adminPassword = newAdminPassword;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Old or New Passwords do not match!", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
