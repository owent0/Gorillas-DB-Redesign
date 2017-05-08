package ROMdb.Controllers;

import ROMdb.Driver.Main;
import ROMdb.Helpers.SCICRRow;
import ROMdb.Models.AddSCICRModel;
import ROMdb.Models.MainMenuModel;
import ROMdb.Models.SCICRModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.ucanaccess.jdbc.UcanaccessSQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by chris on 3/15/2017.
 */
public class AddSCICRController
{
    @FXML private TextField field_title;
    @FXML private TextField field_number;

    @FXML private RadioButton radio_sc;
    @FXML private RadioButton radio_icr;

    @FXML private ComboBox<String> combo_build;
    @FXML private ComboBox<String> combo_baseline;

    @FXML private Button button_save;
    @FXML private Button button_cancel;


    /**
     * Initializes the controller and called on each load.
     */
    @FXML
    public void initialize()
    {
        try
        {
          combo_build.setItems(AddSCICRModel.fetchBuildsFromValCodesTableInDB());
        }
        catch(Exception e)
        {
            //e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Build values could not be loaded." + e, ButtonType.OK);
            alert.showAndWait();

        }
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
        String build = combo_build.getSelectionModel().getSelectedItem();
        try
        {
            String SCorICR = "SC";

            // Get either SC or ICR as a string value
            // based on the combo box selection.
            if(radio_icr.isSelected()) {
                SCorICR = radio_icr.getText();
            }

            String baselineId = AddSCICRModel.saveSCICR(baseline, SCorICR, field_number.getText(), field_title.getText(), build);

            // Prepare a new SCICRRow object to put into the list for the selected baseline.
            newSCICR = new SCICRRow(SCorICR, field_number.getText().trim(), field_title.getText().trim(), build, baseline);

            String impromptuSql = "SELECT [scicr_id] FROM SCICR WHERE [number]=? AND [baseline_id]=?";
            PreparedStatement ps = Main.newconn.prepareStatement(impromptuSql);
            ps.setString(1, field_number.getText());
            ps.setString(2, baselineId);
            ResultSet rs = ps.executeQuery();
            String id = "";
            while(rs.next())
            {
                id = rs.getString("scicr_id");
            }
            newSCICR.setID(Integer.parseInt(id));

            // Close the scene.
            closeScene();
            // Add it to the list of SCICRRow objects for that baseline.
            SCICRModel.getMap().get(baseline).add(newSCICR);

            /**
             * Since we have added a new SCICR to our running program, we need to update the list of scicrs
             *      for other windows and components, and we do that from the MainMenuModel class.
             */
            MainMenuModel.scicrs.add(newSCICR.getNumber());
        }
        catch (UcanaccessSQLException ucae)
        {
            if(ucae.getCause() instanceof SQLIntegrityConstraintViolationException)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't add SCICR to database." +
                        "\nSCICR number would not be unique for the current baseline.", ButtonType.OK);
                alert.showAndWait();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't add SCICR to database.", ButtonType.OK);
                alert.showAndWait();
            }
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

    /**
     * Updates a currently selected baseline.
     */
    @FXML
    private void updateCurrentBaseline()
    {
        String baseline = combo_baseline.getSelectionModel().getSelectedItem();
        MainMenuModel.setSelectedBaseline(baseline);
        combo_baseline.getSelectionModel().select(MainMenuModel.getSelectedBaseline());

    }

}
