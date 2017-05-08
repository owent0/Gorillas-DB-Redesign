package ROMdb.Controllers;

import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import ROMdb.Models.AddBaselineModel;
import ROMdb.Models.MainMenuModel;
import ROMdb.Models.SCICRModel;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import net.ucanaccess.jdbc.UcanaccessSQLException;

import java.sql.SQLIntegrityConstraintViolationException;


/**
 * Created by Anthony Orio on 3/20/2017.
 */
public class AddBaselineController {

    @FXML private TextField field_addBaseline;
    @FXML private Button button_cancelNewBaseline;
    @FXML private ListView<String> list_baselineList;
    final private Tooltip listViewTooltip = new Tooltip("To edit an item, double click.\nHit 'Enter' when done.");

    public AddBaselineController() {  }

    /**
     * Initializes the view with database information and factories.
     */
    @FXML
    public void initialize() {

        // Grab the list of baselines.
        ObservableList list = MainMenuModel.getBaselines();

        // Place the baselines inside of the list.
        list_baselineList.setItems(list);

        // Tool tip when mouse hovers over list component.
        list_baselineList.setTooltip(listViewTooltip);

        // Allow the list to be editable.
        // If someone wants to be able to edit baseline values, they would need to set this value to true
        list_baselineList.setEditable(false);

        // Make each list row an editable field.
        list_baselineList.setCellFactory(TextFieldListCell.forListView());

        // This is the action taken when user presses enter upon changing the field in the list.
        list_baselineList.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>()
        {
            @Override
            public void handle(ListView.EditEvent<String> t) {

                // If someone would want to be able to change baseline values they need to put code here to do so
            }
        });
    }

    /**
     * Writing new baseline to the baseline table in MS Access
     * @throws Exception If it fails.
     */
    @FXML
    public void writeBaseline() throws Exception {

        String baselineToAdd = field_addBaseline.getText().trim();

        // If entry field is blank.
        try
        {
            InputValidator.checkPatternDoesNotMatch(baselineToAdd, InputType.WHITE_SPACE);
        }
        catch(InputFormatException ife)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No baseline entered.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // If not alpha-numeric.
        try
        {
            InputValidator.checkPatternMatch(baselineToAdd, InputType.ALPHA_NUMERIC);
        }
        catch(InputFormatException ife)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Baseline.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        try
        {
            AddBaselineModel.writeBaseline(baselineToAdd);
            // Add the new baseline to the main model baseline map.
            MainMenuModel.baselines.add(baselineToAdd);
            MainMenuModel.setSelectedBaseline(baselineToAdd);
        }
        catch (UcanaccessSQLException ucae)
        {
            if(ucae.getCause() instanceof  SQLIntegrityConstraintViolationException)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't add baseline to database." +
                        "\nBaseline value would not be unique.", ButtonType.OK);
                alert.showAndWait();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't add baseline to database.", ButtonType.OK);
                alert.showAndWait();
            }
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't add baseline to database.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Closes the adding a baseline view
     */
    @FXML
    private void closeScene()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "For your changes to take effect please restart the program", ButtonType.OK);
        alert.showAndWait();
        System.exit(1);

        Stage stage = (Stage) button_cancelNewBaseline.getScene().getWindow();

        stage.close();
    }
}
