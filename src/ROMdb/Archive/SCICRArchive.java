package ROMdb.Archive;

import ROMdb.Driver.Main;
import ROMdb.Helpers.SCICRRow;
import ROMdb.Models.EstimationBaseModel;
import ROMdb.Models.SCICRModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Archive for the SC/ICR data.
 *
 * Created by Anthony Orio on 4/6/2017.
 */
public class SCICRArchive extends  Archive<SCICRRow>
{
    /* The list of SCICRRows in the archive. */
    private ObservableList<SCICRRow> rows = FXCollections.observableArrayList();

    /**
     * The SCICRArchive constructor.
     */
    public SCICRArchive() {
        try {
            this.fillRows();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fetch rows.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * This method is deprecated.
     *
     * @param row The object to store into the records list.
     * @throws SQLException If the SQL query cannot complete properly.
     */
    @Override
    public void addRecord(SCICRRow row) throws SQLException {
        //this.moveToArchive(row);
        //this.deleteFromDatabase(row.getNumber(), "SCICRData");
    }

    /**
     * Adds a list of selected records to the database. A list of rows
     * can have 1 to n many rows to insert into the database.
     *
     * @param list The list of objects to append to the current list.
     * @throws SQLException If the SQL query cannot complete properly.
     */
    @Override
    public void addListOfRecords(ObservableList<SCICRRow> list) throws SQLException
    {
        String query = "UPDATE SCICR SET [archived] = ?, [date] = ? WHERE [scicr_id] = ?";
        PreparedStatement st = Main.newconn.prepareStatement(query);

        for (SCICRRow row : list)
        {
            /* Let's get todays date. */
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            row.setTimestamp(date.toString());

            st.setInt(1, 1);
            st.setDate(2, date);
            st.setInt(3, row.getId());
            rows.add(row);

            st.executeUpdate();
        }
    }

    /**
     * Removes a list of records from the archive and places them back into the
     * SCICRData table that contains all non-archived SCICRRows.
     *
     * @param list The list to bring back from the archive.
     * @throws SQLException If the SQL query cannot complete properly.
     */
    @Override
    public void removeListOfRecords(ObservableList<SCICRRow> list) throws SQLException
    {
        String query = "UPDATE SCICR SET [archived] = ? WHERE [scicr_id] = ?";
        PreparedStatement st = Main.newconn.prepareStatement(query);

        for (SCICRRow row : list) {
            st.setInt(1, 0);
            st.setInt(2, row.getId());
            st.executeUpdate();

            rows.remove(row);
        }
    }

    /**
     * Fill the archive table view in the GUI with the current archived
     * @throws SQLException If the SQL query could not complete properly.
     */
    @Override
    public void fillRows() throws SQLException
    {
        try {
            EstimationBaseModel.fillBaselineIDMap();
            SCICRModel.buildValCodeMap();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load archive table properly.", ButtonType.OK);
            alert.showAndWait();
        }

        String query = "SELECT [scicr_id], [number], [type], [title], [build_val_code_id], [baseline_id], [date] " +
                       "FROM SCICR " +
                       "WHERE [archived] = 1";
        Statement st = Main.newconn.createStatement();

        ResultSet rs = st.executeQuery(query);

        while (rs.next())
        {
            SCICRRow temp = new SCICRRow(rs.getString("type"),
                    rs.getString("number"),
                    rs.getString("title"),
                    SCICRModel.valMapByID.get(rs.getInt("build_val_code_id")),
                    EstimationBaseModel.baselineByID.get(rs.getInt("baseline_id"))
            );

            String timestamp = rs.getDate("date").toString();
            temp.setTimestamp(timestamp);
            temp.setID(rs.getInt("scicr_id"));

            this.rows.add(temp);
        }
    }

//    /**
//     * Moves a single SCICRRow object from the database table called
//     * SCICRData_Archive and places it back into memory.
//     *
//     * @param row The SCICRRow to place back into memory.
//     * @throws SQLException If the SQL query could not complete properly.
//     */
//    private void moveFromArchive(SCICRRow row) throws SQLException
//    {
//        // The query to insert the data from the fields.
//        String query =    "INSERT INTO SCICRData ([Number], [Type], [Title], [Build], [Baseline]) VALUES (?, ?, ?, ?, ?)";
//
//        // Create a new statement.
//        PreparedStatement st = Main.newconn.prepareStatement(query);
//
//        /* Parse all of the information and stage for writing. */
//        st.setString(1, row.getNumber());
//        st.setString(2, row.getType());
//        st.setString(3, row.getTitle());
//        st.setString(4, row.getBuild());
//        st.setString(5, row.getBaseline());
//
//        // Perform the update inside of the table of the database.
//        st.executeUpdate();
//
//        this.rows.remove(row);
//    }
//
//    /**
//     * Method that will move a single row to the archive table in the database
//     * called SCICRData_Archive.
//     *
//     * @param row The SCICRRow that will have its data written to the database.
//     * @throws SQLException If the SQL query could not complete properly.
//     */
//    private void moveToArchive(SCICRRow row) throws SQLException
//    {
//        /* Let's get todays date. */
//        long millis = System.currentTimeMillis();
//        java.sql.Date date = new java.sql.Date(millis);
//        row.setTimestamp(date.toString());
//
//        String query = "INSERT INTO SCICRData_Archive ([scicrData_id], [date_archived], [type], [number], [title], [build], [baseline]) " +
//                "       VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//        // Create a new statement.
//        PreparedStatement st = Main.newconn.prepareStatement(query);
//
//        /* Parse all of the information and stage for writing. */
//        st.setInt(1, row.getId());
//        st.setDate(2, date);
//        st.setString(3, row.getType());
//        st.setString(4, row.getNumber());
//        st.setString(5, row.getTitle());
//        st.setString(6, row.getBuild());
//        st.setString(7, row.getBaseline());
//
//        st.executeUpdate();
//
//        this.rows.add(row);
//    }
//
//    /**
//     * Deletes a row from the database when the user decides to add or remove
//     * a row from and to memory.
//     *
//     * @param number The number to search for.
//     * @param table The table to search in.
//     * @throws SQLException If the SQL query could not be complete properly.
//     */
//    private void deleteFromDatabase(String number, String table) throws SQLException {
//        // Set up statement for deleting from database.
//        PreparedStatement st = Main.conn.prepareStatement("DELETE FROM " + table + " WHERE [Number] = ?");
//
//        // Uses the primary key to locate in table.
//        st.setString(1, number);
//
//        // Perform the query.
//        st.executeUpdate();
//    }

    /**
     * Getter for the list of rows currently in memory in the archive.
     * @return The ObservableList of rows.
     */
    public ObservableList<SCICRRow> getRows()
    {
        return this.rows;
    }
}
