package ROMdb.Archive;

import ROMdb.Driver.Main;
import ROMdb.Helpers.SCICRRow;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Created by Anthony Orio on 4/6/2017.
 */
public class SCICRArchive extends  Archive<SCICRRow>
{

    @Override
    public void addRecord(SCICRRow row) throws SQLException
    {
        this.moveToArchive(row);
        this.deleteFromDatabase(row.getNumber());
    }

    @Override
    public void addListOfRecords(ObservableList<SCICRRow> list) throws SQLException {
        for(SCICRRow row : list)
        {
            this.moveToArchive(row);
            this.deleteFromDatabase(row.getNumber());
        }
    }

    private void moveToArchive(SCICRRow row) throws SQLException
    {
        /* Let's get todays date. */
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        System.out.println(date);

        String query = "INSERT INTO SCICRData_Archive ([scicrData_id], [date_archived], [type], [number], [title], [build], [baseline]) " +
                "       VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(query);

        /* Parse all of the information and stage for writing. */
        st.setInt(1, row.getId());
        st.setDate(2, date);
        st.setString(3, row.getType());
        st.setString(4, row.getNumber());
        st.setString(5, row.getTitle());
        st.setString(6, row.getBuild());
        st.setString(7, row.getBaseline());

        st.executeUpdate();
    }

    private void deleteFromDatabase(String number) throws SQLException {
        // Set up statement for deleting from database.
        PreparedStatement st = Main.conn.prepareStatement("DELETE FROM SCICRData WHERE [Number] = ?");

        // Uses the primary key to locate in table.
        st.setString(1, number);

        // Perform the query.
        st.executeUpdate();
    }
}
