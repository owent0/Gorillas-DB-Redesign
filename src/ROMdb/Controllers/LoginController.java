package ROMdb.Controllers;

import ROMdb.Models.LoginModel;
import ROMdb.Models.MainMenuModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Created by chrisrmckane on 4/11/17.
 */
public class LoginController {

    private ObservableList<String> options = FXCollections.observableArrayList("User", "Admin");
    @FXML private ComboBox combo_username;
    @FXML private PasswordField passfield_password;
    @FXML private Label label_loginmessage;
    @FXML private Button button_login;
    @FXML private ImageView image_ASRC;

    @FXML
    public void initialize(){

        combo_username.setItems(options);
        combo_username.getSelectionModel().selectFirst();
        MainMenuModel.loginController = this;

    }

    @FXML
    private void loginButtonClicked() {

        try {

            if(combo_username.getSelectionModel().getSelectedItem().equals("User")){

                LoginModel.isAdmin = false;
                loginSuccess();

            } else if(combo_username.getSelectionModel().getSelectedItem().equals("Admin") && passfield_password.getText().equals(LoginModel.getAdminPassword())){

                LoginModel.isAdmin = true;
                loginSuccess();

                LoginModel.estimationBaseController.enableWeights();

            } else {

                label_loginmessage.setTextFill(Color.web("#ff0000"));
                label_loginmessage.setText("Invalid Username or Password!");
                passfield_password.clear();

            }

        }catch(Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a username.", ButtonType.OK);
            alert.showAndWait();

        }
    }

    private void loginSuccess() {

        LoginModel.isLoggedIn = true;
        label_loginmessage.setTextFill(Color.web("#000000"));
        label_loginmessage.setText("Welcome! You are now logged in as: " + combo_username.getSelectionModel().getSelectedItem().toString());
        combo_username.setVisible(false);
        passfield_password.setVisible(false);
        button_login.setVisible(false);

        LoginModel.mainMenuController.enableMenuButtons();

    }


    public void logoutSuccess() {

        label_loginmessage.setText("");
        combo_username.setVisible(true);
        passfield_password.clear();
        passfield_password.setVisible(true);
        button_login.setVisible(true);

    }

    @FXML
    private void enablePasswordField() {
        if(combo_username.getSelectionModel().getSelectedItem().equals("Admin")) {
            passfield_password.setDisable(false);
        } else {
            passfield_password.setDisable(true);
        }
    }
}
