package ROMdb.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Team Gorillas on 2/19/2017.
 */
public class Controller
{
    public FXMLLoader loader;

    private Connection conn = null;

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
    public void insertBaseline() {
        /*String baseline = combo_estimateBaseline.getSelectionModel().getSelectedItem();
        //System.out.println(baseline);
        String insertQuery = "UPDATE basicrom SET [slocspermanday]=?, [slocspermanmonth]=?, [cprs]=?, [IntergrationWeight]=?, "
                + "[UnitTestWeight]=?, [CodeWeight]=?, [DefaultSLOCS]=?, [DesignWeight]=?, [CPDDDocument]=?, [CPDDDate]=?, [Budget Upgrade]=?, "
                + "[Budget Maintenance]=?, [DDR/CWT SLOCS]=? WHERE [baseline]=?";

        PreparedStatement st = conn.prepareStatement(insertQuery);

        st.setString(1, field_staffDay.getText());
        st.setString(2, field_staffMonth.getText());
        st.setString(3, field_cprs.getText());
        st.setString(4, field_integrationWeight.getText());
        st.setString(5, field_unitTestingWeight.getText());
        st.setString(6, field_codeWeight.getText());
        st.setString(7, field_defaultSlocs.getText());
        st.setString(8, field_designWeight.getText());
        st.setString(9, field_cpddDocument.getText());
        st.setString(10, field_cpddDate.getText());
        st.setString(11, field_budgetUpgrade.getText());
        st.setString(12, field_budgetMaint.getText());
        st.setString(13, field_ddrCwtSlocs.getText());
        st.setString(14, baseline);

        System.out.println(st.toString());

        st.executeUpdate();*/
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
