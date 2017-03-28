package ROMdb.Controllers;

import ROMdb.Helpers.SCICRRow;
import ROMdb.Models.AddSCICRModel;
import ROMdb.Models.MainMenuModel;
import ROMdb.Models.SCICRModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Created by chris on 3/15/2017.
 */
public class AddSCICRController {

    @FXML private TextField field_title;
    @FXML private TextField field_number;
    @FXML private TextField field_build;

    @FXML private RadioButton radio_sc;
    @FXML private RadioButton radio_icr;

    @FXML private ComboBox<String> combo_baseline;

    @FXML private Button button_save;
    @FXML private Button button_cancel;



    @FXML
    public void initialize()
    {

        combo_baseline.setItems(MainMenuModel.getBaselines());
        combo_baseline.getSelectionModel().select(MainMenuModel.getSelectedBaseline());
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
        SCICRRow newSCICR = null;

        // The currently selected baseline from the drop down.
        String baseline = combo_baseline.getSelectionModel().getSelectedItem();
        System.out.println(baseline);
        try
        {
            String SCorICR = "SC";

            // Get either SC or ICR as a string value
            // based on the combo box selection.
            if(radio_icr.isSelected()){
                SCorICR = radio_icr.getText();
            }
            System.out.println("Start Number testing");
            if(AddSCICRModel.isNumberUnique(field_number.getText(), baseline)) {
                System.out.println("Number is in baseline");
                throw new Exception();
            }

            AddSCICRModel.saveSCICR(baseline, SCorICR, field_number.getText(), field_title.getText(), field_build.getText());

            // Prepare a new SCICRRow object to put into the list for the selected baseline.
            newSCICR = new SCICRRow(SCorICR, field_number.getText().trim(), field_title.getText().trim(), field_build.getText().trim(), baseline);

            // Close the scene.
            closeScene();
            // Add it to the list of SCICRRow objects for that baseline.
            //System.out.println(SCICRController.map.get(baseline).toString());
            SCICRModel.getMap().get(baseline).add(newSCICR);

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The input is incorrect.\n" + e, ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Closes the scene for adding a new SC/ICR entry.
     */
    @FXML
    private void closeScene() {
        Stage stage = (Stage) button_save.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void updateCurrentBaseline() {
        String baseline = combo_baseline.getSelectionModel().getSelectedItem();
        MainMenuModel.setSelectedBaseline(baseline);
        combo_baseline.getSelectionModel().select(MainMenuModel.getSelectedBaseline());

    }

}
