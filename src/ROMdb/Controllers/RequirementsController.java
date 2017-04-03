package ROMdb.Controllers;


import ROMdb.Helpers.FilterItem;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import ROMdb.Helpers.RequirementsRow;
import ROMdb.Models.RequirementsModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import javax.swing.event.ChangeEvent;
import java.util.ArrayList;

public class RequirementsController
{
    @FXML private TabPane requirementsEntryView;

    @FXML private ComboBox<String> combo_baseline;
    @FXML private ComboBox<String> combo_scicr;
    @FXML private ComboBox<String> combo_build;
    @FXML private ComboBox<String> combo_resp;
    @FXML private ComboBox<String> combo_csc;
    @FXML private ComboBox<String> combo_capability;
    @FXML private ComboBox<String> combo_program;
    @FXML private ComboBox<String> combo_rommer;
    @FXML private ComboBox<String> combo_sort;

    @FXML private TextField field_paragraph;
    @FXML private TextField field_doors;

    @FXML private Button button_clear;
    @FXML private Button button_save;
    @FXML private Button button_newRow;

    @FXML private TableView<RequirementsRow> table_requirements;

    @FXML private TableColumn<RequirementsRow, String> tableColumn_csc;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_csu;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_doorsID;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_paragraph;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_baseline;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_scicr;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_capability;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_add;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_change;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_delete;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_designWeight;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_codeWeight;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_unitTestWeight;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_integrationWeight;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_ri;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_rommer;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_program;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_build;

    @FXML
    public void initialize()
    {
        this.createFactories();
        this.createTableHandlers();
        this.fillTable();

        this.setCombosToEmptyValues(); // THIS NEEDS TO COME BEFORE this.createFilterHandlers (or nullPointer exception)
        this.createFilterHandlers();
    }

    // Includes combo boxes and text fields used as filters
    private void createFilterHandlers()
    {
        // Set all combo boxes to be editable
        combo_baseline.setEditable(true);
        combo_scicr.setEditable(true);
        combo_build.setEditable(true);
        combo_resp.setEditable(true);
        combo_csc.setEditable(true);
        combo_capability.setEditable(true);
        combo_program.setEditable(true);
        combo_rommer.setEditable(true);
        combo_sort.setEditable(true);

        // Define event change handlers for filtering combo boxes
        attachChangeListenerComboBox(combo_baseline);
        attachChangeListenerComboBox(combo_scicr);
        attachChangeListenerComboBox(combo_build);
        attachChangeListenerComboBox(combo_resp);
        attachChangeListenerComboBox(combo_csc);
        attachChangeListenerComboBox(combo_capability);
        attachChangeListenerComboBox(combo_program);
        attachChangeListenerComboBox(combo_rommer);
        attachChangeListenerComboBox(combo_sort);

        // Define event change handlers for filtering text fields
        attachChangeListenerTextField(field_paragraph);
        attachChangeListenerTextField(field_doors);
    }

    /**
     * Creates a ChangeListener object and assigns it to the comboBox passed in.
     *      The changeListener refreshes the JTable according to the current state of the filters
     *      whenver the changed event is called.
     * @param cb
     */
    private void attachChangeListenerComboBox(ComboBox<String> cb)
    {
        cb.valueProperty().addListener((ChangeListener)(arg, oldVal, newVal) -> {
                    sendFiltersToModel();
                    updateJTableWithFilteredReqData();
                }
        );
    }


    // TODO I would love to update the filter on every key press, but it does not look like the
    //      changed event is called until the enter key is pressed.
    //      if I could somehow call the enter keyevent after every key press this might work...
    //      Or some other solution
    /*
    private void attachChangeListenerComboBox(ComboBox<String> cb)
    {
        cb.getEditor().addEventFilter(KeyEvent.KEY_TYPED, e -> {
            System.out.println(e);
            e.consume();
            cb.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.ENTER.toString(), "", KeyCode.ENTER, false, false, false, false));
            sendFiltersToModel();
            updateJTableWithFilteredReqData();
        });
    }
    */

    /**
     * Creates a ChangeListener object and assigns it to the textField passed in.
     *      The changeListener refreshes the JTable according to the current state of the filters
     *      whenver the changed event is called.
     * @param tf
     */
    private void attachChangeListenerTextField(TextField tf)
    {
        tf.textProperty().addListener((ChangeListener)(arg, oldVal, newVal) -> {
                    sendFiltersToModel();
                    updateJTableWithFilteredReqData();
                }
        );
    }

    /**
     * Clears the filtering combo boxes and text fields of their data and sets them to the empty string
     */
    private void setCombosToEmptyValues()
    {
        combo_baseline.setValue("");
        combo_scicr.setValue("");
        combo_build.setValue("");
        combo_resp.setValue("");
        combo_csc.setValue("");
        combo_capability.setValue("");
        combo_program.setValue("");
        combo_rommer.setValue("");
        combo_sort.setValue("");

        field_doors.setText("");
        field_paragraph.setText("");
    }

    /**
     * Method called to fill the JTable with all ReqData at the load of the window
     */
    private void fillTable()
    {
        try
        {
            RequirementsModel.refreshAllReqDataFromDB();
            table_requirements.setItems(RequirementsModel.allReqData);
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fill table.", ButtonType.OK);
            alert.showAndWait();
        }
    }



    @FXML
    private void addNewRowWithDefaultsToJTable()
    {
        RequirementsRow row = new RequirementsRow("New","","","","","","",
                                                    0.0,0.0,0.0,0.0,0.0,0.0,0.0,
                                                    "","","",""
                                                 );
        table_requirements.getItems().add(row);
    }

    @FXML
    private void saveChanges() {
        try
        {
            RequirementsRow row = getSelectedRow();
            RequirementsModel.updateRowInDB(row);
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save entry", ButtonType.OK);
            alert.showAndWait();
        }
    }


    private RequirementsRow getSelectedRow()
    {
        // Attempt to grab the cell that is selected.
        TablePosition selectedCell = (TablePosition) table_requirements.getSelectionModel().getSelectedCells().get(0);

        // Get the row number of that cell.
        int currentRow = selectedCell.getRow();

        RequirementsRow row = (RequirementsRow) table_requirements.getItems().get(currentRow);

        return row;
    }

    /**
     * Todo this method
     */
    @FXML
    private void pressSave()
    {

    }

    /**
     * Clears filter fields and dropboxes of their values (sets all values to empty string)
     *      (This will call their event handlers which will trigger methods to refresh the table view)
     */
    @FXML
    private void pressClear()
    {
        this.setCombosToEmptyValues();
    }

    /**
     * Method collects all values from the filtering comboBoxes and textFields
     *      and compiles them in an arraylist as FilterItems (easier to manage)
     *      This arraylist is then sent to the model
     */
    public void sendFiltersToModel()
    {
        // define a new list of filter criteria based on the current values of the filter boxes in the requirements view
        ArrayList<FilterItem> newListOfFilters = new ArrayList<FilterItem>();

        newListOfFilters.add(new FilterItem(combo_csc.getSelectionModel().getSelectedItem(), "csc"));
        // newListOfFilters.add(new FilterItem(combo_csu.getSelectionModel().getSelectedItem(), "csu")); // TODO NEEDS TO BE ADDED ONCE IN VIEW
        newListOfFilters.add(new FilterItem(field_doors.getText(), "doors_id"));
        newListOfFilters.add(new FilterItem(field_paragraph.getText(), "paragraph"));
        newListOfFilters.add(new FilterItem(combo_baseline.getSelectionModel().getSelectedItem(), "baseline"));
        newListOfFilters.add(new FilterItem(combo_scicr.getSelectionModel().getSelectedItem(), "scicr"));
        newListOfFilters.add(new FilterItem(combo_capability.getSelectionModel().getSelectedItem(), "capability"));
        newListOfFilters.add(new FilterItem(combo_resp.getSelectionModel().getSelectedItem(), "ri"));
        newListOfFilters.add(new FilterItem(combo_rommer.getSelectionModel().getSelectedItem(), "rommer"));
        newListOfFilters.add(new FilterItem(combo_build.getSelectionModel().getSelectedItem(), "build"));

        // send these FilterItems to the model
        RequirementsModel.filters = newListOfFilters;
    }

    /**
     * Used to test to see if there are any legitimate filters in the filterList
     * If not return true (yes, Filters Are All Empty)
     */
    private boolean areFiltersAllEmpty(ArrayList<FilterItem> filters)
    {
        for(FilterItem fi : filters)
        {
            if(!fi.getValue().matches(InputType.WHITE_SPACE.getPattern()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Method uses a method in the model class to retrieve ReqData according to the current filter in the model.
     * If the filter is all empty then just reload all the full ReqData
     */
    public void updateJTableWithFilteredReqData()
    {
        // If filters are all empty, then load full results set into JTable
        if(areFiltersAllEmpty(RequirementsModel.filters) == true)
        {
            fillTable();
        }
        else
        {
            try
            {
                table_requirements.setItems(RequirementsModel.getReqDataWithFilter());
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not apply filter", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }


/**
 * All the code below here pertains to the views factories and handlers. It is advised that this code
 * is not edited due to the possibility of functionality not working correctly. The code below will
 * be clearly defined so that in the event changes need to be made the programmer can quickly identify
 * where and what they are programming.
 *
 * Below code contains:
 *      1) Factories - Cell factories allow for components, such as a combo box, to be placed into the cell of the entire column.
 *      2) Handlers - Handlers are used to perform actions when a component is changed in some way. IE: A field was changed so write
 *                      the change to the database.
 *
 */


    /**
     * This method will create all of the factories for the columns inside of the table view.
     * It will set up the cells to behave in a certain way, such as having combo boxes or
     * text fields within the cells in the column. It will also set references the
     * SimpleStringProperties inside of the RequirementsRow class. This is performed in
     * setCellValueFactory below.
     */
    private void createFactories() {

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

                    // Sets up the column of cells to be a combo box.
                    ComboBoxTableCell<RequirementsRow, String> cell = new ComboBoxTableCell();

                    // Makes these combo boxes editable, as in you can type into them.
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


        /**
         * These create the components in the column so that the cells are either combo boxes
         * or text fields that can be editable.
         */
        tableColumn_doorsID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumn_paragraph.setCellFactory(TextFieldTableCell.forTableColumn());


        /**
         * The next series of overrides is due to conversion between doubles and Strings. We must
         * override methods so that the fields inside of them can be changed between the two.
         */
        tableColumn_add.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {

            // Converts the Double located in cells of this column
            // into a String value. NOT USED BUT NEEDS OVERRIDE.
            @Override
            public String toString(Double object) {
                return object.toString();
            }

            // Converts the String representation of the double
            // into an actual Double. This is used because all values
            // inside of the table are represented as Strings.
            @Override
            public Double fromString(String string)
            {
                if (string.matches(InputType.POS_NEG_NUM.getPattern()))
                {
                    return Double.parseDouble(string);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for # Added.", ButtonType.OK);
                    alert.showAndWait();
                    return getSelectedRow().getAdd(); // else return item before change was attempted to be made
                }
            }
        }));
        tableColumn_change.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object.toString();
            }

            @Override
            public Double fromString(String string)
            {
                if (string.matches(InputType.POS_NEG_NUM.getPattern()))
                {
                    return Double.parseDouble(string);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for # Changed.", ButtonType.OK);
                    alert.showAndWait();
                    return getSelectedRow().getChange(); // else return item before change was attempted to be made
                }
            }
        }));
        tableColumn_delete.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object.toString();
            }

            @Override
            public Double fromString(String string)
            {
                if (string.matches(InputType.POS_NEG_NUM.getPattern()))
                {
                    return Double.parseDouble(string);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for # Deleted.", ButtonType.OK);
                    alert.showAndWait();
                    return getSelectedRow().getDelete(); // else return item before change was attempted to be made
                }
            }
        }));
        tableColumn_designWeight.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object.toString();
            }

            @Override
            public Double fromString(String string)
            {
                if (string.matches(InputType.POS_NEG_NUM.getPattern()))
                {
                    return Double.parseDouble(string);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Design Weight.", ButtonType.OK);
                    alert.showAndWait();
                    return getSelectedRow().getDesignWeight(); // else return item before change was attempted to be made
                }
            }
        }));
        tableColumn_codeWeight.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object.toString();
            }

            @Override
            public Double fromString(String string)
            {
                if (string.matches(InputType.POS_NEG_NUM.getPattern()))
                {
                    return Double.parseDouble(string);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Code Weight.", ButtonType.OK);
                    alert.showAndWait();
                    return getSelectedRow().getCodeWeight(); // else return item before change was attempted to be made
                }
            }
        }));
        tableColumn_unitTestWeight.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object.toString();
            }

            @Override
            public Double fromString(String string)
            {
                if (string.matches(InputType.POS_NEG_NUM.getPattern()))
                {
                    return Double.parseDouble(string);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Unit Test Weight.", ButtonType.OK);
                    alert.showAndWait();
                    return getSelectedRow().getUnitTestWeight(); // else return item before change was attempted to be made
                }
            }
        }));
        tableColumn_integrationWeight.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object.toString();
            }

            @Override
            public Double fromString(String string)
            {
                if (string.matches(InputType.POS_NEG_NUM.getPattern()))
                {
                    return Double.parseDouble(string);
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Integration Weight.", ButtonType.OK);
                    alert.showAndWait();
                    return getSelectedRow().getIntegrationWeight(); // else return item before change was attempted to be made
                }
            }
        }));
        /**     End override series of methods for String to Double conversions     */
    }


    /**
     * This method creates all of the handlers for editing a cell in a column.
     * Whenever the users presses enter after editing a cell the method for the
     * column respectively will perform its function. Lambda expressions are used
     * in the format:
     *      t -> {
     *          do something
     *      }
     *
     * Each column is identified by the comment above the lambda function.
     *
     * Todo insertion of the error checking will go here.
     *
     */
    private void createTableHandlers()
    {
        /*  CSC Column  */
        tableColumn_csc.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCsc(t.getNewValue());

                // Save the change to the cell to the database.
                // This method is located in this class.
                saveChanges();

            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for CSC.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  CSU Column  */
        tableColumn_csu.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCsu(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for CSU.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });
        /*  Doors ID Column  */
        tableColumn_doorsID.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDoorsID(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Doors ID.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Paragraph Column  */
        tableColumn_paragraph.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setParagraph(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Paragraph/Figure.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Baseline Column  */
        tableColumn_baseline.setOnEditCommit(t -> {
            try
            {
                // TODO This column is disabled in the view because we don't ever want to change this field...
                // So no actions will be done here?

                // Grab the new value enter into the cell.
                //(t.getTableView().getItems().get(t.getTablePosition().getRow())).setBaseline(t.getNewValue());
                //saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Baseline.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  SC/ICR Column  */
        tableColumn_scicr.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setScicr(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for SC/ICR.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Capability Column  */
        tableColumn_capability.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCapability(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Capability.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Add Column  */
        tableColumn_add.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.DOUBLE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setAdd(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Add.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Change Column  */
        tableColumn_change.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.DOUBLE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setChange(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Change.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Delete Column  */
        tableColumn_delete.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.DOUBLE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDelete(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Delete.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Design Column  */
        tableColumn_designWeight.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.DOUBLE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDesignWeight(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Design.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Code Column  */
        tableColumn_codeWeight.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.DOUBLE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodeWeight(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Code.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Unit Test Column  */
        tableColumn_unitTestWeight.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.DOUBLE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setUnitTestWeight(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Unit Test.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Integration Column  */
        tableColumn_integrationWeight.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.DOUBLE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setIntegrationWeight(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Integration.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  RI Column  */
        tableColumn_ri.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRi(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for RI.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Rommer Column  */
        tableColumn_rommer.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRommer(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Rommer.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Program Column  */
        tableColumn_program.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setProgram(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Program.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Build Column  */
        tableColumn_build.setOnEditCommit(t -> {
            try
            {
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setBuild(t.getNewValue());
                saveChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Build.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });
    }

}
