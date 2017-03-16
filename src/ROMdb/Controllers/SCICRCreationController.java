package ROMdb.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 * Created by chris on 3/15/2017.
 */
public class SCICRCreationController {

    @FXML private Label label_scicr;
    @FXML private Label label_title;
    @FXML private Label label_number;
    @FXML private Label label_build;
    @FXML private Label label_baseline;

    @FXML private TextField field_title;
    @FXML private TextField field_number;
    @FXML private TextField field_build;

    @FXML private RadioButton radio_sc;
    @FXML private RadioButton radio_icr;

    @FXML private ComboBox<?> combo_baseline;

    @FXML private Button button_save;
    @FXML private Button button_newBaseline;



}
