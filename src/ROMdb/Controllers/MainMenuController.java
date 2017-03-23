/**
 * This is the controller that will be used for any actions that are
 * performed outside of each of the menus. It will decide which menus
 * to display, handling exiting the program, the menu bar and any other
 * external functions to each specific menu.
 */

package ROMdb.Controllers;

import ROMdb.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Created by Team Gorillas on 2/19/2017.
 */
public class MainMenuController
{
    // The loader object for FXML.
    public FXMLLoader loader;

    public static String selectedBaseline;

    public static ObservableList<String> baselines = fetchBaselinesFromDB();

    @FXML private AnchorPane anchor_estimation;
    @FXML private AnchorPane anchor_requirements;
    @FXML private AnchorPane anchor_mainScIcr;


    /**
     * This method will read all of the baselines currently stored within
     * the baseline database table.
     *
     * @return ObservableList the list containing the baseline from the baselines table.
     */
    private static ObservableList<String> fetchBaselinesFromDB() {

        // The list to store the baselines in temporarily.
        ArrayList<String> baselines = new ArrayList<String>();

        try
        {
            // Grab all the baselines.
            String query = "SELECT * FROM baseline";

            // Create the statement.
            Statement st = Main.conn.createStatement();

            // Get the result set from the query.
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) // Retrieve data from ResultSet
            {
                baselines.add(rs.getString("baseline")); //4th column of Table
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }

        // Convert to observable list for FXML purposes.
        ObservableList bases = FXCollections.observableArrayList(baselines);

        return bases;
    }

    /**
     * Set the estimation base pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewEstimationBase()
    {
        anchor_estimation.setVisible(true);
        anchor_requirements.setVisible(false);
        anchor_mainScIcr.setVisible(false);
    }


    /**
     * Set the requirements pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewRequirementsEntry()
    {
        anchor_estimation.setVisible(false);
        anchor_requirements.setVisible(true);
        anchor_mainScIcr.setVisible(false);
    }

    /**
     * Set the SC/ICR entry pane to visible when button is hit.
     * All others are set to invisible.
     */
    @FXML
    public void viewMainScIcr()
    {
        anchor_estimation.setVisible(false);
        anchor_requirements.setVisible(false);
        anchor_mainScIcr.setVisible(true);
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
                 "You must restart the application for changes to take affect.\n" +
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/AddBaselineView.fxml"));
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

    /**
     * Loader setter method.
     * @param loader The FXML loader.
     */
    public void setLoader(FXMLLoader loader)
    {
        this.loader = loader;
    }
}
