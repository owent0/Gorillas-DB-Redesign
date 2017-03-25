package ROMdb.Models;

import ROMdb.Driver.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Tom on 3/24/2017.
 */
public class AddSCICRModel {
    public static void saveSCICR(String baseline, String type, String number, String title, String build) throws Exception {
        // The query to insert the data from the fields.
        String insertQuery =    "INSERT INTO SCICRData ([Number], [Type], [Title], [Build], [Baseline]) VALUES (?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        /** Parse all of the information and stage for writing. */
        st.setString(1, number);
        st.setString(2, type);
        st.setString(3, title);
        st.setString(4, build);
        st.setString(5, baseline);

        // Perform the update inside of the table of the database.
        st.executeUpdate();
    }

    public static boolean isNumberUnique(String number, String baseline) throws Exception{
        // The query to insert the data from the fields.
        boolean inDatabase = true;
        String insertQuery =    "SELECT COUNT(*) as NUM_MATCHES FROM SCICRData WHERE Number = ? AND Baseline = ?";

        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        st.setString(1, number);
        st.setString(2, baseline);

        int count = 0;
        ResultSet rs = null;
        try
        {
            rs = st.executeQuery();
            while(rs.next())
            {
                count = rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //...
        }


        if (count == 0) {
            inDatabase = false;
            System.out.println("Not in table for baseline");
        }
        return inDatabase;
    }
}
