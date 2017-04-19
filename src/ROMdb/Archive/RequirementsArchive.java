package ROMdb.Archive;

import ROMdb.Driver.Main;
import ROMdb.Helpers.RequirementsRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Archive for the Requirements data.
 *
 * Created by Anthony Orio on 4/10/2017.
 *
 */
public class RequirementsArchive extends Archive<RequirementsRow> {

    /* The list of RequirementRows in the archive. */
    private ObservableList<RequirementsRow> rows = FXCollections.observableArrayList();

    /**
     * The RequirementArchive constructor.
     */
    public RequirementsArchive()
    {
        try
        {
            /* Fill the table view with the rows from the database. */
            this.fillRows();
        }
        catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fetch rows.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * This method is deprecated.
     * @param object The object to store into the records list.
     * @throws SQLException If the SQL query cannot complete properly.
     */
    @Override
    public void addRecord(RequirementsRow object) throws SQLException {}

    /**
     * Adds a list of selected records to the database. A list of rows
     * can have 1 to n many rows to insert into the database.
     *
     * @param list The list of objects to append to the current list.
     * @throws SQLException If the SQL query cannot complete properly.
     */
    @Override
    public void addListOfRecords(ObservableList<RequirementsRow> list) throws SQLException
    {
        for(RequirementsRow row : list)
        {
            /* Move the current row to the archive table. */
            this.moveToArchive(row);

            /* Deletes from the current database table. */
            this.deleteFromDatabase(row.getId(), "RequirementsData");
        }
    }

    /**
     * Removes a list of records from the archive and places them back into the
     * RequirementsData table that contains all non-archived RequirementRows.
     *
     * @param list The list to bring back from the archive.
     * @throws SQLException If the SQL query cannot complete properly.
     */
    @Override
    public void removeListOfRecords(ObservableList<RequirementsRow> list) throws SQLException
    {
        for(RequirementsRow row : list)
        {
            /* Move from the RequirementsData_Archive table. */
            this.moveFromArchive(row);

            /* Delete it from the RequirementsData_Archive table. */
            this.deleteFromDatabase(row.getId(), "RequirementsData_Archive");
        }
    }

    /**
     * Fill the archive table view in the GUI with the current archived
     * data found within the database table RequirementsData_Archive.
     *
     * @throws SQLException If the SQL query could not complete properly.
     */
    @Override
    public void fillRows() throws SQLException
    {
        // Create query to grab all rows.
        String query = "SELECT * FROM RequirementsData_Archive";

        // Create the statement to send.
        Statement st = Main.conn.createStatement();

        // Return the result set from this query.
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) // Retrieve data from ResultSet
        {
            RequirementsRow temp = new RequirementsRow(
                    rs.getString("csc"),
                    rs.getString("csu"),
                    rs.getString("doors_id"),
                    rs.getString("paragraph"),
                    rs.getString("baseline"),
                    rs.getString("scicr"),
                    rs.getString("capability"),
                    rs.getDouble("add"),
                    rs.getDouble("change"),
                    rs.getDouble("delete"),
                    rs.getDouble("design"),
                    rs.getDouble("code"),
                    rs.getDouble("unitTest"),
                    rs.getDouble("integration"),
                    rs.getString("ri"),
                    rs.getString("rommer"),
                    rs.getString("program"),
                    rs.getString("build")
                );

            /* Retrieve the timestamp. */
            String timestamp = rs.getDate("date_archived").toString();

            /* Set a new timestamp. */
            temp.setTimestamp(timestamp);
            temp.setId(rs.getInt("Req_ID"));

            //tempRows.add(temp);
            this.rows.add(temp);
        }
    }

    /**
     * Method that will move a single row to the archive table in the database
     * called RequirementsData_Archive.
     *
     * @param row The RequirementsRow that will have its data written to the database.
     * @throws SQLException If the SQL query could not complete properly.
     */
    private void moveToArchive(RequirementsRow row) throws SQLException
    {
        /* Let's get todays date. */
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        row.setTimestamp(date.toString());

        // The query to insert the data from the fields.
        String insertQuery =    "INSERT INTO RequirementsData_Archive ([Req_ID], [date_archived], [csc], [csu], [doors_id], [paragraph], " +
                                                                        "[baseline], [scicr], [capability], [add], " +
                                                                        "[change], [delete], [design], [code], [unitTest], " +
                                                                        "[integration], [ri], [rommer], [program], [build])" +
                                                                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        st.setInt(1, row.getId());
        st.setDate(2, date);
        st.setString(3, row.getCsc());
        st.setString(4, row.getCsu());
        st.setString(5, row.getDoorsID());
        st.setString(6, row.getParagraph());
        st.setString(7, row.getBaseline());
        st.setString(8, row.getScicr());
        st.setString(9, row.getCapability());
        st.setDouble(10, row.getAdd());
        st.setDouble(11, row.getChange());
        st.setDouble(12, row.getDelete());
        st.setDouble(13, row.getDesignWeight());
        st.setDouble(14, row.getCodeWeight());
        st.setDouble(15, row.getUnitTestWeight());
        st.setDouble(16, row.getIntegrationWeight());
        st.setString(17, row.getRi());
        st.setString(18, row.getRommer());
        st.setString(19, row.getProgram());
        st.setString(20, row.getBuild());


        // Perform the update inside of the table of the database.
        st.executeUpdate();

        /* Add this element to the list of archied rows in memory. */
        this.rows.add(row);
    }

    /**
     * Moves a single RequirementsRow object from the database table called
     * RequirementsRow_Data and places it back into memory.
     *
     * @param row The RequirementsRow to place back into memory.
     * @throws SQLException If the SQL query could not complete properly.
     */
    private void moveFromArchive(RequirementsRow row) throws SQLException
    {
        // The query to insert the data from the fields.
        String insertQuery =    "INSERT INTO RequirementsData ([csc], [csu], [doors_id], [paragraph], " +
                "[baseline], [scicr], [capability], [add], " +
                "[change], [delete], [design], [code], [unitTest], " +
                "[integration], [ri], [rommer], [program], [build])" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        st.setString(1, row.getCsc());
        st.setString(2, row.getCsu());
        st.setString(3, row.getDoorsID());
        st.setString(4, row.getParagraph());
        st.setString(5, row.getBaseline());
        st.setString(6, row.getScicr());
        st.setString(7, row.getCapability());
        st.setDouble(8, row.getAdd());
        st.setDouble(9, row.getChange());
        st.setDouble(10, row.getDelete());
        st.setDouble(11, row.getDesignWeight());
        st.setDouble(12, row.getCodeWeight());
        st.setDouble(13, row.getUnitTestWeight());
        st.setDouble(14, row.getIntegrationWeight());
        st.setString(15, row.getRi());
        st.setString(16, row.getRommer());
        st.setString(17, row.getProgram());
        st.setString(18, row.getBuild());

        // Perform the update inside of the table of the database.
        st.executeUpdate();

        this.rows.remove(row);
    }

    /**
     * Deletes a row from the database when the user decides to add or remove
     * a row from and to memory.
     *
     * @param ID The ID to search for in the database table.
     * @param table The table to search in.
     * @throws SQLException If the SQL query could not be complete properly.
     */
    private void deleteFromDatabase(int ID, String table) throws SQLException {
        // Set up statement for deleting from database.
        PreparedStatement st = Main.conn.prepareStatement("DELETE FROM " + table + " WHERE [Req_ID] = ?");

        // Uses the primary key to locate in table.
        st.setInt(1, ID);

        // Perform the query.
        st.executeUpdate();
    }

    /**
     * Getter for the list of rows currently in memory in the archive.
     * @return The ObservableList of rows.
     */
    public ObservableList<RequirementsRow> getRows() {
        return rows;
    }
}
