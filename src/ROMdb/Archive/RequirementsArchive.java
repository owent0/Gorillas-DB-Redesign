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
 * Created by Anthony Orio on 4/10/2017.
 */
public class RequirementsArchive extends Archive<RequirementsRow> {

    private ObservableList<RequirementsRow> rows = FXCollections.observableArrayList();

    public RequirementsArchive() {
        try {
            this.fillRows();
        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not fetch rows.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @Override
    public void addRecord(RequirementsRow object) throws SQLException {

    }

    @Override
    public void addListOfRecords(ObservableList<RequirementsRow> list) throws SQLException {
        for(RequirementsRow row : list)
        {
            this.moveToArchive(row);
            this.deleteFromDatabase(row.getId(), "RequirementsData");
        }
    }

    @Override
    public void removeListOfRecords(ObservableList<RequirementsRow> list) throws SQLException {
        for(RequirementsRow row : list)
        {
            this.moveFromArchive(row);
            this.deleteFromDatabase(row.getId(), "RequirementsData_Archive");
        }
    }

    @Override
    public void fillRows() throws SQLException {
        // Initialize rows list.
        //ObservableList<SCICRRow> tempRows = FXCollections.observableArrayList();

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
                    rs.getString("program")
                );

            String timestamp = rs.getDate("date_archived").toString();
            temp.setTimestamp(timestamp);
            temp.setId(rs.getInt("Req_ID"));

            //tempRows.add(temp);
            this.rows.add(temp);
        }
    }

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
                                                                        "[integration], [ri], [rommer], [program])" +
                                                                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

        // Perform the update inside of the table of the database.
        st.executeUpdate();

        this.rows.add(row);
    }

    private void moveFromArchive(RequirementsRow row) throws SQLException
    {
        // The query to insert the data from the fields.
        String insertQuery =    "INSERT INTO RequirementsData ([csc], [csu], [doors_id], [paragraph], " +
                "[baseline], [scicr], [capability], [add], " +
                "[change], [delete], [design], [code], [unitTest], " +
                "[integration], [ri], [rommer], [program])" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

        // Perform the update inside of the table of the database.
        st.executeUpdate();

        this.rows.remove(row);
    }

    private void deleteFromDatabase(int ID, String table) throws SQLException {
        // Set up statement for deleting from database.
        PreparedStatement st = Main.conn.prepareStatement("DELETE FROM " + table + " WHERE [Req_ID] = ?");

        // Uses the primary key to locate in table.
        st.setInt(1, ID);

        // Perform the query.
        st.executeUpdate();
    }

    public ObservableList<RequirementsRow> getRows() {
        return rows;
    }
}
