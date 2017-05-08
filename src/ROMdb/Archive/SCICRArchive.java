package ROMdb.Archive;

import ROMdb.Driver.Main;
import ROMdb.Helpers.SCICRRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import net.ucanaccess.jdbc.UcanaccessSQLException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    public SCICRArchive()
    {
        try
        {
            this.fillRows();
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fetch rows.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * This method is deprecated.
     * @param row The object to store into the records list.
     * @throws SQLException If the SQL query cannot complete properly.
     */
    @Override
    public void addRecord(SCICRRow row) throws SQLException
    {
        this.moveToArchive(row);
        this.deleteFromDatabase(Integer.toString(row.getId()), "SCICR");
    }

    /**
     * Adds a list of selected records to the database. A list of rows
     * can have 1 to n many rows to insert into the database.
     *
     * @param list The list of objects to append to the current list.
     * @throws SQLException If the SQL query cannot complete properly.
     */
    @Override
    public void addListOfRecords(ObservableList<SCICRRow> list) throws SQLException {
        for(SCICRRow row : list)
        {
            this.moveToArchive(row);
            this.deleteFromDatabase(Integer.toString(row.getId()), "SCICR");
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
        for(SCICRRow row : list)
        {
            try
            {
                this.moveFromArchive(row);
                this.deleteFromDatabase(Integer.toString(row.getId()), "SCICR_Archive");
            }
            catch(UcanaccessSQLException ucae)
            {
                if(ucae.getCause() instanceof SQLIntegrityConstraintViolationException)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't add SCICR to database." +
                            "\nSCICR already exists.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        }
    }


    /**
     * Fill the archive table view in the GUI with the current archived
     * @throws SQLException If the SQL query could not complete properly.
     */
    @Override
    public void fillRows() throws SQLException {
        // Initialize rows list.
        //ObservableList<SCICRRow> tempRows = FXCollections.observableArrayList();

        // Create query to grab all rows.
        String query = "SELECT [scicr_id], [date_archived], [type], [number], [title], [field_value], [baseline_desc] " +
                "FROM SCICR_Archive INNER JOIN ValCodes ON SCICR_Archive.build_val_code_id = ValCodes.val_id INNER JOIN Baseline ON " +
                "SCICR_Archive.baseline_id = Baseline.baseline_id";

        // Create the statement to send.
        Statement st = Main.newconn.createStatement();

        // Return the result set from this query.
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) // Retrieve data from ResultSet
        {
            SCICRRow temp = new SCICRRow(
                    rs.getString("type"),
                    rs.getString("number"),
                    rs.getString("title"),
                    rs.getString("field_value"),
                    rs.getString("baseline_desc")
            );
            String timestamp = rs.getDate("date_archived").toString();
            temp.setTimestamp(timestamp);
            temp.setID(rs.getInt("scicr_id"));

            //tempRows.add(temp);
            this.rows.add(temp);
        }
    }

    /**
     * Moves a single SCICRRow object from the database table called
     * SCICRData_Archive and places it back into memory.
     *
     * @param row The SCICRRow to place back into memory.
     * @throws SQLException If the SQL query could not complete properly.
     */
    private void moveFromArchive(SCICRRow row) throws SQLException
    {
        String dataQuery = "SELECT * FROM SCICR_Archive WHERE [scicr_id]=?";
        PreparedStatement dataQueryPS = Main.newconn.prepareStatement(dataQuery);
        dataQueryPS.setString(1, Integer.toString(row.getId()));
        ResultSet dataQueryRS = dataQueryPS.executeQuery();

        // The query to insert the data from the fields.
        String query = "INSERT INTO SCICR ([scicr_id], [number], [type], [title], [build_val_code_id], " +
                "[baseline_id]) VALUES (?, ?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.newconn.prepareStatement(query);

        while(dataQueryRS.next())
        {
            st.setString(1, dataQueryRS.getString("scicr_id"));
            st.setString(2, dataQueryRS.getString("number"));
            st.setString(3, dataQueryRS.getString("type"));
            st.setString(4, dataQueryRS.getString("title"));
            st.setString(5, dataQueryRS.getString("build_val_code_id"));
            st.setString(6, dataQueryRS.getString("baseline_id"));
        }

        // Perform the update inside of the table of the database.
        st.executeUpdate();

        this.rows.remove(row);
    }


    /**
     * Method that will move a single row to the archive table in the database
     * called SCICRData_Archive.
     *
     * @param row The SCICRRow that will have its data written to the database.
     * @throws SQLException If the SQL query could not complete properly.
     */
    private void moveToArchive(SCICRRow row) throws SQLException
    {
        /* Let's get todays date. */
        //long millis = System.currentTimeMillis();
        //java.sql.Date date = new java.sql.Date(millis);
        //row.setTimestamp(date.toString());
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        row.setTimestamp(strDate);

        String scicrId = Integer.toString(row.getId());
        String getDBSCICRDataQuery = "SELECT * FROM SCICR WHERE [scicr_id]=?";
        PreparedStatement st = Main.newconn.prepareStatement(getDBSCICRDataQuery);
        st.setString(1, scicrId);
        ResultSet rs = st.executeQuery();

        String archiveInsertQuery = "INSERT INTO SCICR_Archive ([scicr_id], [date_archived], [number], [type], " +
                "[title], [build_val_code_id], [baseline_id]) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement newst = Main.newconn.prepareStatement(archiveInsertQuery);
        while(rs.next())
        {
            newst.setString(1, rs.getString("scicr_id"));
            newst.setString(2, row.getTimestamp());
            newst.setString(3, rs.getString("number"));
            newst.setString(4, rs.getString("type"));
            newst.setString(5, rs.getString("title"));
            newst.setString(6, rs.getString("build_val_code_id"));
            newst.setString(7, rs.getString("baseline_id"));
        }
        newst.executeUpdate();

        this.rows.add(row);
    }

    /**
     * Deletes a row from the database when the user decides to add or remove
     * a row from and to memory.
     *
     * @param scicrId The id to search for.
     * @param table The table to search in.
     * @throws SQLException If the SQL query could not be complete properly.
     */
    private void deleteFromDatabase(String scicrId, String table) throws SQLException {


        // Set up statement for deleting from database.
        PreparedStatement st = Main.newconn.prepareStatement("DELETE FROM " + table + " WHERE [scicr_id] = ?");

        // Uses the primary key to locate in table.
        st.setString(1, scicrId);

        // Perform the query.
        st.executeUpdate();
    }

    /**
     * Getter for the list of rows currently in memory in the archive.
     * @return The ObservableList of rows.
     */
    public ObservableList<SCICRRow> getRows()
    {
        return this.rows;
    }
}
