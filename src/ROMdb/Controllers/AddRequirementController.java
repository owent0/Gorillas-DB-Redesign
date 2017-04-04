package ROMdb.Controllers;

import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Anthony Orio on 4/4/2017.
 */
public class AddRequirementController
{
    @FXML private ComboBox<?> combo_csc;
    @FXML private ComboBox<?> combo_csu;
    @FXML private ComboBox<?> combo_baseline;
    @FXML private ComboBox<?> combo_scicr;
    @FXML private ComboBox<?> combo_capability;
    @FXML private ComboBox<?> combo_ri;
    @FXML private ComboBox<?> combo_rommer;
    @FXML private ComboBox<?> combo_program;

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

    private void errorChecking() throws InputFormatException{
        try{
            // The value is alpha numeric only with no spaces.
            InputValidator.checkPatternMatch(field_doors.getText().trim(), InputType.ALPHA_NUMERIC);

            InputValidator.checkPatternMatch(field_paragraph.getText().trim(), InputType.ALPHA_NUMERIC);
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

            // The value contains no white space.
            //InputValidator.checkPatternDoesNotMatch(inputString, InputType.WHITE_SPACE);
        }
        catch(Exception e) {

            // If it failed.
        }
    }

    @FXML
    private void exit()
    {
        Stage stage = (Stage) button_save.getScene().getWindow();
        stage.close();
    }
}
