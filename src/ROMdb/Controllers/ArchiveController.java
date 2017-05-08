package ROMdb.Controllers;

import ROMdb.Helpers.RequirementsRow;
import ROMdb.Helpers.SCICRRow;
import ROMdb.Models.RequirementsModel;
import ROMdb.Models.SCICRModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * The controller that controls the Archive component and handles
 * the ArchiveModel.
 *
 * Created by Anthony Orio on 4/10/2017.
 */
public class ArchiveController {

    @FXML private Tab tab_scicr;

    @FXML private Button button_cancel;

    @FXML private TableView<SCICRRow> table_scicrArchive;
    @FXML private TableView<RequirementsRow> table_requirementsArchive;

    @FXML private TableColumn tableColumn_date;
    @FXML private TableColumn tableColumn_scicr;
    @FXML private TableColumn tableColumn_number;
    @FXML private TableColumn tableColumn_title;
    @FXML private TableColumn tableColumn_build;
    @FXML private TableColumn tableColumn_baseline;


    @FXML private TableColumn tableColumn_reqDate;
    @FXML private TableColumn tableColumn_reqCSC;
    @FXML private TableColumn tableColumn_reqCSU;
    @FXML private TableColumn tableColumn_reqDoors;
    @FXML private TableColumn tableColumn_reqParagraph;
    @FXML private TableColumn tableColumn_reqBaseline;
    @FXML private TableColumn tableColumn_reqSCICR;
    @FXML private TableColumn tableColumn_reqCapability;
    @FXML private TableColumn tableColumn_reqAdded;
    @FXML private TableColumn tableColumn_reqChanged;
    @FXML private TableColumn tableColumn_reqDeleted;
    @FXML private TableColumn tableColumn_reqDesign;
    @FXML private TableColumn tableColumn_reqCode;
    @FXML private TableColumn tableColumn_reqUnit;
    @FXML private TableColumn tableColumn_reqInt;
    @FXML private TableColumn tableColumn_reqRI;
    @FXML private TableColumn tableColumn_reqRom;
    @FXML private TableColumn tableColumn_reqProgram;
    @FXML private TableColumn tableColumn_reqBuild;

    /**
     * Initialize is called each time the program runs.
     */
    @FXML
    public void initialize()
    {

        table_scicrArchive.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table_requirementsArchive.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.createCellFactories();
        this.fillSCICRTable();
        this.fillRequirementsTable();
    }

    /**
     * Moves the selected rows from the archive back to the editable tables.
     * @throws Exception
     */
    @FXML
    private void restoreSelected() throws Exception
    {
        /* If its the SC/ICR tab. */
        if(tab_scicr.isSelected())
        {
            /* Create a list from the selected rows. */
            ObservableList<SCICRRow> rows = table_scicrArchive.getSelectionModel().getSelectedItems();
            rows.get(0); // We must do this to avoid a notorious javaFX bug.

            /* Convert it to an array list. */
            ArrayList<SCICRRow> list = new ArrayList<>(rows);

            /* Loop until the end of the list. */
            int size = list.size();
            for (int i = 0; i < size; i++) {
                SCICRRow temp = list.get(i);
                SCICRModel.getMap().get(temp.getBaseline()).add(temp);
                table_scicrArchive.getItems().remove(temp);
            }

            /* Bring it back to an ObservableList. */
            rows = FXCollections.observableList(list);

            /* Remove the list of rows from the archive table. */
            SCICRModel.archive.removeListOfRecords(rows);

        }
        /* If its a Requirements row.*/
        else
        {
            /* Set up the list from the selected rows in the table view.*/
            ObservableList<RequirementsRow> rows = table_requirementsArchive.getSelectionModel().getSelectedItems();
            rows.get(0); // Avoids a JavaFX null pointer bug.

            ArrayList<RequirementsRow> list = new ArrayList<>(rows);

            /* Loop until end. */
            int size = list.size();
            for (int i = 0; i < size; i++) {
                RequirementsRow temp = list.get(i);
                RequirementsModel.currentFilteredList.add(temp);
                table_requirementsArchive.getItems().remove(temp);
            }

            rows = FXCollections.observableList(list);
            RequirementsModel.archive.removeListOfRecords(rows);
        }
    }

    /**
     * Fills the SC/ICR table view in the archive.
     */
    private void fillSCICRTable()
    {
        table_scicrArchive.setItems(SCICRModel.archive.getRows());
    }

    /**
     * Fills the Requirements table view in the archive.
     */
    private void fillRequirementsTable()
    {
        table_requirementsArchive.setItems(RequirementsModel.archive.getRows());
    }

    /**
     * Creates the cell factories for each column inside of the table views.
     */
    private void createCellFactories()
    {
        tableColumn_date.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        tableColumn_scicr.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumn_build.setCellValueFactory(new PropertyValueFactory<>("build"));
        tableColumn_baseline.setCellValueFactory(new PropertyValueFactory<>("baseline"));
        tableColumn_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableColumn_number.setCellValueFactory(new PropertyValueFactory<>("number"));

        tableColumn_reqDate.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        tableColumn_reqCSC.setCellValueFactory(new PropertyValueFactory<>("csc"));
        tableColumn_reqCSU.setCellValueFactory(new PropertyValueFactory<>("csu"));
        tableColumn_reqDoors.setCellValueFactory(new PropertyValueFactory<>("doorsID"));
        tableColumn_reqParagraph.setCellValueFactory(new PropertyValueFactory<>("paragraph"));
        tableColumn_reqBaseline.setCellValueFactory(new PropertyValueFactory<>("baseline"));
        tableColumn_reqSCICR.setCellValueFactory(new PropertyValueFactory<>("scicr"));
        tableColumn_reqCapability.setCellValueFactory(new PropertyValueFactory<>("capability"));
        tableColumn_reqAdded.setCellValueFactory(new PropertyValueFactory<>("add"));
        tableColumn_reqChanged.setCellValueFactory(new PropertyValueFactory<>("change"));
        tableColumn_reqDeleted.setCellValueFactory(new PropertyValueFactory<>("delete"));
        tableColumn_reqDesign.setCellValueFactory(new PropertyValueFactory<>("designWeight"));
        tableColumn_reqCode.setCellValueFactory(new PropertyValueFactory<>("codeWeight"));
        tableColumn_reqUnit.setCellValueFactory(new PropertyValueFactory<>("unitTestWeight"));
        tableColumn_reqInt.setCellValueFactory(new PropertyValueFactory<>("integrationWeight"));
        tableColumn_reqRI.setCellValueFactory(new PropertyValueFactory<>("ri"));
        tableColumn_reqRom.setCellValueFactory(new PropertyValueFactory<>("rommer"));
        tableColumn_reqProgram.setCellValueFactory(new PropertyValueFactory<>("program"));
        tableColumn_reqBuild.setCellValueFactory(new PropertyValueFactory<>("build"));

    }

    /**
     * Closes the archive scene.
     */
    @FXML
    private void closeScene() {
        Stage stage = (Stage) button_cancel.getScene().getWindow();
        stage.close();
    }
}
