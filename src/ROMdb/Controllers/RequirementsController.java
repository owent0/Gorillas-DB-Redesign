package ROMdb.Controllers;

import ROMdb.Helpers.*;
import ROMdb.Models.MainMenuModel;
import ROMdb.Models.RequirementsModel;
import ROMdb.Reports.ReportGenerator;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Team Gorillas
 *
 * This is the main controller class that will control the requirements
 * menu table and the tabs associated with it. Due to the complexity of
 * this class many of the features associated with the GUI components
 * are located at the bottom of this file and should only be altered if
 * completely necessary. Look for the comment section below for where
 * this begins.
 */
public class RequirementsController
{

    /* For use when keeping the combo boxes updated */
    private ObservableList individuals = FXCollections.observableArrayList();
    private ObservableList programs = FXCollections.observableArrayList();

    /* For use with the list views on the Group Reports Tab */
    private ObservableList<String> groupChoices = FXCollections.observableArrayList();
    private ObservableList<String> groupSelection = FXCollections.observableArrayList();
    private int currSelectCount = 0;

    public HashMap<String, ObservableList> observableFilterMap = null;

    @FXML private ComboBox<String> combo_baseline;
    @FXML private ComboBox<String> combo_scicr;
    @FXML private ComboBox<ComboItem> combo_build;
    @FXML private ComboBox<ComboItem> combo_resp;
    @FXML private ComboBox<ComboItem> combo_csc;
    @FXML private ComboBox<ComboItem> combo_csu;
    @FXML private ComboBox<ComboItem> combo_capability;
    @FXML private ComboBox<ComboItem> combo_program;
    @FXML private ComboBox<ComboItem> combo_rommer;
    private final static int MAX_NUM_ROWS_VISIBLE_IN_COMBO_BOX = 10;

    @FXML private TextField field_paragraph;
    @FXML private TextField field_doors;

//    @FXML private Button button_archive;

    /* Complete tab components                          */
    @FXML private TextField field_completeDesign;
    @FXML private TextField field_completeCode;
    @FXML private TextField field_completeUnitTest;
    @FXML private TextField field_completeIntegration;

    @FXML private ComboBox<ComboItem> combo_completeRI;
    @FXML private ComboBox<ComboItem> combo_completeProgram;
    /* End complete tab components                          */

    /* Header / Footer Tab Components */
    @FXML private TextField field_header;
    @FXML private TextField field_footer;
    /* End Header / Footer Tab Components */

    /* DDR Tab Components */
//    @FXML private Button button_tracePortrait;
//    @FXML private Button button_traceLandscape;
    /* End DDR Tab Components*/

    /* Group Reports Tab Components */
//    @FXML private Button button_add;
//    @FXML private Button button_remove;
//    @FXML private Button button_clearChoices;
//    @FXML private Button button_SLOCs;
//    @FXML private Button button_status;

    @FXML private ListView<String> listview_choices;
    @FXML private ListView<String> listview_selections;
    /* End Group Reports Tab Components */


    @FXML private TableView<RequirementsRow> table_requirements;

    @FXML private TableColumn<RequirementsRow, ComboItem> tableColumn_csc;
    @FXML private TableColumn<RequirementsRow, ComboItem> tableColumn_csu;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_doorsID;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_paragraph;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_baseline;
    @FXML private TableColumn<RequirementsRow, ComboItem> tableColumn_build;
    @FXML private TableColumn<RequirementsRow, String> tableColumn_scicr;
    @FXML private TableColumn<RequirementsRow, ComboItem> tableColumn_capability;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_add;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_change;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_delete;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_designWeight;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_codeWeight;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_unitTestWeight;
    @FXML private TableColumn<RequirementsRow, Double> tableColumn_integrationWeight;
    @FXML private TableColumn<RequirementsRow, ComboItem> tableColumn_ri;
    @FXML private TableColumn<RequirementsRow, ComboItem> tableColumn_rommer;
    @FXML private TableColumn<RequirementsRow, ComboItem> tableColumn_program;

    public static boolean UPDATING_SCICR_VALUES = false;


    /**
     * This method will activate when the program loads.
     */
    @FXML
    public void initialize()
    {

        MainMenuController.requirementsController = this;

        // Create the factories for the GUI components.
        this.createFactories();

        // Creates the handlers for the GUI components.
        this.createTableHandlers();

        // Initializes the combo boxes with empty values.
        this.setCombosToEmptyValues(); // THIS NEEDS TO COME BEFORE this.createFilterHandlers (or nullPointer exception)

        // Creates the handlers for filtering table data.
        this.createFilterHandlers();

        // Creates the handler for when you click on the table.
        this.createTableViewClickHandler();

        table_requirements.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try
        {
            // Initialize each observable list of filters.
            this.initializeObservableFilterLists();

            // Fill the combo boxes in the main tab.
            this.occupyMainTabCombos();

            // Fill the combo boxes in the complete tab.
            this.occupyCompleteTabCombos();
        }
        catch(Exception e)
        {
            // Filters or combos could not be completed.
            //e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Could not load filter values for combo boxes." +
                            "Program closing.", ButtonType.OK);
            alert.showAndWait();
            System.exit(1);
        }

        // Set the column cells in the table view.
        this.setColumnCells();

        // Reference the requirements controller.
        AddRequirementController.requirementsController = this;

        /* Group Reports initial */
        this.fillListView();
        /* ---------------------------------- */

        /* The current filtered list should be the as allReqData initially. */
        RequirementsModel.currentFilteredList = RequirementsModel.allReqData;
    }

    /**
     * Updates the Baseline Combo Box on the Main tab to the currently selected baseline in the main menu
     */
    @FXML
    public void updateFilterByBaseline() {

        if(MainMenuModel.getSelectedBaseline() != null && MainMenuModel.getSelectedBaseline() != "Baseline"){
            combo_baseline.getSelectionModel().select(MainMenuModel.getSelectedBaseline());
            updateSCICR();
            pressClear();
        } else {
            combo_baseline.setValue("");
            //combo_baseline.getSelectionModel().clearSelection();
        }

    }

    /**
     * This method will take each combo box in the main tab and fill them with the data from
     * the database.
     */
    private void occupyMainTabCombos() {
        combo_scicr.setItems(this.observableFilterMap.get("scicr"));
        combo_capability.setItems(this.observableFilterMap.get("capability"));
        combo_csc.setItems(this.observableFilterMap.get("csc"));
        combo_csu.setItems(this.observableFilterMap.get("csu"));
        combo_program.setItems(this.observableFilterMap.get("program"));
        combo_resp.setItems(this.observableFilterMap.get("ri"));
        combo_rommer.setItems(this.observableFilterMap.get("rommer"));
        combo_baseline.setItems(new SortedList<String>(this.observableFilterMap.get("baseline"), Collator.getInstance()));
        combo_build.setItems(this.observableFilterMap.get("build"));

        int i = MAX_NUM_ROWS_VISIBLE_IN_COMBO_BOX;

        combo_scicr.setVisibleRowCount(i);
        combo_capability.setVisibleRowCount(i);
        combo_csc.setVisibleRowCount(i);
        combo_csu.setVisibleRowCount(i);
        combo_program.setVisibleRowCount(i);
        combo_resp.setVisibleRowCount(i);
        combo_rommer.setVisibleRowCount(i);
        combo_baseline.setVisibleRowCount(i);
        combo_build.setVisibleRowCount(i);

    }

    /**
     * Fills the combos with the list data for program and
     * responsible individual.
     */
    private void occupyCompleteTabCombos()
    {
        combo_completeProgram.setItems(this.observableFilterMap.get("program"));
        combo_completeRI.setItems(this.observableFilterMap.get("ri"));
    }

    /**
     * Method makes call to RequirementsModel and sets the Controller's HashMap of observable lists
     *      for each of the filtering combo boxes
     *
     * @throws SQLException If getFilterListsData cannot successfully complete its statement.
     */
    private void initializeObservableFilterLists() throws SQLException
    {
        // We need to grab all of the lists.
        ArrayList<MapList<ComboItem>> listOfLists = RequirementsModel.getFilterListsData();

        // Create a hashmap.
        this.observableFilterMap = new HashMap<String, ObservableList>();

        // Fill the hashmap with a key and the list that should
        // associate with this key from the observableFilterMap.
        for(MapList<ComboItem> fl : listOfLists)
        {
            ObservableList<ComboItem> ol = FXCollections.observableArrayList(fl.getList());
            this.observableFilterMap.put(fl.getName(), ol);
        }

        // add already existing ObservableLists
        this.observableFilterMap.put("baseline", MainMenuModel.baselines);
        this.observableFilterMap.put("scicr", MainMenuModel.scicrs);
    }

    /**
     * Neccessary to listen for the changed property of the combo boxes and text fields
     */
    private void createFilterHandlers()
    {
        // Define event change handlers for filtering combo boxes
        attachChangeListenerComboBoxStr(combo_baseline);
        attachChangeListenerComboBoxCI(combo_build);
        combo_scicr.valueProperty().addListener((ChangeListener)(arg, oldVal, newVal) -> {
            if(!UPDATING_SCICR_VALUES)
            {
                sendFiltersToModel();
                updateJTableWithFilteredReqData();
            }}
        );

        attachChangeListenerComboBoxCI(combo_resp);
        attachChangeListenerComboBoxCI(combo_csc);
        attachChangeListenerComboBoxCI(combo_csu);
        attachChangeListenerComboBoxCI(combo_capability);
        attachChangeListenerComboBoxCI(combo_program);
        attachChangeListenerComboBoxCI(combo_rommer);

        // Define event change handlers for filtering text fields
        attachChangeListenerTextField(field_paragraph);
        attachChangeListenerTextField(field_doors);
    }

    /**
     * Creates a ChangeListener object and assigns it to the comboBox passed in.
     *      The changeListener refreshes the JTable according to the current state of the filters
     *      whenver the changed event is called.
     * @param cb The combobox to attach the change listener to.
     *           This version is for the combo boxes that store ComboItems
     */
    private void attachChangeListenerComboBoxCI(ComboBox<ComboItem> cb)
    {
        cb.valueProperty().addListener((ChangeListener)(arg, oldVal, newVal) -> {
                    sendFiltersToModel();
                    updateJTableWithFilteredReqData();
                }
        );
    }

    /**
     * Creates a ChangeListener object and assigns it to the comboBox passed in.
     *      The changeListener refreshes the JTable according to the current state of the filters
     *      whenver the changed event is called.
     * @param cb The combobox to attach the change listener to.
     *           This version is for the combo boxes that just store Strings
     */
    private void attachChangeListenerComboBoxStr(ComboBox<String> cb)
    {
        cb.valueProperty().addListener((ChangeListener)(arg, oldVal, newVal) -> {
                    sendFiltersToModel();
                    updateJTableWithFilteredReqData();
                }
        );
    }

    /**
     * Creates a ChangeListener object and assigns it to the textField passed in.
     *      The changeListener refreshes the JTable according to the current state of the filters
     *      whenver the changed event is called.
     * @param tf The textfield to attach the change listener to.
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
        // combo_baseline.setValue(""); // we always filter by baseline
        combo_build.setValue(new ComboItem("", ""));
        combo_scicr.setValue("");

        combo_resp.setValue(new ComboItem("", ""));
        combo_csc.setValue(new ComboItem("", ""));
        combo_csu.setValue(new ComboItem("", ""));
        combo_capability.setValue(new ComboItem("", ""));
        combo_program.setValue(new ComboItem("", ""));
        combo_rommer.setValue(new ComboItem("", ""));

        field_doors.setText("");
        field_paragraph.setText("");

        combo_completeProgram.setValue(new ComboItem("", ""));
        combo_completeRI.setValue(new ComboItem("", ""));
    }

    /**
     * Creates the handler for being able to click on the table view
     * and perform an action such as right clicking to display a menu.
     */
    @FXML
    public void createTableViewClickHandler()
    {
        ContextMenu cm = createRightClickContextMenu();

        table_requirements.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
        {
            // Right mouse click.
            if(event.getButton() == MouseButton.SECONDARY)
            {
                table_requirements.setContextMenu(cm);
            }
        });
    }

    /**
     * Creates the menu that will display when you right click
     * on the table view.
     * @return The context menu that will display upon click.
     */
    private ContextMenu createRightClickContextMenu()
    {
        // Create ContextMenu
        ContextMenu cm = new ContextMenu();

        MenuItem addItem = new MenuItem("Add New Row");
        addItem.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    displayAddRequirementWindow();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not display add requirements window.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });

        MenuItem copyItem = new MenuItem("Copy Selected Row");
        copyItem.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                try {
                    RequirementsRow currRow = getSelectedRow();
                } catch(Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not copy requirements row.", ButtonType.OK);
                    alert.showAndWait();
                }
                RequirementsRow currRow = getSelectedRow();
                try {
                    displayAddRequirementWindowFromCopy(currRow);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not copy requirements row.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });

        MenuItem archiveItem = new MenuItem("Archive Selected Row(s)");
        archiveItem.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                    archiveSelected();
            }
        });

        // Add MenuItem to ContextMenu
        cm.getItems().addAll(addItem);
        cm.getItems().addAll(copyItem);
        cm.getItems().addAll(archiveItem);

        return cm;
    }

    /**
     * Displays the window for adding a new requirements row to the
     * table view when the user right clicks the context menu.
     * @throws IOException If the window could not successfully load.
     */
    private void displayAddRequirementWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/AddRequirementWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        /* Focus on this current window to prevent clicking on windows behind it. */
        Stage owner = (Stage) table_requirements.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Requirement Creation");
        stage.setScene(new Scene(root));
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Displays the window for adding a new requirements row to the
     * table view when the user right clicks the context menu. It takes in a current row to copy the selected rows
     * data into the fields of the requirements entry
     * @param currentRow the row to be copied
     * @throws IOException If the window could not successfully load.
     */
    private void displayAddRequirementWindowFromCopy(RequirementsRow currentRow) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/AddRequirementWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        /* Focus on this current window to prevent clicking on windows behind it. */
        Stage owner = (Stage) table_requirements.getScene().getWindow();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setTitle("Requirement Creation");
        stage.setScene(new Scene(root));
        AddRequirementController controller = loader.<AddRequirementController>getController();
        controller.setFieldsFromCopiedItem(currentRow);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    /**
     * This method gets called on an editCommit event in any field of any column in the tableView
     * It is used to call the Model's updateRowInDB method to save the changes made to a row object in the DB
     */
    private void saveRowEditChanges()
    {
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

    /**
     * Retrieve the selected row from the tableView and return the object contained there (RequirementsRow object)
     * @return The RequirementsRow object that is currently selected in the table view.
     */
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
     * Clears filter fields and dropboxes of their values (sets all values to empty string)
     *      (This will call their event handlers which will trigger methods to refresh the table view)
     */
    @FXML
    private void pressClear()
    {
        this.setCombosToEmptyValues();
        //RequirementsModel.currentFilteredList.clear();
        RequirementsModel.currentFilteredList = RequirementsModel.allReqData;
        //this.fillTable();
        this.setCombosToEmptyValues();
        RequirementsModel.currentFilteredList = RequirementsModel.allReqData;
    }

    /**
     * Method collects all values from the filtering comboBoxes and textFields
     *      and compiles them in an arraylist as FilterItems (easier to manage)
     *      This arraylist is then sent to the model
     *
     *      The names of these filters needs to be identical to the columns of the database that we will be filtering by.
     *      Otherwise the query will fail.
     */
    public void sendFiltersToModel()
    {
        // define a new list of filter criteria based on the current values of the filter boxes in the requirements view
        ArrayList<FilterItem> newListOfFilters = new ArrayList<FilterItem>();

        newListOfFilters.add(new FilterItem(combo_csc.getSelectionModel().getSelectedItem().getId(), "csc_val_code_id"));
        newListOfFilters.add(new FilterItem(combo_csu.getSelectionModel().getSelectedItem().getId(), "csu_val_code_id"));
        newListOfFilters.add(new FilterItem(field_doors.getText(), "doors_id"));
        newListOfFilters.add(new FilterItem(field_paragraph.getText(), "paragraph"));
        newListOfFilters.add(new FilterItem(combo_baseline.getSelectionModel().getSelectedItem(), "baseline_desc"));
        newListOfFilters.add(new FilterItem(combo_build.getSelectionModel().getSelectedItem().getId(), "build_val_code_id"));
        newListOfFilters.add(new FilterItem(combo_scicr.getSelectionModel().getSelectedItem(), "number"));
        newListOfFilters.add(new FilterItem(combo_capability.getSelectionModel().getSelectedItem().getId(), "capability_val_code_id"));
        newListOfFilters.add(new FilterItem(combo_resp.getSelectionModel().getSelectedItem().getId(), "responsible_individual_val_code_id"));
        newListOfFilters.add(new FilterItem(combo_rommer.getSelectionModel().getSelectedItem().getId(), "rommer_val_code_id"));
        newListOfFilters.add(new FilterItem(combo_program.getSelectionModel().getSelectedItem().getId(), "program_val_code_id"));

        // send these FilterItems to the model
        RequirementsModel.filters = newListOfFilters;
    }

    /**
     * Used to test to see if there are any legitimate filters in the filterList
     * If not return true (yes, Filters Are All Empty)
     * @param filters The list of filters to apply.
     * @return True if the input is correct. False if it doesn't
     */
    private boolean areFiltersAllEmpty(ArrayList<FilterItem> filters)
    {
        for(FilterItem fi : filters)
        {
            if(!fi.getValue().matches(InputType.WHITE_SPACE.getPattern()) && !fi.getName().equalsIgnoreCase("baseline_desc"))
            {
                return false;
            }
        }
        return true;
    }

    @FXML
    public void updateSCICR()
    {
        RequirementsModel.updateSCICR();
        combo_scicr.setItems(RequirementsModel.filterSCICR);
    }

    /**
     * Method uses a method in the model class to retrieve ReqData according to the current filter in the model.
     * If the filter is all empty then just reload all the full ReqData
     */
    public void updateJTableWithFilteredReqData()
    {
        // If filters are all empty, then load full results set into JTable
        if(RequirementsModel.filters == null || areFiltersAllEmpty(RequirementsModel.filters) == true)
        {
            try
            {
                table_requirements.setItems(RequirementsModel.getReqDataWithOnlyBaselineFilter());
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed in getReqDataWithOnlyBaselineFilter", ButtonType.OK);
                alert.showAndWait();
            }
        }
        else
        {
            try
            {
                table_requirements.setItems(RequirementsModel.getReqDataWithFilter());
            }
            catch(Exception e)
            {
                //Alert alert = new Alert(Alert.AlertType.ERROR, "Could not apply filter", ButtonType.OK);
                //alert.showAndWait();
            }
        }
    }


    /**
     * Performs the archive function when the user presses the archive button.
     */
    @FXML
    private void archiveSelected()
    {
        ObservableList<RequirementsRow> selectedRows = table_requirements.getSelectionModel().getSelectedItems();

        selectedRows.get(0);

        if(!selectedRows.isEmpty()) {
            Alert warningMsg = new Alert(Alert.AlertType.WARNING,
                    "Are you sure you want to archive this selection?", ButtonType.YES, ButtonType.NO);
            warningMsg.showAndWait();

            if(warningMsg.getResult() == (ButtonType.NO)) {
                return;
            }

            try {
                RequirementsModel.archiveRows(selectedRows);
            } catch (SQLException s) {
                warningMsg = new Alert(Alert.AlertType.WARNING, "Could not archive this entry.", ButtonType.OK);
                warningMsg.showAndWait();
            }
        }
    }


    /********** BEGIN % COMPLETE TAB FUNCTIONALITY **************/
    /**
     * Occupy combo boxes in the complete tab.
     */
    /*
    private void occupyComboBoxes()
    {
        for(RequirementsRow row : RequirementsModel.allReqData)
        {
            //String build = row.getBuild();
            String ri = row.getRi();
            String program = row.getProgram();

            //if(!builds.contains(build)) { builds.add(build); }
            if(!individuals.contains(ri)) { individuals.add(ri); }
            if(!programs.contains(program)) { programs.add(program); }
        }

        //combo_completeBuild.setItems(builds);
        combo_completeRI.setItems(individuals);
        combo_completeProgram.setItems(programs);
    }
    */

    /**
     * For updating the design column for all rows currently
     * selected.
     */
    @FXML
    private void updateDesign()
    {
        /** Confirmation of updating Design field.
        * Yes: then continue the attempt to write to DB
        * No: exit this method and don't write to DB
        */
        Alert warningMsg = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to update the Design field for all of the rows below?", ButtonType.YES, ButtonType.NO);
        warningMsg.showAndWait();

        if (warningMsg.getResult() == (ButtonType.NO))
        {
            return;
        }

        try
        {
            RequirementsModel.updateDoubleColumnInDB("Requirement", "design_percentage",
                    Double.parseDouble(field_completeDesign.getText()) );

            // clear the field once they've done the update
            field_completeDesign.setText("");

            table_requirements.refresh();
        }
        catch(NumberFormatException nfe)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Input must be a number between 0 - 100.", ButtonType.OK);
            alert.showAndWait();
        }
        catch(Exception e)
        {

        }
    }

    /**
     * For updating the code column for all rows currently
     * selected.
     */
    @FXML
    private void updateCode()
    {
        /** Confirmation of updating Code field.
         * Yes: then continue the attempt to write to DB
         * No: exit this method and don't write to DB
         */
        Alert warningMsg = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to update the Code field for all of the rows below?", ButtonType.YES, ButtonType.NO);
        warningMsg.showAndWait();

        if (warningMsg.getResult() == (ButtonType.NO))
        {
            return;
        }

        try
        {
            RequirementsModel.updateDoubleColumnInDB("Requirement", "code_percentage",
                    Double.parseDouble(field_completeCode.getText()) );

            // clear the field once they've done the update
            field_completeCode.setText("");

            table_requirements.refresh();
        }
        catch(NumberFormatException nfe)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Input must be a number between 0 - 100.", ButtonType.OK);
            alert.showAndWait();
        }
        catch(Exception e)
        {

        }
    }

    /**
     * For updating weight the unit testing column for all rows currently
     * selected.
     */
    @FXML
    private void updateUnitTest()
    {
        /** Confirmation of updating the UnitTest field.
         * Yes: then continue the attempt to write to DB
         * No: exit this method and don't write to DB
         */
        Alert warningMsg = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to update the Unit Test field for all of the rows below?", ButtonType.YES, ButtonType.NO);
        warningMsg.showAndWait();

        if (warningMsg.getResult() == (ButtonType.NO))
        {
            return;
        }

        try
        {
            RequirementsModel.updateDoubleColumnInDB("Requirement",
                    "unit_test_percentage", Double.parseDouble(field_completeUnitTest.getText()) );

            field_completeUnitTest.setText("");

            table_requirements.refresh();
        }
        catch(NumberFormatException nfe)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Input must be a number between 0 - 100.", ButtonType.OK);
            alert.showAndWait();
        }
        catch(Exception e)
        {

        }
    }

    /**
     * For updating the integration column for all rows currently
     * selected.
     */
    @FXML
    private void updateIntegration()
    {
        /** Confirmation of updating Integration field.
         * Yes: then continue the attempt to write to DB
         * No: exit this method and don't write to DB
         */
        Alert warningMsg = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to update the Integration field for all of the rows below?", ButtonType.YES, ButtonType.NO);
        warningMsg.showAndWait();

        if (warningMsg.getResult() == (ButtonType.NO))
        {
            return;
        }

        try
        {
            RequirementsModel.updateDoubleColumnInDB("Requirement",
                    "integration_percentage", Double.parseDouble(field_completeIntegration.getText()) );

            field_completeIntegration.setText("");

            table_requirements.refresh();
        }
        catch(NumberFormatException nfe)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Input must be a number between 0 - 100.", ButtonType.OK);
            alert.showAndWait();
        }
        catch(Exception e)
        {

        }
    }

    /**
     * For updating the responsible individual column for all
     * rows currently selected.
     */
    @FXML
    private void updateRI()
    {
        /** Confirmation of updating RI field.
         * Yes: then continue the attempt to write to DB
         * No: exit this method and don't write to DB
         */
        Alert warningMsg = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to update the RI field for all the rows below?", ButtonType.YES, ButtonType.NO);
        warningMsg.showAndWait();

        if (warningMsg.getResult() == (ButtonType.NO))
        {
            return;
        }

        try
        {
            RequirementsModel.updateTextColumnInDB("Requirement", "responsible_individual_val_code_id", combo_completeRI.getValue());
            updateJTableWithFilteredReqData();
        }
        catch(SQLException se)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Responsible Individual was unable to be updated.", ButtonType.OK);
            alert.showAndWait();
        }
        catch(Exception e)
        {

        }
    }

    /**
     * For updating program column for all rows currently selected.
     */
    @FXML
    private void updateProgram()
    {
        /** Confirmation of updating Program field.
         * Yes: then continue the attempt to write to DB
         * No: exit this method and don't write to DB
         */
        Alert warningMsg = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to update the Program field for all the rows below?", ButtonType.YES, ButtonType.NO);
        warningMsg.showAndWait();

        if (warningMsg.getResult() == (ButtonType.NO))
        {
            return;
        }

        try
        {
            RequirementsModel.updateTextColumnInDB("Requirement", "program_val_code_id", combo_completeProgram.getValue());
            updateJTableWithFilteredReqData();
        }
        catch(SQLException se)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Program was unable to be updated.", ButtonType.OK);
            alert.showAndWait();
        }
        catch(Exception e)
        {

        }
    }

    /**************** END % COMPLETE TAB FUNCTIONALITY ***********************/

    /**************** BEGIN HDR/FTR TAB FUNCTIONALITY ***********************/

    private String getHeader() {
        return field_header.getText();
    }

    private String getFooter() {
        return field_footer.getText();
    }

    /***************** END HDR/FTR TAB FUNCTIONALITY ************************/

    /**************** BEGIN DDR TAB (TAB 9)FUNCTIONALITY ********************/


    /**
     * this method calls the one in the ReportGenerator and then brings up an error popup if failed
     */
    @FXML
    private void generateDDRPortraitPDF()
    {

        String header = getHeader();
        String footer = getFooter();

        try {
            String fileName = ReportGenerator.generateDDR(false, header, footer);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to generate DDR Report - Portrait.",
                    ButtonType.OK);
            alert.showAndWait();
        }

        /*
        Document ddr_pdf_report = new Document();

        PdfWriter.getInstance(ddr_pdf_report, new FileOutputStream("src/ROMdb/Reports/ddr_pdf_report.pdf"));
        ddr_pdf_report.open();

        //ddr_pdf_report.addTitle("DDR Requirements");
        ddr_pdf_report.add(addHeadertoPDF());
        ddr_pdf_report.add(addDataContentToPDF());
        ddr_pdf_report.close();
        */

    } // end generateDDRPortraitPDF()

    /**
     * this method calls the one in the ReportGenerator and then brings up an error popup if failed
     */
    @FXML
    private void generateDDRLandscapePDF()
    {
        String header = getHeader();
        String footer = getFooter();

        try
        {
            String fileName = ReportGenerator.generateDDR(true, header, footer);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to generate DDR Report - Landscape.",
                    ButtonType.OK);
            alert.showAndWait();
        }

    }

    /**************** END DDR TAB FUNCTIONALITY *************************/


    /**************** Begin GROUPS TAB FUNCTIONALITY ********************/

    /**
     * Fills the list views in the group reports tab.
     */
    private void fillListView()
    {
        this.groupChoices.addAll(RequirementsModel.groupChoices);
        this.listview_selections.setItems(groupSelection);

        this.listview_choices.setItems(groupChoices);
    }



    /**
     * Add an item from the selection list view to the choice list view.
     */
    @FXML
    private void addGroupItem()
    {
        String selected = listview_choices.getSelectionModel().getSelectedItem();

        /* If nothing is selected. */
        if (selected == null)
            return;

        int limit = 6;

        /* If you can pick more items still. */
        if (currSelectCount < limit)
        {
            this.groupChoices.remove(selected);
            this.groupSelection.add(selected);
            this.currSelectCount++;
        }
    }

    /**
     * Remove a group item from the current selected group items.
     */
    @FXML
    private void removeGroupItem()
    {
        String selected = listview_selections.getSelectionModel().getSelectedItem();

        if (selected == null)
            return;

        /* Updates choices list. */
        this.groupChoices.add(selected);

        /* Updates selection list. */
        this.groupSelection.remove(selected);

        /* Decrement the number of choices the user has left. */
        this.currSelectCount--;
    }

    /**
     * Clears the selection list view when clear button pressed.
     */
    @FXML
    private void clearGroupItem()
    {
        this.groupChoices.clear();
        this.groupSelection.clear();
        this.groupChoices.addAll(RequirementsModel.groupChoices);
        this.currSelectCount = 0;
    }

    /**
     * When the user generates the SLOCs Add/Chg/Del Report.
     */
    @FXML
    private void pressSLOCSButton()
    {
        String header = getHeader();
        String footer = getFooter();

        try {
            ArrayList groups = new ArrayList(listview_selections.getItems());

            if (groups.size() == 0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "There is group names selected.", ButtonType.OK);
                alert.showAndWait();
            }
            else
                ReportGenerator.generateSLOCS(groups, header, footer);
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to generate SLOCs Add/Chg/Del Report.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * When the user generates the D/C/T/I report.
     */
    @FXML
    private void pressDCTIButton()
    {
        String header = getHeader();
        String footer = getFooter();

        try {
            ArrayList groups = new ArrayList(listview_selections.getItems());

            if (groups.size() == 0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "There is group names selected.", ButtonType.OK);
                alert.showAndWait();
            }
            else {
                ReportGenerator.generateDCTI(groups, header, footer);
            }
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to generate SLOCs Add/Chg/Del Report.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    /**************** END GROUPS TAB FUNCTIONALITY **********************/


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
     * Method sets the table column to be a combobox on click
     * It uses a special anon class to be able to be used with a ComboItem
     * @param col
     * @param ol
     */
    private void setCellFactoryToComboBoxWithComboItem(TableColumn<RequirementsRow, ComboItem> col,
                                                       ObservableList<ComboItem> ol)
    {
        col.setCellFactory(ComboBoxTableCell.forTableColumn(ol));
    }

    /**
     * Sets the appropriate column cells to a combo box cell.
     */
    private void setColumnCells()
    {
        setCellFactoryToComboBoxWithComboItem(tableColumn_csc, observableFilterMap.get("csc"));
        setCellFactoryToComboBoxWithComboItem(tableColumn_csu, observableFilterMap.get("csu"));
        //tableColumn_scicr.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), observableFilterMap.get("scicr")));
        setCellFactoryToComboBoxWithComboItem(tableColumn_capability, observableFilterMap.get("capability"));
        setCellFactoryToComboBoxWithComboItem(tableColumn_ri, observableFilterMap.get("ri"));
        setCellFactoryToComboBoxWithComboItem(tableColumn_rommer, observableFilterMap.get("rommer"));
        setCellFactoryToComboBoxWithComboItem(tableColumn_program, observableFilterMap.get("program"));
        //setCellFactoryToComboBoxWithComboItem(tableColumn_build, observableFilterMap.get("build"));
    }

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
        tableColumn_build.setCellValueFactory(new PropertyValueFactory<>("build"));
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
                //InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCsc(t.getNewValue().getValue());
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCsc_val_code_id(t.getNewValue().getId());

                // Save the change to the cell to the database.
                // This method is located in this class.
                saveRowEditChanges();

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
                //InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCsu(t.getNewValue().getValue());
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCsu_val_code_id(t.getNewValue().getId());
                saveRowEditChanges();
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
                saveRowEditChanges();
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
                InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_PERIOD);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setParagraph(t.getNewValue());
                saveRowEditChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Paragraph/Figure.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });

        /*  Capability Column  */
        tableColumn_capability.setOnEditCommit(t -> {
            try
            {
                //InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCapability(t.getNewValue().getValue());
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCapability_val_code_id(t.getNewValue().getId());
                saveRowEditChanges();
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
                InputValidator.checkNegative(t.getNewValue());

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setAdd(t.getNewValue());
                saveRowEditChanges();
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
                InputValidator.checkNegative(t.getNewValue());

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setChange(t.getNewValue());
                saveRowEditChanges();
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
                InputValidator.checkNegative(t.getNewValue());

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDelete(t.getNewValue());
                saveRowEditChanges();
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
                InputValidator.oneToHundredInclusive(t.getNewValue());

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDesignWeight(t.getNewValue());
                saveRowEditChanges();
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
                InputValidator.oneToHundredInclusive(t.getNewValue());

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setCodeWeight(t.getNewValue());
                saveRowEditChanges();
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
                InputValidator.oneToHundredInclusive(t.getNewValue());

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setUnitTestWeight(t.getNewValue());
                saveRowEditChanges();
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
                InputValidator.oneToHundredInclusive(t.getNewValue());

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setIntegrationWeight(t.getNewValue());
                saveRowEditChanges();
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
                //InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRi(t.getNewValue().getValue());
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setResponsible_individual_val_code_id(t.getNewValue().getId());
                saveRowEditChanges();
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
                //InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRommer(t.getNewValue().getValue());
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setRommer_val_code_id(t.getNewValue().getId());
                saveRowEditChanges();
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
                //InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC_SPACE);

                // Grab the new value enter into the cell.
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setProgram(t.getNewValue().getValue());
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setProgram_val_code_id(t.getNewValue().getId());
                saveRowEditChanges();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for Program.", ButtonType.OK);
                alert.showAndWait();

                // Refresh the table.
                table_requirements.refresh();
            }
        });
    }
}