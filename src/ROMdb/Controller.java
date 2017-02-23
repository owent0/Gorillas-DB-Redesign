package ROMdb;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Created by Team Gorillas on 2/19/2017.
 */
public class Controller {


    Estimation_Controller ec = new Estimation_Controller();

    /** These all link into the Scene Builder ID field for the components.
     *  It is how we can reference them in the java code. */
    @FXML
        private StackPane stackPane_main;           // Stack pane for switch panels.
    @FXML
        private TabPane tabPane_requirementsEntry;  // Pane with tabs at the top.
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


////// Estimation Pane ////////
    @FXML
        private TextField field_staffMonth;
    @FXML
        private TextField field_staffDay;
    @FXML
        private TextField field_seSupport;
    @FXML
        private TextField field_cscPmo;
    @FXML
        private TextField field_elementAdmin;
    @FXML
        private TextField field_elementTest;
    @FXML
        private TextField field_cpddGeneration;
    @FXML
        private TextField field_engineeringTest;
    @FXML
        private TextField field_cscMeitTesting;
    @FXML
        private TextField field_cprs;
    @FXML
        private TextField field_defaultSlocs;
    @FXML
        private TextField field_designWeight;
    @FXML
        private TextField field_codeWeight;
    @FXML
        private TextField field_unitTestingWeight;
    @FXML
        private TextField field_intergrationWeight;
    @FXML
        private TextField field_cpddDocument;
    @FXML
        private TextField field_cpddDate;
    @FXML
        private TextField field_budgetUpgrade;
    @FXML
        private TextField field_budgetMaint;
    @FXML
        private TextField field_ddrCwtSlocs;
    @FXML
        private Button button_estimateSubmit;
    @FXML
        private ComboBox combo_estimateBaseline;
    @FXML
        private ComboBox combo_element;
///// END ESTIMATION //////


    @FXML
    public void viewEstimationBase() {
        pane_estimationBase.setVisible(true);
        tabPane_requirementsEntry.setVisible(false);
    }

    @FXML
    public void viewRequirementsEntry() {
        pane_estimationBase.setVisible(false);
        tabPane_requirementsEntry.setVisible(true);
    }
}
