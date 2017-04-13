package ROMdb.Controllers;

import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import ROMdb.Helpers.RequirementsRow;
import ROMdb.Helpers.SCICRRow;
import ROMdb.Models.MainMenuModel;
import ROMdb.Models.RequirementsModel;
import ROMdb.Models.SCICRModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Created by Anthony Orio on 4/4/2017.
 *
 * This class associates with the window for adding a new
 * row within the requirements table.
 *
 */
public class AddRequirementController
{
    // Reference to the RequirementsController class.
    // Allows us to alter components, such as the table view
    // found within the requirements menu.
    public static RequirementsController requirementsController;

    // Combo boxes
    @FXML private ComboBox<String> combo_csc;
    @FXML private ComboBox<String> combo_csu;
    @FXML private ComboBox<String> combo_baseline;
    @FXML private ComboBox<String> combo_scicr;
    @FXML private ComboBox<String> combo_capability;
    @FXML private ComboBox<String> combo_ri;
    @FXML private ComboBox<String> combo_rommer;
    @FXML private ComboBox<String> combo_program;

    // Text fields
    @FXML private TextField field_doors; //alphanumeric
    @FXML private TextField field_paragraph; //alphanumeric
    @FXML private TextField field_added; //double
    @FXML private TextField field_changed; //double
    @FXML private TextField field_deleted; //double
    @FXML private TextField field_design; //double 0-100
    @FXML private TextField field_code; //double 0-100
    @FXML private TextField field_unitTest; //double 0-100
    @FXML private TextField field_integration; //double 0-100

    // Button
    @FXML private Button button_save;
    @FXML private Button button_saveNew;


    /**
     * This method will activate each time the window is created.
     */
    @FXML
    public void initialize()
    {
        // Fill the combo boxes with the components.
        this.occupyComboBoxes();

        // Fill the baseline combo with the current selected baseline from main menu.
        this.combo_baseline.getSelectionModel().select(MainMenuModel.getSelectedBaseline());

        // Only display the SC/ICR's associated with the selected baseline.
        this.changeSCICRToSelectedBaseline();
    }

    /**
     * This method will ensure that the SC/ICR combobox will only
     * contain the SC/ICR's associated with the baseline currently
     * selected in another combobox. This will prevent mixing of
     * SC/ICR's with other baselines that they don't belong to.
     */
    @FXML
    private void changeSCICRToSelectedBaseline()
    {
        // Set up a new observable list.
        ObservableList<String> scicrs = FXCollections.observableArrayList();

        // Set the currently selected baseline to this baseline.
        MainMenuModel.setSelectedBaseline(this.combo_baseline.getSelectionModel().getSelectedItem());

        // If there is no baseline currently selected then
        // we will just filter the combo when the user selects
        // it in the window.
        if(MainMenuModel.getSelectedBaseline().equals("Baseline"))
        {
            return;
        }

        // Get the SC/ICR row objects associated with this baseline.
        ObservableList<SCICRRow> rows = SCICRModel.map.get(this.combo_baseline.getSelectionModel().getSelectedItem());

        // Fill the temp list with the SC/ICR number.
        for(SCICRRow r : rows)
        {
            scicrs.add(r.getNumber());
        }

        // Set the items.
        this.combo_scicr.setItems(scicrs);
    }

    /**
     * This method allows you to save a requirements and entry and then instantly allows you to add another requirements
     * entry to the database.
     * @throws InputFormatException If any of the fields have invalid input
     */
    @FXML
    private void saveAndNewRequirements() throws InputFormatException {

        try {

            // Check for invalid inputs.
            this.errorChecking();

            // Build new row object.
            RequirementsRow newRow = new RequirementsRow
                    (
                            combo_csc.getValue(),
                            combo_csu.getValue(),
                            field_doors.getText().trim(),
                            field_paragraph.getText().trim(),
                            combo_baseline.getValue(),
                            combo_scicr.getValue(),
                            combo_capability.getValue(),
                            Double.parseDouble(field_added.getText().trim()),
                            Double.parseDouble(field_changed.getText().trim()),
                            Double.parseDouble(field_deleted.getText().trim()),
                            Double.parseDouble(field_design.getText().trim()),
                            Double.parseDouble(field_code.getText().trim()),
                            Double.parseDouble(field_unitTest.getText().trim()),
                            Double.parseDouble(field_integration.getText().trim()),
                            combo_ri.getValue(),
                            combo_rommer.getValue(),
                            combo_program.getValue()
                    );

            RequirementsModel.allReqData.add(newRow);
            RequirementsModel.insertNewReqRow(newRow);
            requirementsController.updateJTableWithFilteredReqData();

            field_doors.setText("");
            field_paragraph.setText("");
            field_added.setText("");
            field_changed.setText("");
            field_deleted.setText("");
            field_design.setText("");
            field_code.setText("");
            field_unitTest.setText("");
            field_integration.setText("");
            combo_ri.getSelectionModel().clearSelection();
            combo_baseline.getSelectionModel().select("Baseline");
        }
        catch(InputFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Ensure that the fields contain the correct values. Some fields can only contain numbers or alpha numeric characters.", ButtonType.OK);
            alert.showAndWait();
        }
        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Could not write new entry to database.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Sets the fields of the requirements entry to the components of the selected row to be copied
     * @param row the row to fill the fields of the requirements entry view.
     */
    @FXML
    public void setFieldsFromCopiedItem(RequirementsRow row) {

        field_doors.setText(row.getDoorsID());
        field_paragraph.setText(row.getParagraph());
        field_added.setText(Double.toString(row.getAdd()));
        field_changed.setText(Double.toString(row.getChange()));
        field_deleted.setText(Double.toString(row.getDelete()));
        field_design.setText(Double.toString(row.getDesignWeight()));
        field_code.setText(Double.toString(row.getCodeWeight()));
        field_unitTest.setText(Double.toString(row.getUnitTestWeight()));
        field_integration.setText(Double.toString(row.getIntegrationWeight()));

        combo_capability.getSelectionModel().select(row.getCapability());
        combo_rommer.getSelectionModel().select(row.getRommer());
        combo_program.getSelectionModel().select(row.getProgram());
        combo_baseline.getSelectionModel().select(row.getBaseline());
        combo_csc.getSelectionModel().select(row.getCsc());
        combo_csu.getSelectionModel().select(row.getCsu());
        combo_scicr.getSelectionModel().select(row.getScicr());
        combo_ri.getSelectionModel().select(row.getRi());

        // Only display the SC/ICR's associated with the selected baseline.
        this.changeSCICRToSelectedBaseline();
    }

    /**
     * Create the actual RequirementsRow object that will be inserted into the list
     * of current rows and displayed in the table view.
     * @throws InputFormatException if the input is unacceptable during the call to error check.
     */
    @FXML
    private void createNewRequirementObject() throws InputFormatException
    {
        try {

            // Check for invalid inputs.
            this.errorChecking();

            // Build new row object.
            RequirementsRow newRow = new RequirementsRow
                (
                        combo_csc.getValue(),
                        combo_csu.getValue(),
                        field_doors.getText().trim(),
                        field_paragraph.getText().trim(),
                        combo_baseline.getValue(),
                        combo_scicr.getValue(),
                        combo_capability.getValue(),
                        Double.parseDouble(field_added.getText().trim()),
                        Double.parseDouble(field_changed.getText().trim()),
                        Double.parseDouble(field_deleted.getText().trim()),
                        Double.parseDouble(field_design.getText().trim()),
                        Double.parseDouble(field_code.getText().trim()),
                        Double.parseDouble(field_unitTest.getText().trim()),
                        Double.parseDouble(field_integration.getText().trim()),
                        combo_ri.getValue(),
                        combo_rommer.getValue(),
                        combo_program.getValue()
                );

            RequirementsModel.allReqData.add(newRow);
            RequirementsModel.insertNewReqRow(newRow);
            requirementsController.updateJTableWithFilteredReqData();

            this.exit();
        }
        catch(InputFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Ensure that the fields contain the correct values. Some fields can only contain numbers or alpha numeric characters.", ButtonType.OK);
            alert.showAndWait();
        }
        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Could not write new entry to database.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    /**
     * Perform the error checking before accepting all of the values filled
     * into the text fields. This includes out of bound numbers and invalid
     * characters.
     * @throws InputFormatException if the input is not acceptable for the fields.
     */
    private void errorChecking() throws InputFormatException{

            // The value is alpha numeric only with no spaces.
            InputValidator.checkPatternMatch(field_doors.getText().trim(), InputType.ALPHA_NUMERIC);
            if(field_doors.getText().trim() == null || field_doors.getText().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }

            InputValidator.checkPatternMatch(field_paragraph.getText().trim(), InputType.ALPHA_NUMERIC);
            if(field_paragraph.getText().trim() == null || field_paragraph.getText().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }

            InputValidator.checkPatternMatch(field_added.getText().trim(), InputType.DOUBLE);
            InputValidator.checkPatternMatch(field_changed.getText().trim(), InputType.DOUBLE);
            InputValidator.checkPatternMatch(field_deleted.getText().trim(), InputType.DOUBLE);

            //Checking to see if the weights are either empty or the right input type.
            if (!InputValidator.checkEmptyString(field_design.getText().trim(), InputType.EMPTY_STRING)) {
                if (Double.parseDouble(field_design.getText().trim()) < 0 || Double.parseDouble(field_design.getText().trim()) > 100) {
                    throw new InputFormatException("The design weight is not within 0-100");
                }
            }

            if (!InputValidator.checkEmptyString(field_code.getText().trim(), InputType.EMPTY_STRING)) {
                if (Double.parseDouble(field_code.getText().trim()) < 0 || Double.parseDouble(field_code.getText().trim()) > 100) {
                    throw new InputFormatException("The code weight is not within 0-100");
                }
            }

            if (!InputValidator.checkEmptyString(field_unitTest.getText().trim(), InputType.EMPTY_STRING)) {
                if (Double.parseDouble(field_unitTest.getText().trim()) < 0 || Double.parseDouble(field_unitTest.getText().trim()) > 100) {
                    throw new InputFormatException("The unit test weight is not within 0-100");
                }
            }

            if (!InputValidator.checkEmptyString(field_integration.getText().trim(), InputType.EMPTY_STRING)) {
                if (Double.parseDouble(field_integration.getText().trim()) < 0 || Double.parseDouble(field_integration.getText().trim()) > 100) {
                    throw new InputFormatException("The integration weight is not within 0-100");
                }
            }

            if(combo_baseline.getValue() == null || combo_baseline.getValue().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }
            if(combo_capability.getValue() == null || combo_capability.getValue().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }
            if(combo_csc.getValue() == null || combo_csc.getValue().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }
            if(combo_csu.getValue() == null || combo_csu.getValue().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }
            if(combo_program.getValue() == null || combo_program.getValue().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }
            if(combo_ri.getValue() == null || combo_ri.getValue().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }
            if(combo_rommer.getValue() == null || combo_rommer.getValue().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }
            if(combo_scicr.getValue() == null || combo_scicr.getValue().trim().equals("")) {
                throw new InputFormatException("Value is empty");
            }


            // The value contains no white space.
            //InputValidator.checkPatternDoesNotMatch(inputString, InputType.WHITE_SPACE);
    }

    /**
     * Fills the combo boxes with the items that need to
     * be displayed.
     */
    private void occupyComboBoxes()
    {
        combo_csc.setItems(requirementsController.observableFilterMap.get("csc"));
        combo_csu.setItems(requirementsController.observableFilterMap.get("csu"));
        combo_baseline.setItems(requirementsController.observableFilterMap.get("baseline"));
        combo_capability.setItems(requirementsController.observableFilterMap.get("capability"));
        combo_ri.setItems(requirementsController.observableFilterMap.get("ri"));
        combo_rommer.setItems(requirementsController.observableFilterMap.get("rommer"));
        combo_program.setItems(requirementsController.observableFilterMap.get("program"));
    }

    /**
     * Exits the window when the user presses the cancel button.
     */
    @FXML
    private void exit()
    {
        Stage stage = (Stage) button_save.getScene().getWindow();
        stage.close();
    }
}
