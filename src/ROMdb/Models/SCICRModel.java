package ROMdb.Models;

import ROMdb.Driver.Main;
import ROMdb.Helpers.SCICRRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Anthony Orio on 3/27/2017.
 */
public class SCICRModel {

    // This map keeps track of the baselines and the SC/ICR objects
    // associated with that baseline.
    public static HashMap<String, ObservableList<SCICRRow>> map = new HashMap<>();



    /**
     * Fills the table with the data from the database.
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
     */
    public static void updateChanges(SCICRRow rowToUpdate) throws SQLException {

            // The query to insert the data from the fields.
            String insertQuery = "UPDATE SCICRData SET [Type]=?, [Number]=?, [Title]=?, [Build]=?, [Baseline]=? WHERE [id]=?";

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
     */
    public static void deleteRowFromDatabase(String rowKey) throws SQLException {

            // Set up statement for deleting from database.
            PreparedStatement st = Main.conn.prepareStatement("DELETE FROM SCICRData WHERE Number = ?");

            // Uses the primary key to locate in table.
            st.setString(1,rowKey);

            // Perform the query.
            st.executeUpdate();
    }

    public static HashMap<String, ObservableList<SCICRRow>> getMap() {
        return map;
    }

    public static void setMap(HashMap<String, ObservableList<SCICRRow>> map) {
        SCICRModel.map = map;
    }
}

