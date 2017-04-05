package ROMdb.Controllers;

import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import ROMdb.Helpers.RequirementsRow;
import ROMdb.Models.RequirementsModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Anthony Orio on 4/4/2017.
 */
public class AddRequirementController
{
    public static RequirementsController requirementsController;

    @FXML private ComboBox<String> combo_csc;
    @FXML private ComboBox<String> combo_csu;
    @FXML private ComboBox<String> combo_baseline;
    @FXML private ComboBox<String> combo_scicr;
    @FXML private ComboBox<String> combo_capability;
    @FXML private ComboBox<String> combo_ri;
    @FXML private ComboBox<String> combo_rommer;
    @FXML private ComboBox<String> combo_program;

    @FXML private TextField field_doors; //alphanumeric
    @FXML private TextField field_paragraph; //alphanumeric
    @FXML private TextField field_added; //double
    @FXML private TextField field_changed; //double
    @FXML private TextField field_deleted; //double
    @FXML private TextField field_design; //double 0-100
    @FXML private TextField field_code; //double 0-100
    @FXML private TextField field_unitTest; //double 0-100
    @FXML private TextField field_integration; //double 0-100

    @FXML private Button button_save;
    @FXML private Button button_cancel;

    @FXML
    public void initialize()
    {
        this.occupyComboBoxes();
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
            this.errorChecking();

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

            InputValidator.checkPatternMatch(field_design.getText().trim(), InputType.DOUBLE);
            if (Double.parseDouble(field_design.getText().trim()) < 0 || Double.parseDouble(field_design.getText().trim()) > 100) {
                throw new InputFormatException("The design weight is not within 0-100");
            }
            InputValidator.checkPatternMatch(field_code.getText().trim(), InputType.DOUBLE);
            if (Double.parseDouble(field_code.getText().trim()) < 0 || Double.parseDouble(field_code.getText().trim()) > 100) {
                throw new InputFormatException("The code weight is not within 0-100");
            }
            InputValidator.checkPatternMatch(field_unitTest.getText().trim(), InputType.DOUBLE);
            if (Double.parseDouble(field_unitTest.getText().trim()) < 0 || Double.parseDouble(field_unitTest.getText().trim()) > 100) {
                throw new InputFormatException("The unit test weight is not within 0-100");
            }
            InputValidator.checkPatternMatch(field_integration.getText().trim(), InputType.DOUBLE);
            if (Double.parseDouble(field_integration.getText().trim()) < 0 || Double.parseDouble(field_integration.getText().trim()) > 100) {
                throw new InputFormatException("The integration weight is not within 0-100");
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

    private void occupyComboBoxes()
    {
        combo_csc.setItems(requirementsController.observableFilterMap.get("csc"));
        combo_csu.setItems(requirementsController.observableFilterMap.get("csu"));
        combo_baseline.setItems(requirementsController.observableFilterMap.get("baseline"));
        combo_scicr.setItems(requirementsController.observableFilterMap.get("scicr"));
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
