package ROMdb.Controllers;

import ROMdb.*;
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
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.HashMap;

/**
 * Created by Team Gorillas on 3/14/2017
 */
public class SCICRController {

    // This map keeps track of the baselines and the SC/ICR objects
    // associated with that baseline.
    public static HashMap<String, ObservableList<ScicrRow>> map;

    @FXML private ComboBox<String> combo_ScIcrBaseline;

    @FXML private TableView table_ScIcr;

    @FXML private TableColumn tableColumn_scicr;
    @FXML private TableColumn tableColumn_number;
    @FXML private TableColumn tableColumn_baseline;
    @FXML private TableColumn tableColumn_title;
    @FXML private TableColumn tableColumn_build;


    /**
     * Initialize this view.
     */
    @FXML
    public void initialize()
    {
        map = new HashMap<>();

        // File the combo with the baselines list.
        combo_ScIcrBaseline.setItems(MainMenuController.baselines);

        // Call to create the factories.
        createFactories();

        // Call to fill the table view with the SC/ICR
        // entries in the database.
        fillTable();
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
     *  SimpleStringProperty located inside of the class ScicrRow. This is how it
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
        // tableColumn_baseline.setCellFactory(TextFieldTableCell.<String>forTableColumn()); // WE DON'T WANT TO BE ABLE TO EDIT BASELINES
        tableColumn_title.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_number.setCellFactory(TextFieldTableCell.<String>forTableColumn());


        tableColumn_scicr.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                    @Override
                    public void handle(CellEditEvent<ScicrRow, String> t)
                    {
                        ((ScicrRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setType(t.getNewValue());
                        saveCellChange();
                    }
                }
        );
        tableColumn_build.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                    @Override
                    public void handle(CellEditEvent<ScicrRow, String> t)
                    {
                        try
                        {
                            InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC);

                            ((ScicrRow) t.getTableView().getItems().get(
                                   t.getTablePosition().getRow())
                            ).setBuild(t.getNewValue());
                            saveCellChange();
                        }
                        catch(InputFormatException ife)
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for SC/ICR Build.", ButtonType.OK);
                            alert.showAndWait();

                            table_ScIcr.refresh();
                        }
                    }
                }
        );

        tableColumn_title.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                @Override
                public void handle(CellEditEvent<ScicrRow, String> t)
                {
                    try
                    {
                        InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC);

                        ((ScicrRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTitle(t.getNewValue());
                        saveCellChange();

                    }
                    catch(InputFormatException ife)
                    {

                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for SC/ICR Title.", ButtonType.OK);
                        alert.showAndWait();

                        table_ScIcr.refresh();
                    }
                }
            }
        );

        tableColumn_number.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                @Override
                public void handle(CellEditEvent<ScicrRow, String> t) {
                    try
                    {
                        InputValidator.checkPatternMatch(t.getNewValue(), InputType.ALPHA_NUMERIC);

                        ((ScicrRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setNumber(t.getNewValue());
                        saveCellChange();
                    }
                    catch (InputFormatException ife)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input for SC/ICR Number.", ButtonType.OK);
                        alert.showAndWait();

                        table_ScIcr.refresh();
                    }
                }
            }
        );
    }

    /**
     * Puts all the items into the correct columns.
     */
    @FXML
    public void switchTableData()
    {
        //ObservableList data = createRowObjects();
        String selectedBaseline = combo_ScIcrBaseline.getSelectionModel().getSelectedItem();
        ObservableList data = map.get(selectedBaseline);
        table_ScIcr.setItems(data);
    }


    /**
     * Fills the table with the data from the database.
     */
    @FXML
    private void fillTable() {
        ObservableList<String> baselines = MainMenuController.baselines;

        for( String baseline : MainMenuController.baselines ) {

            // Initialize rows list.
            ObservableList rows = FXCollections.observableArrayList();

            try {

                // Create query to grab all rows.
                String query = "SELECT * FROM SCICRData";

                // Create the statement to send.
                Statement st = Main.conn.createStatement();

                // Return the result set from this query.
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) { // Retrieve data from ResultSet

                    // We need to add a "All" feature to show all baselines.
                    if( rs.getString("Baseline").equals(baseline) ) {
                        ScicrRow temp = new ScicrRow(
                                rs.getString("Type"),
                                rs.getString("Number"),
                                rs.getString("Title"),
                                rs.getString("Build"),
                                rs.getString("Baseline")

                        );
                        temp.setID(rs.getInt("id"));
                        rows.add(temp);
                    }
                }
            }
            catch(Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fill table.", ButtonType.OK);
            }

            this.map.put(baseline, rows);
        }
    }


    /**
     * Creates the scene for inputting a new SC/ICR item.
     * @throws IOException If I/O error occurs.
     */
    @FXML
    private void createNewSCICR() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/SCICRCreation.fxml"));
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
        TablePosition selectedCell = (TablePosition) table_ScIcr.getSelectionModel().getSelectedCells().get(0);
        int currentRow = selectedCell.getRow();

        ScicrRow temp = (ScicrRow) table_ScIcr.getItems().get(currentRow);
        updateChanges(temp);
    }

    /**
     * Updates the database with any of the changes made.
     * @param rowToUpdate
     */
    private void updateChanges(ScicrRow rowToUpdate) {
        try
        {
            // The query to insert the data from the fields.
            String insertQuery = "UPDATE SCICRData SET [Type]=?, [Number]=?, [Title]=?, [Build]=?, [Baseline]=? WHERE [id]=?";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            st.setInt(6, rowToUpdate.getId());

            /** Parse all of the information and stage for writing. */
            st.setString(1, rowToUpdate.getType());
            st.setString(2, rowToUpdate.getNumber());
            st.setString(3, rowToUpdate.getTitle());
            st.setString(4, rowToUpdate.getBuild());
            st.setString(5, rowToUpdate.getBaseline());

            // Perform the update inside of the table of the database.
            st.executeUpdate();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not add entry.", ButtonType.OK);
        }
    }
}
