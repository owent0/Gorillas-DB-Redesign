package ROMdb.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Created by Anthony Orio on 3/20/2017.
 */
public class AddBaselineController {


    @FXML private ComboBox<?> combo_existingBaselines;
    @FXML private TextField field_addBaseline;
    @FXML private Button button_cancelNewBaseline;
    @FXML private Button button_addNewBaseline;

    public AddBaselineController() {
    }

    public void initialize() {
    }

    @FXML
    private void closeScene(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
