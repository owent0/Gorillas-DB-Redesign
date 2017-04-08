package ROMdb.Controllers;

import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import ROMdb.Helpers.SCICRRow;
import ROMdb.Models.MainMenuModel;
import ROMdb.Models.SCICRModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Team Gorillas on 3/14/2017
 */
public class SCICRController {

    public ObservableList<SCICRRow> selectedRows = FXCollections.observableArrayList();

    @FXML private ComboBox<String> combo_ScIcrBaseline;

    @FXML private TableView table_ScIcr;

    @FXML private TableColumn tableColumn_scicr;
    @FXML private TableColumn tableColumn_number;
    @FXML private TableColumn tableColumn_baseline;
    @FXML private TableColumn tableColumn_title;
    @FXML private TableColumn tableColumn_build;

    @FXML private Button button_archive;

    /**
     * Initialize this view.
     */
    @FXML
    public void initialize()
    {
        //map = new HashMap<>();

        MainMenuModel.sCICRController = this;

        // File the combo with the baselines list.
        combo_ScIcrBaseline.setItems(MainMenuModel.getBaselines());

        // Call to create the factories.
        this.createFactories();

        // Call to fill the table view with the SC/ICR
        // entries in the database.
        this.fillTable();

        this.createEventHandlers();
    }

    /**
     * This method creates the factories for the specified components.
     * The factories set certain properties for the component, such as
     * making specific cells correlate to fields or combo box within
     * the table view.
     */
    private void createFactories()
    {
        /** This allows for the data for each row object to be stored in the cells.
         *  The String parameter of PropertyValueFactory is a reference to the
         *  SimpleStringProperty located inside of the class SCICRRow. This is how it
         *  "knows" the correct cells to place them into.
         **/
        tableColumn_scicr.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableColumn_build.setCellValueFactory(new PropertyValueFactory<>("build"));
        tableColumn_baseline.setCellValueFactory(new PropertyValueFactory<>("baseline"));
        tableColumn_title.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableColumn_number.setCellValueFactory(new PropertyValueFactory<>("number"));

        /** This sets the cell to use a combo box for selection. **/
        ObservableList choice = FXCollections.observableArrayList("SC","ICR");
        tableColumn_scicr.setCellFactory(ComboBoxTableCell.forTableColumn(choice));

        /** This allows for the cells to be editable like text fields by clicking. **/
        tableColumn_build.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_title.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_number.setCellFactory(TextFieldTableCell.<String>forTableColumn());

        /**
         * This is the event handler for when the user changes a specific cell within the table
         * view. In this case it handles the combo box located in the column SC/ICR and writes
         * the changes to the database by calling the method saveCellChange.
         */
        tableColumn_scicr.setOnEditCommit( new EventHandler<CellEditEvent<SCICRRow, String>>() {
                    @Override
                    public void handle(CellEditEvent<SCICRRow, String> t)
                    {
                        // Grab the new value enter into the cell.
                        ((SCICRRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setType(t.getNewValue());

                        // Save it to the database.
                        saveCellChange();
                    }
                }
        );

        /**
         * This is the event handler for when the user changes a specific cell within the table
         * view. In this case it handles the cells located in the column Build and writes
         * the changes to the database by calling the method saveCellChange.
         */
        tableColumn_build.setOnEditCommit( new EventHandler<CellEditEvent<SCICRRow, String>>() {
                    @Override
                    public void handle(CellEditEvent<SCICRRow, String> t)
                    {
                        try
                        {
                            InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                            // Grab the new value enter into the cell.
                            ((SCICRRow) t.getTableView().getItems().get(
                                   t.getTablePosition().getRow())
                            ).setBuild(t.getNewValue().trim());

                            // Save it to the database.
                            saveCellChange();
                        }
                        catch(InputFormatException ife)
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for SC/ICR Build.", ButtonType.OK);
                            alert.showAndWait();

                            // Refresh the table.
                            table_ScIcr.refresh();
                        }
                    }
                }
        );

        /**
         * This is the event handler for when the user changes a specific cell within the table
         * view. In this case it handles the cells located in the column Title and writes
         * the changes to the database by calling the method saveCellChange.
         */
        tableColumn_title.setOnEditCommit( new EventHandler<CellEditEvent<SCICRRow, String>>() {
                @Override
                public void handle(CellEditEvent<SCICRRow, String> t)
                {
                    try
                    {
                        InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                        // Grab the new value enter into the cell.
                        ((SCICRRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTitle(t.getNewValue().trim());

                        // Save it to the database.
                        saveCellChange();

                    }
                    catch(InputFormatException ife)
                    {

                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for SC/ICR Title.", ButtonType.OK);
                        alert.showAndWait();

                        // Refresh the table.
                        table_ScIcr.refresh();
                    }
                }
            }
        );

        /**
         * This is the event handler for when the user changes a specific cell within the table
         * view. In this case it handles the cells located in the column Number and writes
         * the changes to the database by calling the method saveCellChange.
         */
        tableColumn_number.setOnEditCommit( new EventHandler<CellEditEvent<SCICRRow, String>>() {
                @Override
                public void handle(CellEditEvent<SCICRRow, String> t) {
                    try
                    {
                        InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                        // Grab the new value enter into the cell.
                        ((SCICRRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setNumber(t.getNewValue().trim());

                        // Save it to the database.
                        saveCellChange();
                    }
                    catch (InputFormatException ife)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for SC/ICR Number.", ButtonType.OK);
                        alert.showAndWait();

                        // Refresh the table.
                        table_ScIcr.refresh();
                    }
                }
            }
        );
    }

    public void switchTableData() {
        // Gets the list associated with that baseline.
        ObservableList data = SCICRModel.getMap().get(MainMenuModel.getSelectedBaseline());

        // Puts data into the table view.
        table_ScIcr.setItems(data);
    }

    /**
     * Fills the table with the data from the database.
     */
    @FXML
    private void fillTable() {

        try {
            SCICRModel.fillTable();

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fill table.", ButtonType.OK);
            alert.showAndWait();
        }
    }


    /**
     * Creates the scene for inputting a new SC/ICR item.
     * @throws IOException If I/O error occurs.
     */
    @FXML
    private void createNewSCICR() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/AddSCICRWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        stage.setTitle("SC/ICR Creation");
        stage.setScene(new Scene(root, 325, 255));
        stage.setResizable(false);
        stage.show();
    }



    /**
     * Saves any changes to a specific cell within the table view.
     */
    @FXML
    private void saveCellChange()
    {
        // The currently selected cell.
        TablePosition selectedCell = (TablePosition) table_ScIcr.getSelectionModel().getSelectedCells().get(0);

        // Get the current row number.
        int currentRow = selectedCell.getRow();

        // Get the SCICRRow object associated with that row number.
        SCICRRow temp = (SCICRRow) table_ScIcr.getItems().get(currentRow);

        // Update the database.
        updateChanges(temp);
    }


    /**
     * Deletes a selected row from the table view and the database.
     */
    @FXML
    // TODO THIS METHOD WILL BE CALLED VIA ADMIN FUNCTION
    private void removeRow() {

        try {

            // Attempt to grab the cell that is selected.
            TablePosition selectedCell = (TablePosition) table_ScIcr.getSelectionModel().getSelectedCells().get(0);

            // Get the row number of that cell.
            int currentRow = selectedCell.getRow();

            // Get the SCICRRow object that is the candidate to delete.
            SCICRRow rowToDelete = (SCICRRow) table_ScIcr.getItems().get(currentRow);

            // Get the baseline from rowToDelete to use as a key in the hash map.
            String baseline = rowToDelete.getBaseline();

            // Remove the row from the observable list keyed by the baseline.
            SCICRModel.getMap().get(baseline).remove(rowToDelete);

            // Method call to delete from database.
            deleteRowFromDatabase(rowToDelete.getNumber());

        } catch(IndexOutOfBoundsException e) {
            return;
        }
    }

    /**
     * Deletes the selected row from the database.
     * @param rowKey the primary key to search for in the table.
     */
    private void deleteRowFromDatabase(String rowKey) {
        try {
            SCICRModel.deleteRowFromDatabase(rowKey);

        } catch(SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete entry from database.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Updates the database with any of the changes made.
     * @param rowToUpdate the row to update.
     */
    private void updateChanges(SCICRRow rowToUpdate) {

        try
        {
            SCICRModel.updateChanges(rowToUpdate);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not add entry.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /*Alert warningMsg = new Alert(Alert.AlertType.WARNING,
            "Are you sure you want to update the Design field for all of the rows below?", ButtonType.YES, ButtonType.NO);
        warningMsg.showAndWait();

        if (warningMsg.getResult() == (ButtonType.NO))
    {
        return;
    }*/
    @FXML
    private void archiveSelected()
    {
        if(!selectedRows.isEmpty()) {
            Alert warningMsg = new Alert(Alert.AlertType.WARNING,
                    "Are you sure you want to archive this selection?", ButtonType.YES, ButtonType.NO);
            warningMsg.showAndWait();

            if(warningMsg.getResult() == (ButtonType.NO)) {
                return;
            }

            try {
                SCICRModel.archiveRows(selectedRows);
            } catch (SQLException s) {
                warningMsg = new Alert(Alert.AlertType.WARNING, "Could not archive this entry.", ButtonType.OK);
                warningMsg.showAndWait();
            }
        }
    }

    private void createEventHandlers()
    {

        table_ScIcr.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
        {
            if(event.isControlDown())
            {
                table_ScIcr.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                selectedRows = table_ScIcr.getSelectionModel().getSelectedItems();
            }
        });
    }

}
