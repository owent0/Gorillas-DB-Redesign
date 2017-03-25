package ROMdb.Controllers;

import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import ROMdb.Helpers.ScicrRow;
import ROMdb.Models.NewSCICRModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Created by chris on 3/15/2017.
 */
public class SCICRCreationController {

    @FXML private TextField field_title;
    @FXML private TextField field_number;
    @FXML private TextField field_build;

    @FXML private RadioButton radio_sc;
    @FXML private RadioButton radio_icr;

    @FXML private ComboBox<String> combo_baseline;

    @FXML private Button button_save;


    @FXML
    public void initialize()
    {
        combo_baseline.setItems(MainMenuController.baselines);
        combo_baseline.getSelectionModel().select(MainMenuController.selectedBaseline);
    }

    /**
     * This method will parse the information currently in the text
     * fields and write them into the database column specified by the
     * names within the insertQuery string.
     */
    @FXML
    public void saveSCICR()
    {
        boolean valid = false;
        ScicrRow newSCICR = null;

        // The currently selected baseline from the drop down.
        String baseline = combo_baseline.getSelectionModel().getSelectedItem();
        System.out.println(baseline);
        try
        {
            // If there are errors.
            if( errorsExist() ) {

                // It is not valid then.
                valid = false;

                // Throw the exception to leave.
                throw new Exception();
            }

            // It is valid if it got this far.
            valid = true;
            String SCorICR = "";

            // Get either SC or ICR as a string value
            // based on the combo box selection.
            if(radio_icr.isSelected()){
                SCorICR = radio_icr.getText();
            }else{
                SCorICR = radio_sc.getText();
            }
            System.out.println("Start Number testing");
            if(NewSCICRModel.isNumberUnique(field_number.getText(), baseline)) {
                System.out.println("Number is in baseline");
                throw new Exception();
            }

            NewSCICRModel.saveSCICR(baseline, SCorICR, field_number.getText(), field_title.getText(), field_build.getText());

            // Prepare a new ScicrRow object to put into the list for the selected baseline.
            newSCICR = new ScicrRow(SCorICR, field_number.getText().trim(), field_title.getText().trim(), field_build.getText().trim(), baseline);

            // If the input is valid.
            if( valid ) {

                // Close the scene.
                closeScene(button_save);
                // Add it to the list of ScicrRow objects for that baseline.
                //System.out.println(SCICRController.map.get(baseline).toString());
                SCICRController.map.get(baseline).add(newSCICR);

            }
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The input is incorrect.\n" + e, ButtonType.OK);
            alert.showAndWait();
        }


    }

    /**
     * Makes sure that the user inputted correct input that matches the patterns
     * specified by the programmer.
     * @param inputString the string to check validity for.
     * @return true if the string is valid.
     * @throws InputFormatException If input is not correct.
     */
    private boolean isValidInput(String inputString) throws InputFormatException
    {
        try{
            // The value is alpha numeric only with no spaces.
            InputValidator.checkPatternMatch(inputString, InputType.ALPHA_NUMERIC_SPACE);

            // The value contains no white space.
            InputValidator.checkPatternDoesNotMatch(inputString, InputType.WHITE_SPACE);
            return true;
        }
        catch(Exception e) {

            // If it failed.
            return false;
        }
    }

    /**
     * Checks to see if any errors exist, such as non alpha-numeric
     * characters.
     * @return true if there exists an error in the input.
     */
    @FXML
    private boolean errorsExist()
    {
        try
        {
            // Check that each field is valid to insert.
            if(!isValidInput(field_title.getText())
                    || !isValidInput(field_number.getText())
                    || !isValidInput(field_build.getText()))
            {
                return true;
            }
        }
        catch(InputFormatException ife)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input in one of the fields.", ButtonType.OK);
            alert.showAndWait();
        }

        if(combo_baseline.getSelectionModel().isEmpty())
        {
            return true;
        }

        return false;
    }

    /**
     * Creates the scene for adding a new baseline.
     * @throws IOException If I/O error occurs.
     */
    @FXML
    private void createNewBaseline() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/AddBaselineView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        stage.setTitle("Baseline Creation");
        stage.setScene(new Scene(root, 375, 255));
        stage.setResizable(false);
        stage.show();
    }


    /**
     * Closes the scene for adding a new SC/ICR entry.
     * @param button the button to grab the current scene from.
     */
    @FXML
    private void closeScene(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
