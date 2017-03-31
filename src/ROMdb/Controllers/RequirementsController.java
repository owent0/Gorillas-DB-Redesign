package ROMdb.Controllers;

import ROMdb.Helpers.RequirementsRow;
import ROMdb.Models.RequirementsModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

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
    @FXML private Button button_save;

    @FXML private TableView<RequirementsRow> table_requirements;

    @FXML private TableColumn tableColumn_csc;
    @FXML private TableColumn tableColumn_csu;
    @FXML private TableColumn tableColumn_doorsID;
    @FXML private TableColumn tableColumn_paragraph;
    @FXML private TableColumn tableColumn_baseline;
    @FXML private TableColumn tableColumn_scicr;
    @FXML private TableColumn tableColumn_capability;
    @FXML private TableColumn tableColumn_add;
    @FXML private TableColumn tableColumn_change;
    @FXML private TableColumn tableColumn_delete;
    @FXML private TableColumn tableColumn_designWeight;
    @FXML private TableColumn tableColumn_codeWeight;
    @FXML private TableColumn tableColumn_unitTestWeight;
    @FXML private TableColumn tableColumn_integrationWeight;
    @FXML private TableColumn tableColumn_ri;
    @FXML private TableColumn tableColumn_rommer;
    @FXML private TableColumn tableColumn_program;
    @FXML private TableColumn tableColumn_build;

    @FXML
    public void initialize() {

        // This will prevent allow us to access the first row
        // when table is empty.
        //table_requirements.getItems().add(null);

        this.createFactories();
        this.fillTable();
    }

    private void fillTable() {
        try {
            RequirementsModel.fillTable();
            table_requirements.setItems(RequirementsModel.map.get("Skeleton Key"));
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fill table.", ButtonType.OK);
            alert.showAndWait();
        }

        table_requirements.getItems().add(null);
    }

    private void createFactories()
    {
        /**
         * These factories link the column to the RequirementsRow object. The string
         * property parameter is a reference to the SimpleStringProperty object found
         * inside of the RequirementsRow class. This allows us to reference rows of the
         * table view as these objects.
         */
        tableColumn_csc.setCellValueFactory(new PropertyValueFactory<>("csc"));
        tableColumn_csu.setCellValueFactory(new PropertyValueFactory<>("csu"));
        tableColumn_doorsID.setCellValueFactory(new PropertyValueFactory<>("doorsID"));
        tableColumn_paragraph.setCellValueFactory(new PropertyValueFactory<>("paragraph"));
        tableColumn_baseline.setCellValueFactory(new PropertyValueFactory<>("baseline"));
        tableColumn_scicr.setCellValueFactory(new PropertyValueFactory<>("scicr"));
        tableColumn_capability.setCellValueFactory(new PropertyValueFactory<>("capability"));
        tableColumn_add.setCellValueFactory(new PropertyValueFactory<>("add"));
        tableColumn_change.setCellValueFactory(new PropertyValueFactory<>("change"));
        tableColumn_delete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        tableColumn_designWeight.setCellValueFactory(new PropertyValueFactory<>("designWeight"));
        tableColumn_codeWeight.setCellValueFactory(new PropertyValueFactory<>("codeWeight"));
        tableColumn_unitTestWeight.setCellValueFactory(new PropertyValueFactory<>("unitTestWeight"));
        tableColumn_integrationWeight.setCellValueFactory(new PropertyValueFactory<>("integrationWeight"));
        tableColumn_ri.setCellValueFactory(new PropertyValueFactory<>("ri"));
        tableColumn_rommer.setCellValueFactory(new PropertyValueFactory<>("rommer"));
        tableColumn_program.setCellValueFactory(new PropertyValueFactory<>("program"));
        tableColumn_build.setCellValueFactory(new PropertyValueFactory<>("build"));


        /**
         * These factories will set the referenced column to a specific component
         * such as a text field or combo box in this case.
         */
        tableColumn_csc.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_csu.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_baseline.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_scicr.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_capability.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_ri.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_rommer.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_program.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_build.setCellFactory(col -> {
                ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();
                cell.setComboBoxEditable(true);
                return cell;
            }
        );

        tableColumn_doorsID.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_paragraph.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_add.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_change.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_delete.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_designWeight.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_codeWeight.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_unitTestWeight.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_integrationWeight.setCellFactory(TextFieldTableCell.<String>forTableColumn());
    }

    @FXML
    private void pressSave() {

        /*String csc =
        String csu
        String doors
        String paragraph
        String baseline
        String scicr
        String capability
        String add
        String change
        String delete
        String design
        String code
        String unitTest
        String integration
        String ri
        String rommer
        String program
        String build*/




        try {
            //RequirementsModel.insertIntoTable();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save entry", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
