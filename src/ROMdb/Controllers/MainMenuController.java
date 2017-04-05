/**
 * This is the controller that will be used for any actions that are
 * performed outside of each of the menus. It will decide which menus
 * to display, handling exiting the program, the menu bar and any other
 * external functions to each specific menu.
 */

package ROMdb.Controllers;

import ROMdb.Driver.Main;
import ROMdb.Models.MainMenuModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Collator;
import java.util.ArrayList;


/**
 * Created by Team Gorillas on 2/19/2017.
 */
public class MainMenuController
{
    // The loader object for FXML.
    public FXMLLoader loader;

    @FXML private AnchorPane anchor_estimation;
    @FXML private AnchorPane anchor_requirements;
    @FXML private AnchorPane anchor_mainScIcr;

    @FXML private StackPane mainStackPane;
    @FXML private StackPane estimationStackPane;
    @FXML private StackPane scicrStackPane;
    @FXML private StackPane requirementsStackPane;

    @FXML private ComboBox<String> combo_baseline;

    @FXML
    public void initialize()
    {
        this.combo_baseline.setItems(new SortedList<String>(MainMenuModel.getBaselines(), Collator.getInstance()));
    }

    @FXML
    public void changeSelectedBaseline() {
        MainMenuModel.setSelectedBaseline(this.combo_baseline.getSelectionModel().getSelectedItem());
        MainMenuModel.estimationBaseController.fillTextFieldsFromDB();
        MainMenuModel.sCICRController.switchTableData();
    }

    /**
     * Set the estimation base pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewEstimationBase()
    {
        estimationStackPane.setVisible(true);
        requirementsStackPane.setVisible(false);
        scicrStackPane.setVisible(false);
    }


    /**
     * Set the requirements pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewRequirementsEntry()
    {
        estimationStackPane.setVisible(false);
        requirementsStackPane.setVisible(true);
        scicrStackPane.setVisible(false);
    }

    /**
     * Set the SC/ICR entry pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewMainScIcr()
    {
        estimationStackPane.setVisible(false);
        requirementsStackPane.setVisible(false);
        scicrStackPane.setVisible(true);
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

        stage.setTitle("Baseline Creation");
        stage.setScene(new Scene(root, 375, 255));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Handles the action when the user selects exit button.
     */
    @FXML
    public void exitProgram() throws SQLException {
        Main.conn.close();
        System.exit(0);
    }
}
