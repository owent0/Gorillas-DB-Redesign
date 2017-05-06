package ROMdb.Models;

import ROMdb.Driver.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anthony Orio on 4/25/2017.
 */
public class AddItemsModel
{
    private static final String[] valTypes = {"Capability", "CSC", "CSU", "Program",
                                              "RI", "Rommer", "Build"};

    private static HashMap<String, ArrayList<String>> map = new HashMap();

    public static void fillHashMap() throws SQLException
    {
        /* Create query */
        String query = "SELECT * FROM Val_Codes";

        /* Create the statement to send. */
        Statement st = Main.conn.createStatement();

        /* Traverse valTypes array. */
        for (int i = 0; i < valTypes.length; i++)
        {
            /* Return the result set from this query. */
            ResultSet rs = st.executeQuery(query);

            /* The current value to pull data for. */
            String currVal = valTypes[i].toLowerCase();
            map.put(currVal, new ArrayList<String>());

            /* Cycle through result set. */
            while (rs.next())
            {
                /* If the current result is the same value type. */
                String currFieldName = rs.getString("Field_Name");
                if (currFieldName.equals(currVal))
                {
                    map.get(currVal).add(rs.getString("Field_Value"));
                }
            }
        }
    }




    public static String[] getValTypes()
    {
        return valTypes;
    }

    public static HashMap<String, ArrayList<String>> getMap()
    {
        return map;
    }

    public static void writeItemToDb(String type, String value) throws SQLException {

        /* Create query */
        String query = "INSERT INTO Val_Codes ([Field_Name], [Field_Value], [Order_Id]) VALUES (?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(query);

        /** Parse all of the information and stage for writing. */
        st.setString(1, type);
        st.setString(2, value);
        st.setString(3, Integer.toString(0));
        // Perform the update inside of the table of the database.
        st.executeUpdate();
    }

}
