package ROMdb.Models;

import ROMdb.Archive.SCICRArchive;
import ROMdb.Driver.Main;
import ROMdb.Helpers.SCICRRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anthony Orio on 3/27/2017.
 */
public class SCICRModel {

    // This map keeps track of the baselines and the SC/ICR objects
    // associated with that baseline.
    public static HashMap<String, ObservableList<SCICRRow>> map = new HashMap<>();
    public static SCICRArchive archive = new SCICRArchive();


    /**
     * Fills the table with the data from the database.
     * @throws SQLException If the query statement could not successfully complete.
     */
    public static void fillTable() throws SQLException {

        // For each baseline.
        ObservableList<String> baselines = MainMenuModel.getBaselines();
        for( String baseline : baselines) {

            // Initialize rows list.
            ObservableList rows = FXCollections.observableArrayList();

            // Create query to grab all rows.
            String query = "SELECT * FROM SCICRData";

            // Create the statement to send.
            Statement st = Main.conn.createStatement();

            // Return the result set from this query.
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) { // Retrieve data from ResultSet

                // We need to add a "All" feature to show all baselines.
                if( rs.getString("Baseline").equals(baseline) ) {
                    SCICRRow temp = new SCICRRow(
                            rs.getString("Type"),
                            rs.getString("Number"),
                            rs.getString("Title"),
                            rs.getString("Build"),
                            rs.getString("Baseline")

                    );
                    temp.setID(rs.getInt("SCICR_ID"));
                    rows.add(temp);
                }
            }

           SCICRModel.getMap().put(baseline, rows);
        }
    }

    /**
     * Updates the database with any of the changes made.
     * @param rowToUpdate the row to update.
     * @throws SQLException If the query could not successfully complete.
     */
    public static void updateChanges(SCICRRow rowToUpdate) throws SQLException {

            // The query to insert the data from the fields.
            String insertQuery = "UPDATE SCICRData SET [Type]=?, [Number]=?, [Title]=?, [Build]=?, [Baseline]=? WHERE [SCICR_ID]=?";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            st.setInt(6, rowToUpdate.getId());

            /** Parse all of the information and stage for writing. */
            st.setString(1, rowToUpdate.getType().trim());
            st.setString(2, rowToUpdate.getNumber().trim());
            st.setString(3, rowToUpdate.getTitle().trim());
            st.setString(4, rowToUpdate.getBuild().trim());
            st.setString(5, rowToUpdate.getBaseline().trim());

            // Perform the update inside of the table of the database.
            st.executeUpdate();
    }

    /**
     * Deletes the selected row from the database.
     * @param rowKey the primary key to search for in the table.
     * @throws SQLException If the query statement could not successfully complete.
     */
    public static void deleteRowFromDatabase(String rowKey) throws SQLException {

        // Set up statement for deleting from database.
        PreparedStatement st = Main.conn.prepareStatement("DELETE FROM SCICRData WHERE Number = ?");

        // Uses the primary key to locate in table.
        st.setString(1,rowKey);

        // Perform the query.
        st.executeUpdate();
    }

    /**
     * Archives a list of SCICRRows into the SCICRData_Archive table in the database.
     * @param rows The list of rows to archive.
     * @throws SQLException If the SQL Query could not complete successfully.
     */
    public static void archiveRows(ObservableList<SCICRRow> rows) throws SQLException
    {
        archive.addListOfRecords(rows);

        ArrayList<SCICRRow> list = new ArrayList<>(rows);

        int size = list.size();
        for(int i = 0; i < size; i++)
        {
            SCICRRow temp = list.get(i);
            map.get(MainMenuModel.getSelectedBaseline()).remove(temp);
        }
    }


    /**
     * Get the map that keeps track of all the SC/ICRs for a baseline
     * @return the hashmap containing SC/ICRs
     */
    public static HashMap<String, ObservableList<SCICRRow>> getMap() {
        return map;
    }

    /**
     * Set the map that keeps track of all the SC/ICRs for a baseline
     * @param map the new map to store the values of all the SC/ICRs for a baseline
     */
    public static void setMap(HashMap<String, ObservableList<SCICRRow>> map) {
        SCICRModel.map = map;
    }
}

