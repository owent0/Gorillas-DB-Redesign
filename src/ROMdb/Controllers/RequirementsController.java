package ROMdb.Controllers;

import ROMdb.Helpers.RequirementsRow;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RequirementsController
{
    @FXML private TabPane requirementsEntryView;

    @FXML private ComboBox<?> combo_baseline;
    @FXML private ComboBox<?> combo_scicr;
    @FXML private ComboBox<?> combo_build;
    @FXML private ComboBox<?> combo_resp;
    @FXML private ComboBox<?> combo_csc;
    @FXML private ComboBox<?> combo_capability;
    @FXML private ComboBox<?> combo_program;
    @FXML private ComboBox<?> combo_rommer;
    @FXML private ComboBox<?> combo_sort;

    @FXML private TextField field_paragraph;
    @FXML private TextField field_foors;

    @FXML private Button button_clear;

    @FXML private TableView<RequirementsRow> table_requirements;

    @FXML
    public void initialize() {

        // This will prevent allow us to access the first row
        // when table is empty.
        table_requirements.getItems().add(null);
    }

    private void createFactories() {

    }
}
