package ROMdb.Controllers;

import ROMdb.Driver.Main;
import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import ROMdb.Helpers.SCICRRow;
import ROMdb.Models.MainMenuModel;
import ROMdb.Models.SCICRModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import java.sql.PreparedStatement;


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
        list_baselineList.setEditable(false);

        // Make each list row an editable field.
        list_baselineList.setCellFactory(TextFieldListCell.forListView());

        // This is the action taken when user presses enter upon changing the field in the list.
        list_baselineList.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> t) {
                // Grab the initial string value.
                String oldBaseline = list_baselineList.getSelectionModel().getSelectedItem();

                try
                {
                    // validate newBaseline text
                    InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC);

                    InputValidator.checkPatternDoesNotMatch(t.getNewValue(), InputType.WHITE_SPACE);

                    // Set the new string value.
                    list_baselineList.getItems().set(t.getIndex(), t.getNewValue());

                    // Grab the new string value.
                    String newBaseline = list_baselineList.getSelectionModel().getSelectedItem().trim();

                    // Call to write this new string the the database.
                    writeBaselineEditToDB(oldBaseline, newBaseline);

                }
                catch(InputFormatException ife)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Baseline.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }

    /**
     * Updating a baseline that is already in the baseline table in MS Access
     * Precondition: newBaseline is validated already
     * @param oldBaseline the current baseline in the baseline table
     * @param newBaseline the new value for the baseline in the baseline table
     *
     */
    @FXML
    public void writeBaselineEditToDB(String oldBaseline, String newBaseline) {

        try
        {
            // The query to insert the data from the fields.
            String insertQuery =    "UPDATE baseline SET [baseline]=? WHERE [baseline]=? ";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            // This will search for the column name.
            st.setString(2, oldBaseline);

            // Parse all of the information and stage for writing.
            st.setString(1, newBaseline);


            // Perform the update inside of the table of the database.
            st.executeUpdate();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot read database.\n" + e, ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Writing the baseline to the baseline table in MS Access
     * @throws Exception If it fails.
     */
    @FXML
    public void writeBaseline() throws Exception {

        String baselineToAdd = field_addBaseline.getText().trim();

        // If baseline is already in existence.
        if(SCICRModel.getMap().containsKey(baselineToAdd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Baseline already exists", ButtonType.OK);
            alert.showAndWait();
            return;
        }

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

        // Add the new baseline to the map.
        MainMenuModel.getBaselines().add(baselineToAdd);

        try {
            // The query to insert the data from the fields.
            String insertQuery =    "INSERT INTO baseline ([baseline]) VALUES (?)";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            /** Parse all of the information and stage for writing. */
            st.setString(1, baselineToAdd);

            // Perform the update inside of the table of the database.
            st.executeUpdate();

            ObservableList<SCICRRow> temp = FXCollections.observableArrayList();
            SCICRModel.getMap().put(baselineToAdd, temp);

            // The query to insert the data from the fields.
            insertQuery =    "INSERT INTO basicrom ([slocspermanday], [slocspermanmonth], [cprs], [IntergrationWeight],"
                    + "[UnitTestWeight], [CodeWeight], [DefaultSLOCS], [DesignWeight], [CPDDDocument], [CPDDDate], [Budget Upgrade], "
                    + "[Budget Maintenance], [DDR/CWT SLOCS], [baseline]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            st = Main.conn.prepareStatement(insertQuery);

            st.setString(1, "0.0");
            st.setString(2, "0.0");
            st.setString(3, "0.0");
            st.setString(4, "0.0");
            st.setString(5, "0.0");
            st.setString(6, "0.0");
            st.setString(7, "0.0");
            st.setString(8, "0.0");
            st.setString(9, "0.0");
            st.setString(10, "0.0");
            st.setString(11, "0.0");
            st.setString(12, "0.0");
            st.setString(13, "0.0");
            st.setString(14, baselineToAdd);

            // Perform the update inside of the table of the database.
            st.executeUpdate();

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
    private void closeScene() {
        Stage stage = (Stage) button_cancelNewBaseline.getScene().getWindow();
        stage.close();
    }
}
