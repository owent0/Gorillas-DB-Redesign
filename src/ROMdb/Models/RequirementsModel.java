package ROMdb.Models;

import ROMdb.Driver.Main;
import ROMdb.Helpers.RequirementsRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Anthony Orio on 3/28/2017.
 */
public class RequirementsModel
{
    public static HashMap<String, ObservableList<RequirementsRow>> map = new HashMap<>();
    public static final String SKELETON_KEY = "Skeleton Key";


    /**
     * Fills the table with the data from the database.
     */
    public static void fillTable() throws SQLException {


        // Initialize rows list.
        ObservableList rows = FXCollections.observableArrayList();

        // Create query to grab all rows.
        String query = "SELECT * FROM RequirementsData";

        // Create the statement to send.
        Statement st = Main.conn.createStatement();

        // Return the result set from this query.
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) { // Retrieve data from ResultSet

            RequirementsRow tempRow = new RequirementsRow(
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
            tempRow.setId(rs.getInt("Req_ID"));
            rows.add(tempRow);
        }
        RequirementsModel.map.put(SKELETON_KEY, rows);
    }

    public static void insertIntoTable(String csc,
                                       String csu,
                                       String doorsID,
                                       String paragraph,
                                       String baseline,
                                       String scicr,
                                       String capability,
                                       String add,
                                       String change,
                                       String delete,
                                       String designWeight,
                                       String codeWeight,
                                       String unitTestWeight,
                                       String integrationWeight,
                                       String ri,
                                       String rommer,
                                       String program,
                                       String build) throws SQLException
    {
        String insertQuery = "INSERT INTO SCICRData ([csc], [csu], [doors_id], [paragraph], [baseline], " +
                                                    "[scicr], [capability], [add], [change], [delete]" +
                                                    "[design], [code], [unitTest], [integration], " +
                                                    "[ri], [rommer], [program], [build]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

         /** Parse all of the information and stage for writing. */
        st.setString(1, csc.trim());
        st.setString(2, csu.trim());
        st.setString(3, doorsID.trim());
        st.setString(4, paragraph.trim());
        st.setString(5, baseline.trim());
        st.setString(6, scicr.trim());
        st.setString(7, capability.trim());
        st.setDouble(8, Double.parseDouble(add));
        st.setDouble(9, Double.parseDouble(change));
        st.setDouble(10, Double.parseDouble(delete));
        st.setDouble(11, Double.parseDouble(designWeight));
        st.setDouble(12, Double.parseDouble(codeWeight));
        st.setDouble(13, Double.parseDouble(unitTestWeight));
        st.setDouble(14, Double.parseDouble(integrationWeight));
        st.setString(15, ri.trim());
        st.setString(16, rommer.trim());
        st.setString(17, program.trim());
        st.setString(18, build.trim());

        st.executeUpdate();
    }

    /**
     * Updates the database with any of the changes made.
     * @param rowToUpdate the row to update.
     */
    public static void updateChangesToDB(RequirementsRow rowToUpdate) throws SQLException
    {
        // The query to insert the data from the fields.
        String insertQuery = "UPDATE RequirementsData SET [csc]=?, [csu]=?, [doors_id]=?, [paragraph]=?, [baseline]=?, [scicr]=?, [capability]=?, [add]=?, [change]=?, [delete]=?, [design]=?, [code]=?, [unitTest]=?, [integration]=?, [ri]=?, [rommer]=?, [program]=?, [build]=? WHERE [Req_ID]=?";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        st.setInt(19, rowToUpdate.getId());

        /** Parse all of the information and stage for writing. */
        st.setString(1, rowToUpdate.getCsc().trim());
        st.setString(2, rowToUpdate.getCsu().trim());
        st.setString(3, rowToUpdate.getDoorsID().trim());
        st.setString(4, rowToUpdate.getParagraph().trim());
        st.setString(5, rowToUpdate.getBaseline().trim());
        st.setString(6, rowToUpdate.getScicr().trim());
        st.setString(7, rowToUpdate.getCapability().trim());
        st.setDouble(8, rowToUpdate.getAdd());
        st.setDouble(9, rowToUpdate.getChange());
        st.setDouble(10, rowToUpdate.getDelete());
        st.setDouble(11, rowToUpdate.getDesignWeight());
        st.setDouble(12, rowToUpdate.getCodeWeight());
        st.setDouble(13, rowToUpdate.getUnitTestWeight());
        st.setDouble(14, rowToUpdate.getIntegrationWeight());
        st.setString(15, rowToUpdate.getRi().trim());
        st.setString(16, rowToUpdate.getRommer().trim());
        st.setString(17, rowToUpdate.getProgram().trim());
        st.setString(18, rowToUpdate.getBuild().trim());

        // Perform the update inside of the table of the database.
        st.executeUpdate();
    }
}
