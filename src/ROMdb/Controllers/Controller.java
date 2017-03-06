/**
 * This is the controller that will be used for any actions that are
 * performed outside of each of the menus. It will decide which menus
 * to display, handling exiting the program, the menu bar and any other
 * external functions to each specific menu.
 */

package ROMdb.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.Connection;


/**
 * Created by Team Gorillas on 2/19/2017.
 */
public class Controller
{
    // The loader object for FXML.
    public FXMLLoader loader;

    // Will be used as the global connection to database.
    private Connection conn = null;

    /** References to other controllers go here. */
    @FXML private EstimationController estimationController;
    @FXML private RequirementsController requirementsController;
    @FXML private ScIcrController scIcrController;

    /** These all link into the Scene Builder ID field for the components.
     *  It is how we can reference them in the java code. */
    @FXML private Pane pane_menu;

    @FXML private AnchorPane anchor_estimation;
    @FXML private AnchorPane anchor_requirements;
    @FXML private AnchorPane anchor_mainScIcr;

    @FXML private Button button_estimationBase;       // Button switch to estimation base pane.
    @FXML private Button button_SCICR;                // Button to switch to SC/ICR entry pane.
    @FXML private Button button_requirementsEntry;    // Button to switch to requirements entry pane.
    @FXML private Button button_exit;                 // Exit button.
    @FXML private Button button_baseline;             // Button to change base line


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

    @FXML
    public void viewMainScIcr()
    {
        anchor_estimation.setVisible(false);
        anchor_requirements.setVisible(false);
        anchor_mainScIcr.setVisible(true);
    }


    /**
     * Handles the action when the user selects exit button.
     */
    @FXML
    public void exitProgram()
    {
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
