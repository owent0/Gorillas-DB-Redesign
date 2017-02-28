package ROMdb.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Created by Team Gorillas on 2/19/2017.
 */
public class Controller
{
    public FXMLLoader loader;

    @FXML private EstimationController estimationController;
    @FXML private RequirementsController requirementsController;

    /** These all link into the Scene Builder ID field for the components.
     *  It is how we can reference them in the java code. */
    @FXML private Pane pane_menu;

    @FXML private AnchorPane anchor_estimation;
    @FXML private AnchorPane anchor_requirements;

    @FXML private Button button_estimationBase;       // Button switch to estimation base pane.
    @FXML private Button button_SCICR;                // Button to switch to SC/ICR entry pane.
    @FXML private Button button_requirementsEntry;    // Button to switch to requirements entry pane.
    @FXML private Button button_exit;                 // Exit button.
    @FXML private Button button_baseline;             // Button to change base line

    @FXML
    public void initialize()
    {
        System.out.println("Controller class initialized!");

    }

    @FXML
    public void viewEstimationBase()
    {
        anchor_estimation.setVisible(true);
        anchor_requirements.setVisible(false);
    }

    @FXML
    public void viewRequirementsEntry()
    {
        anchor_estimation.setVisible(false);
        anchor_requirements.setVisible(true);
    }

    @FXML
    public void exitProgram()
    {
        System.exit(0);
    }

    public void setLoader(FXMLLoader loader)
    {
        this.loader = loader;
    }
}
