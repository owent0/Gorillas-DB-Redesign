package ROMdb;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Created by Team Gorillas on 2/19/2017.
 */
public class Controller {


    /** These all link into the Scene Builder ID field for the components.
     *  It is how we can reference them in the java code. */
    @FXML
        private StackPane stackPane_main;           // Stack pane for switch panels.
    @FXML
        private TabPane tabPane_requirementsEntry;     // Pane with tabs at the top.
    @FXML
        private Pane pane_estimationBase;           // Pane used for estimation base.
    @FXML
        private Button button_estimationBase;       // Button switch to estimation base pane.
    @FXML
        private Button button_SCICR;                // Button to switch to SC/ICR entry pane.
    @FXML
        private Button button_requirementsEntry;    // Button to switch to requirements entry pane.
    @FXML
        private Button button_exit;                 // Exit button.
    @FXML
        private Button button_baseline;             // Button to change base line



    @FXML
    public void displayEstimationBase() {
        tabPane_requirementsEntry.setVisible(false);
    }

}
