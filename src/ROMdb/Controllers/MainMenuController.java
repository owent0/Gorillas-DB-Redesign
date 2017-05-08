/**
 * This is the controller that will be used for any actions that are
 * performed outside of each of the menus. It will decide which menus
 * to display, handling exiting the program, the menu bar and any other
 * external functions to each specific menu.
 */

package ROMdb.Controllers;

import ROMdb.Driver.Main;
import ROMdb.Models.LoginModel;
import ROMdb.Models.MainMenuModel;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.Collator;


/**
 * Created by Team Gorillas on 2/19/2017.
 */
public class MainMenuController
{
    // The loader object for FXML.
    public FXMLLoader loader;

    public static RequirementsController requirementsController;

    @FXML private AnchorPane anchor_estimation;
    @FXML private AnchorPane anchor_requirements;
    @FXML private AnchorPane anchor_mainScIcr;

    @FXML private StackPane mainStackPane;
    @FXML private StackPane estimationStackPane;
    @FXML private StackPane scicrStackPane;
    @FXML private StackPane requirementsStackPane;
    @FXML private StackPane loginStackPane;

    @FXML private Button button_SCICR;
    @FXML private Button button_requirementsEntry;
    @FXML private Button button_estimationBase;
    @FXML private Button button_viewArchive;
    @FXML private MenuItem menuItem_createBaseline;
    @FXML private MenuItem menuItem_changeAdminPassword;
    @FXML private MenuItem menuItem_addItem;
    @FXML private ComboBox<String> combo_baseline;
    @FXML private Label label_selectBaseline;

    /**
     * This method will be called upon creating this window.
     */
    @FXML
    public void initialize()
    {
        this.combo_baseline.setItems(new SortedList<String>(MainMenuModel.getBaselines(), Collator.getInstance()));
        this.combo_baseline.getSelectionModel().select(MainMenuModel.getSelectedBaseline());
        LoginModel.mainMenuController = this;
    }

    /**
     * Changes the currently selected baseline inside
     * of the combobox in each menu.
     */
    @FXML
    public void changeSelectedBaseline()
    {
        MainMenuModel.setSelectedBaseline(this.combo_baseline.getSelectionModel().getSelectedItem());
        MainMenuModel.refreshSCICRsFromDB();
        MainMenuModel.estimationBaseController.fillTextFieldsFromDB();
        MainMenuModel.sCICRController.switchTableData();
        requirementsController.updateFilterByBaseline();
    }

    /**
     * Set the estimation base pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewEstimationBase()
    {
        MainMenuModel.estimationBaseController.fillTextFieldsFromDB();

        estimationStackPane.setVisible(true);
        requirementsStackPane.setVisible(false);
        scicrStackPane.setVisible(false);
        loginStackPane.setVisible(false);
    }


    /**
     * Set the requirements pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewRequirementsEntry()
    {
        requirementsController.updateFilterByBaseline();

        estimationStackPane.setVisible(false);
        requirementsStackPane.setVisible(true);
        scicrStackPane.setVisible(false);
        loginStackPane.setVisible(false);
    }

    /**
     * Set the SC/ICR entry pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewMainScIcr()
    {
        MainMenuModel.sCICRController.switchTableData();

        estimationStackPane.setVisible(false);
        requirementsStackPane.setVisible(false);
        scicrStackPane.setVisible(true);
        loginStackPane.setVisible(false);
    }


    /**
     * Changes the path string located in the file path.dat to a new path
     * that the user gets with a file chooser.
     * @throws IOException If I/O error occurs.
     * @throws URISyntaxException If path cannot be found.
     * @throws SQLException If connection cannot be found.
     */
    @FXML
    public void changeDatabasePath() throws IOException, URISyntaxException, SQLException {

        // Return the file chosen with the chooser.
        File tempFile = Main.fileHandler.useFileChooser();

        // If file is null or user hits cancel.
        if(tempFile == null) {
            return;
        }

        // Call to write the new path into the file.
        Main.fileHandler.writeNewPath( tempFile.getPath() );

        // Alert to restart the program.
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                 "You must restart the application for changes to take effect.\n" +
                            "Want to restart?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            exitProgram();
        }
    }


    /**
     * If the user chooses to add or edit a baseline from the menu bar feature.
     * @throws IOException If I/O error occurs.
     */
    @FXML
    public void addBaselineFromMenuBar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/AddBaselineWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        /* Focus on this current window to prevent clicking on windows behind it. */
        Stage owner = (Stage) combo_baseline.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Baseline Creation");
        stage.setScene(new Scene(root, 375, 255));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Opens the archive window if a user selects the 'View Archive' button
     * @throws IOException If I/O error occurs.
     */
    @FXML
    public void viewArchive() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/ArchiveWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        /* Focus on this current window to prevent clicking on windows behind it. */
        Stage owner = (Stage) combo_baseline.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Archive");
        stage.setScene(new Scene(root));
        //stage.setResizable(false);
        stage.show();
    }

    /**
     * This method gets called when the user selects 'Logout' from the File menu on the menu bar.
     * When this method is called the system is set to a logged out state.
     */
    @FXML
    public void systemLogout() {

        if(LoginModel.isLoggedIn == false) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "No username is logged in.", ButtonType.OK);
            alert.showAndWait();

        } else {

            estimationStackPane.setVisible(false);
            requirementsStackPane.setVisible(false);
            scicrStackPane.setVisible(false);
            loginStackPane.setVisible(true);

            LoginModel.isAdmin = false;
            LoginModel.isLoggedIn = false;

            LoginModel.mainMenuController.disableMenuButtons();
            LoginModel.estimationBaseController.disableComponents();
            MainMenuModel.loginController.logoutSuccess();

        }

    }

    /**
     * Enables the buttons on the left side of the main menu
     */
    public void enableMenuButtons() {

        button_SCICR.setDisable(false);
        button_estimationBase.setDisable(false);
        button_requirementsEntry.setDisable(false);
        button_viewArchive.setDisable(false);
        combo_baseline.setDisable(false);
        label_selectBaseline.setTextFill(Color.web("#000000"));
        menuItem_addItem.setDisable(false);

        if(LoginModel.isAdmin == true){
            menuItem_createBaseline.setDisable(false);
            menuItem_changeAdminPassword.setDisable(false);
        }
    }

    /**
     * Disables the buttons on the left side of the main menu
     */
    public void disableMenuButtons() {

        button_SCICR.setDisable(true);
        button_estimationBase.setDisable(true);
        button_requirementsEntry.setDisable(true);
        button_viewArchive.setDisable(true);
        combo_baseline.setDisable(true);
        menuItem_createBaseline.setDisable(true);
        menuItem_changeAdminPassword.setDisable(true);
        menuItem_addItem.setDisable(true);
        label_selectBaseline.setTextFill(Color.web("#b8b8b8"));
    }

    /**
     * Opens the Change Admin Password window when the user selects the 'Change Admin Password' menu item in the
     * file menu bar while logged in as an admin.
     * @throws IOException If I/O error occurs.
     */
    @FXML
    public void viewChangeAdminPassword() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/ChangeAdminPasswordView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        /* Focus on this current window to prevent clicking on windows behind it. */
        Stage owner = (Stage) combo_baseline.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Changing Password");
        stage.setScene(new Scene(root));
        //stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void viewAddItemMenu() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/AddItemsView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        /* Focus on this current window to prevent clicking on windows behind it. */
        Stage owner = (Stage) combo_baseline.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Add New Item");
        stage.setScene(new Scene(root));
        //stage.setResizable(false);
        stage.show();
    }

    /**
     * Handles the action when the user selects exit button.
     * @throws SQLException If the connection could not close successfully.
     */
    @FXML
    public void exitProgram() throws SQLException {
        Main.conn.close();
        System.exit(0);
    }
}
