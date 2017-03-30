package ROMdb.Controllers;

import ROMdb.Helpers.RequirementsRow;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

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

        createFactories();
    }

    private void createFactories()
    {
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


        tableColumn_csc.setCellValueFactory(ComboBoxTableCell.forTableColumn());
        tableColumn_csu.setCellValueFactory(ComboBoxTableCell.forTableColumn());
        tableColumn_doorsID.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_paragraph.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_baseline.setCellValueFactory(ComboBoxTableCell.forTableColumn());
        tableColumn_scicr.setCellValueFactory(ComboBoxTableCell.forTableColumn());
        tableColumn_capability.setCellValueFactory(ComboBoxTableCell.forTableColumn());
        tableColumn_add.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_change.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_delete.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_designWeight.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_codeWeight.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_unitTestWeight.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_integrationWeight.setCellValueFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_ri.setCellValueFactory(ComboBoxTableCell.forTableColumn());
        tableColumn_rommer.setCellValueFactory(ComboBoxTableCell.forTableColumn());
        tableColumn_program.setCellValueFactory(ComboBoxTableCell.forTableColumn());
        tableColumn_build.setCellValueFactory(ComboBoxTableCell.forTableColumn());


    }
}
