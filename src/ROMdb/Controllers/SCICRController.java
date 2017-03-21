package ROMdb.Controllers;

import ROMdb.Main;
import ROMdb.ScicrRow;
import com.sun.org.apache.xpath.internal.SourceTree;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Team Gorillas on 3/14/2017
 */
public class SCICRController {

    public static HashMap<String, ObservableList<ScicrRow>> map;

    @FXML private ComboBox<String> combo_ScIcrBaseline;

    @FXML private TableView table_ScIcr;

    @FXML private TableColumn tableColumn_scicr;
    @FXML private TableColumn tableColumn_number;
    @FXML private TableColumn tableColumn_baseline;
    @FXML private TableColumn tableColumn_title;
    @FXML private TableColumn tableColumn_build;


    @FXML
    public void initialize()
    {
        map = new HashMap<>();

        combo_ScIcrBaseline.setItems(fillBaselineFromDB());

        createFactories();
        fillTable();
    }

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
        tableColumn_baseline.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_title.setCellFactory(TextFieldTableCell.<String>forTableColumn());
        tableColumn_number.setCellFactory(TextFieldTableCell.<String>forTableColumn());


        tableColumn_scicr.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                    @Override
                    public void handle(CellEditEvent<ScicrRow, String> t) {
                        ((ScicrRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setType(t.getNewValue());
                        saveCellChange();
                    }
                }
        );
        tableColumn_build.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                    @Override
                    public void handle(CellEditEvent<ScicrRow, String> t) {
                        ((ScicrRow) t.getTableView().getItems().get(
                               t.getTablePosition().getRow())
                        ).setBuild(t.getNewValue());
                        saveCellChange();
                    }
                }
        );
        tableColumn_baseline.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                 @Override
                 public void handle(CellEditEvent<ScicrRow, String> t) {
                      ((ScicrRow) t.getTableView().getItems().get(
                              t.getTablePosition().getRow())
                      ).setBaseline(t.getNewValue());
                       saveCellChange();
                 }
            }
        );
        tableColumn_title.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                @Override
                public void handle(CellEditEvent<ScicrRow, String> t) {
                     ((ScicrRow) t.getTableView().getItems().get(
                             t.getTablePosition().getRow())
                     ).setTitle(t.getNewValue());
                     saveCellChange();
                 }
            }
        );

        tableColumn_number.setOnEditCommit( new EventHandler<CellEditEvent<ScicrRow, String>>() {
                @Override
                public void handle(CellEditEvent<ScicrRow, String> t) {
                        ((ScicrRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setNumber(t.getNewValue());
                        saveCellChange();
                    }
               }
        );
    }


    /**
     * This method will read all of the baselines currently stored within
     * the baseline database table.
     *
     * @return ObservableList the list containing the baseline from the baselines table.
     */
    @FXML
    private ObservableList<String> fillBaselineFromDB()
    {

        // The list to store the baselines in temporarily.
        ArrayList<String> baselines = new ArrayList<String>();

        try {
            // Grab all the baselines.
            String query = "SELECT * FROM baseline";

            // Create the statement.
            Statement st = Main.conn.createStatement();

            // Get the result set from the query.
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {// Retrieve data from ResultSet
                baselines.add(rs.getString("baseline")); //1st column of Table
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        // Convert to observable list for FXML purposes.
        ObservableList bases = FXCollections.observableArrayList(baselines);

        return bases;
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


    @FXML
    private void fillTable() {
        ObservableList<String> baselines = fillBaselineFromDB();

        for( String baseline : baselines ) {

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
                JOptionPane.showMessageDialog(null, e);
            }

            this.map.put(baseline, rows);
        }
    }


    @FXML
    private void createNewSCICR() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/SCICRCreation.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();

        stage.setTitle("New SC/ICR Creation");
        stage.setScene(new Scene(root, 325, 255));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void saveCellChange()
    {
        TablePosition selectedCell = (TablePosition) table_ScIcr.getSelectionModel().getSelectedCells().get(0);
        int currentRow = selectedCell.getRow();

        ScicrRow temp = (ScicrRow) table_ScIcr.getItems().get(currentRow);
        updateChanges(temp);
    }

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
            JOptionPane.showMessageDialog(null, e);
        }
    }
}
