package ROMdb.Controllers;

import ROMdb.Models.LoginModel;
import ROMdb.Models.MainMenuModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Created by chrisrmckane on 4/11/17.
 */
public class LoginController {

    // The choices for login names to the system
    private ObservableList<String> options = FXCollections.observableArrayList("User", "Admin");

    // GUI components for the LoginView
    @FXML private ComboBox combo_username;
    @FXML private PasswordField passfield_password;
    @FXML private Label label_loginmessage;
    @FXML private Button button_login;
    @FXML private Hyperlink hyperlink_forgotPassword;
    private int loginAttempts = 0;

    /**
     * Initializes the login view components
     */
    @FXML
    public void initialize(){

        // Populates the combo box with pre-determined user names
        combo_username.setItems(options);

        // Sets the combo box to 'User' by default
        combo_username.getSelectionModel().selectFirst();

        MainMenuModel.loginController = this;

    }

    /**
     * Method called when the login button is clicked. Checks to see which user is logging in.
     *
     * If 'User' is logging in LoginModel.isAdmin is set to false and then it calls loginSuccess()
     * since no password is required.
     *
     * If 'Admin' is logging in then it also checks the password to see if it is correct.
     * If the password is correct then LoginModel.isAdmin is set to true to flag that an admin
     * has signed in. Then estimation base weights are enabled. Then loginSuccess() is called.
     * If the admin password is incorrect and error message informs the user that the password
     * or username is incorrect and gives them a chance to enter again.
     */
    @FXML
    private void loginButtonClicked() {

        try {

            if(combo_username.getSelectionModel().getSelectedItem().equals("User")){
                LoginModel.estimationBaseController.disableComponents();
                LoginModel.isAdmin = false;
                loginSuccess();

            } else if(combo_username.getSelectionModel().getSelectedItem().equals("Admin") && LoginModel.checkInputPassword(passfield_password.getText())) {

                LoginModel.isAdmin = true;
                LoginModel.estimationBaseController.enableComponents();
                loginSuccess();

            } else {

                if(loginAttempts >= 5){
                    label_loginmessage.setVisible(false);
                    hyperlink_forgotPassword.setVisible(true);
                    loginAttempts++;
                } else {
                    label_loginmessage.setTextFill(Color.web("#ff0000"));
                    label_loginmessage.setText("Invalid Username or Password!");
                    loginAttempts++;
                    passfield_password.clear();
                }

            }

        }  catch(Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a username.", ButtonType.OK);
            alert.showAndWait();

        }
    }

    /**
     * When login is successful LoginModel.isLoggedIn is set to true to flag that someone has logged in. The user is
     * then welcomed and informed who they are logged in under. The combo box, password field and login buttons are
     * then hidden. Then all of the main menu buttons are enabled for use of the program.
     */
    private void loginSuccess() {

        LoginModel.isLoggedIn = true;
        hyperlink_forgotPassword.setVisible(false);
        label_loginmessage.setTextFill(Color.web("#000000"));
        label_loginmessage.setText("Welcome! You are now logged in as: " + combo_username.getSelectionModel().getSelectedItem().toString());
        label_loginmessage.setVisible(true);
        combo_username.setVisible(false);
        passfield_password.setVisible(false);
        button_login.setVisible(false);
        loginAttempts = 0;

        LoginModel.mainMenuController.enableMenuButtons();

    }

    /**
     * When logout is successful the login message is hidden and the combo box, password field and login button
     * are made visible.
     */
    public void logoutSuccess() {

        label_loginmessage.setText("");
        combo_username.setVisible(true);
        passfield_password.clear();
        passfield_password.setVisible(true);
        button_login.setVisible(true);

    }

    /**
     * This method makes a password field appear when the 'Admin' user is selected
     */
    @FXML
    private void enablePasswordField() {
        if(combo_username.getSelectionModel().getSelectedItem().equals("Admin")) {
            passfield_password.setDisable(false);
        } else {
            passfield_password.setDisable(true);
        }
    }

    /**
     * Used to place an initial password if the DBUser table has an error.
     * Change LoginView Login button to call this method. Sets password to ""
     * which can later be changed in the program
     */
    @FXML
    private void callInitPassword() {
        LoginModel.writeInitAdminPasswordToDB();
    }

    @FXML
    private void forgotPasswordClicked() throws IOException {

        hyperlink_forgotPassword.setVisible(false);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/EnterMasterPasswordView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        /* Focus on this current window to prevent clicking on windows behind it. */
        Stage owner = (Stage) combo_username.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Master Password");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

}
