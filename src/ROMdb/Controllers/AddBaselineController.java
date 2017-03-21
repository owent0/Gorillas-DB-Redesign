package ROMdb.Controllers;

import ROMdb.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.PreparedStatement;


/**
 * Created by Anthony Orio on 3/20/2017.
 */
public class AddBaselineController {


    @FXML private ComboBox<?> combo_existingBaselines;
    @FXML private TextField field_addBaseline;
    @FXML private Button button_cancelNewBaseline;
    @FXML private Button button_addNewBaseline;
    @FXML private ListView<String> list_baselineList;

    public AddBaselineController() {  }

    @FXML
    public void initialize() {
        ObservableList list = MainMenuController.baselines;
        list_baselineList.setItems(list);
    }

    @FXML
    public void writeBaseline() throws Exception {

        String baselineToAdd = field_addBaseline.getText().trim();

        if(SCICRController.map.containsKey(baselineToAdd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Baseline already exists", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(baselineToAdd.equals("") || baselineToAdd == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No baseline entered.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        MainMenuController.baselines.add(baselineToAdd);

        try {
            // The query to insert the data from the fields.
            String insertQuery =    "INSERT INTO baseline ([baseline]) VALUES (?)";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            /** Parse all of the information and stage for writing. */
            st.setString(1, baselineToAdd);

            // Perform the update inside of the table of the database.
            st.executeUpdate();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't add baseline to database.", ButtonType.OK);
            alert.showAndWait();
        }

    }


    @FXML
    private void closeScene() {
        Stage stage = (Stage) button_cancelNewBaseline.getScene().getWindow();
        stage.close();
    }
}
