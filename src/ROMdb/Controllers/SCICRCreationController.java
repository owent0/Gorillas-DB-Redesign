package ROMdb.Controllers;

import ROMdb.Main;
import ROMdb.ScicrRow;
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

    @FXML
    private boolean errorsExist() {

        if(field_title.getText() == null || field_title.getText().trim().equals("")) {
            return true;
        }
        if(field_number.getText() == null || field_number.getText().trim().equals("")) {
            return true;
        }
        if(field_build.getText() == null || field_build.getText().trim().equals("")) {
            return true;
        }
        if(combo_baseline.getSelectionModel().isEmpty()) {
            return true;
        }

        return false;
    }

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


    @FXML
    private void closeScene(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
