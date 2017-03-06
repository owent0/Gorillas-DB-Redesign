package ROMdb.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Created by tom on 3/6/17.
 */
public class ScIcrController {

    @FXML private ComboBox<String> combo_baseline;
    @FXML private ComboBox<String> combo_ScIcr;

    @FXML private TextField field_number;
    @FXML private TextField field_title;
    @FXML private TextField field_build;

    @FXML private Button button_addBaseline;
    @FXML private Button button_addScIcr;

    @FXML private RadioButton radio_sc;
    @FXML private RadioButton radio_icr;

    @FXML private Label label_baseline;
    @FXML private Label label_ScIcr;
    @FXML private Label label_number;
    @FXML private Label label_title;
    @FXML private Label label_build;


}
