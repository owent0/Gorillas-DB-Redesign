package ROMdb.Controllers;

import ROMdb.Main;
import ROMdb.ScicrRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Team Gorillas on 3/14/2017
 */
public class SCICRController {

    public static HashMap<String, ObservableList<ScicrRow>> map;

    @FXML private SCICRCreationController scicrCreationController;

    @FXML private ComboBox<String> combo_ScIcrBaseline;

    @FXML private TableView table_ScIcr;

    @FXML private TableColumn tableColumn_scicr;
    @FXML private TableColumn tableColumn_number;
    @FXML private TableColumn tableColumn_baseline;
    @FXML private TableColumn tableColumn_title;
    @FXML private TableColumn tableColumn_build;

    @FXML private Button button_newSCICR;

    @FXML private AnchorPane anchor_newScIcr;

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
                String query = "SELECT * FROM scdata";

                // Create the statement to send.
                Statement st = Main.conn.createStatement();

                // Return the result set from this query.
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) { // Retrieve data from ResultSet

                    // We need to add a "All" feature to show all baselines.
                    if( rs.getString("SC Baseline").equals(baseline) ) {
                        ScicrRow temp = new ScicrRow(
                                rs.getString("SC_ICR"),
                                rs.getString("SC_ICR Number"),
                                rs.getString("Title"),
                                rs.getString("Function"),
                                rs.getString("SC Baseline")
                        );
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
}
