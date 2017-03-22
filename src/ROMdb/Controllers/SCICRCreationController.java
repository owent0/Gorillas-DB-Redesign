package ROMdb.Controllers;

import ROMdb.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

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

        try
        {
            if( errorsExist() ) {
                valid = false;
                throw new Exception();
            }

            valid = true;

            String SCorICR = "";

            if(radio_icr.isSelected()){
                SCorICR = radio_icr.getText();
            }else{
                SCorICR = radio_sc.getText();
            }

            newSCICR = new ScicrRow(SCorICR, field_number.getText(), field_title.getText(), field_build.getText(), baseline);

            // The query to insert the data from the fields.
            String insertQuery =    "INSERT INTO SCICRData ([Number], [Type], [Title], [Build], [Baseline]) VALUES (?, ?, ?, ?, ?)";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            /** Parse all of the information and stage for writing. */
            st.setString(1, field_number.getText());
            st.setString(2, SCorICR);
            st.setString(3, field_title.getText());
            st.setString(4, field_build.getText());
            st.setString(5, baseline);

            // Perform the update inside of the table of the database.
            st.executeUpdate();

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must fill out all fields.\n" + e, ButtonType.OK);
            alert.showAndWait();
        }

        if( valid ) {

            closeScene(button_save);
            SCICRController.map.get(baseline).add(newSCICR);
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
            InputValidator.checkPatternMatch(inputString, InputType.ALPHA_NUMERIC);
            InputValidator.checkPatternDoesNotMatch(inputString, InputType.WHITE_SPACE);
            return true;
        }
        catch(Exception e) {
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
