package ROMdb.Models;

import ROMdb.Archive.RequirementsArchive;
import ROMdb.Driver.Main;
import ROMdb.Helpers.FilterItem;
import ROMdb.Helpers.MapList;
import ROMdb.Helpers.RequirementsRow;
import ROMdb.Helpers.QueryBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Anthony Orio on 3/28/2017.
 *
 * This is the model class that the RequirementsController
 * will be altering. All information is stored in memory here.
 */
public class RequirementsModel
{
    public static RequirementsArchive archive = new RequirementsArchive();

    public static final String SKELETON_KEY = "Skeleton Key";

    // This list contains all the requirement rows objects in the table.
    public static ObservableList<RequirementsRow> allReqData;

    // This list contains the requirement rows that are currently filtered.
    // Will constantly be cleared and altered as filters appear and disappear.
    public static ObservableList<RequirementsRow> currentFilteredList = FXCollections.observableArrayList();

    // The array list containing the filters.
    public static ArrayList<FilterItem> filters = null;

    public static final String[] groupChoices = {    "SC/ICR", "CSC", "CSU", "Capability", "Build",
                                                        "Responsible Individual", "Baseline", "Paragraph/Figure",
                                                        "Program"
                                                    };

    public static final String[] gridViewColumnHeadings = { "CSC", "CSU", "Door", "Para/Fig", "BL",
                                                                "SC/ICR", "Cap", "Add", "Chg", "Del",
                                                                "Design Wt", "Code Wt", "Unit Test Wt", "Integ Wt",
                                                                "Resp Indv", "Rom", "Prog"
                                                            };

    public static void archiveRows(ObservableList<RequirementsRow> rows) throws SQLException
    {
        archive.addListOfRecords(rows);

        ArrayList<RequirementsRow> list = new ArrayList<>(rows);

        int size = list.size();
        for(int i = 0; i < size; i++)
        {
            RequirementsRow temp = list.get(i);
            currentFilteredList.remove(temp);
        }
    }


    /**
     * Fills the table with the data from the database.
     * @throws SQLException If the query could not successfully complete.
     */
    public static void refreshAllReqDataFromDB() throws SQLException
    {
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
        RequirementsModel.allReqData = rows;
    }

    /**
     * This method will return an observable list contain all of the
     * requirement rows that have been filtered while the user was
     * searching.
     * @return The observable list containing the filtered rows.
     * @throws SQLException If the SQL query could not successfully complete.
     */
    public static ObservableList getReqDataWithFilter() throws SQLException
    {
        ObservableList<RequirementsRow> filteredList = FXCollections.observableArrayList();

        // Method call to QueryBuilder to build the query.
        PreparedStatement st = QueryBuilder.buildSelectWhereQuery("RequirementsData", "*", RequirementsModel.filters, true);

        ResultSet rs = st.executeQuery();

        while (rs.next())
        {
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
            filteredList.add(tempRow);
        }
        currentFilteredList = filteredList;
        return filteredList;
    }

    /**
     * Updates an entire column columnName within the database table tableName
     * with the value textToWrite.
     * @param tableName The table to make the update to.
     * @param columnName The column to update in the table.
     * @param textToWrite The text to write into the column.
     * @throws Exception If the input is invalid or SQL statement could not complete.
     */
    public static void updateTextColumnInDB(String tableName, String columnName, String textToWrite) throws Exception
    {

        // Check to make sure the input is not null or empty.
        if(textToWrite == null || textToWrite.trim().equals(""))
        {
            throw new Exception("Invalid input.");
        }

        ObservableList<RequirementsRow> list;
        if(!currentFilteredList.isEmpty()) {
            list = currentFilteredList;
        } else {
            list = allReqData;
        }

        for(RequirementsRow row : list)
        {
            PreparedStatement st = QueryBuilder.updateColumnText(tableName, columnName, textToWrite, row.getId());
            st.executeUpdate();
            int i = list.indexOf(row);

            // We need to see which column we actually
            // are updating so that we can make the update
            // to the object in memory.
            switch(columnName)
            {
                case "ri":
                    row.setRi(textToWrite);
                    list.set(i, row);
                    break;
                case "program":
                    row.setProgram(textToWrite);
                    list.set(i, row);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Updates the column columnName in the table tableName with a value that
     * is a double.
     * @param tableName The table name in the database.
     * @param columnName The column name in the table.
     * @param value The value to insert into the column.
     * @throws Exception If value is not between 0 and 100 inclusively or SQL statement cannot complete.
     */
    public static void updateDoubleColumnInDB(String tableName, String columnName, double value) throws Exception
    {
        if(value > 100 || value < 0) {
            throw new Exception("Number not within range 0-100");
        }

        ObservableList<RequirementsRow> list;
        if(!currentFilteredList.isEmpty()) {
            list = currentFilteredList;
        } else {
            list = allReqData;
        }

        for(RequirementsRow row : list)
        {
            PreparedStatement st = QueryBuilder.updateColumnDouble(tableName, columnName, value, row.getId());
            st.executeUpdate();
            int i = list.indexOf(row);

            // We need to see which column we actually
            // are updating so that we can make the update
            // to the object in memory.
            switch(columnName)
            {
                case "design" :
                    row.setDesignWeight(value);
                    list.set(i, row);
                    break;
                case "code":
                    row.setCodeWeight(value);
                    list.set(i, row);
                    break;
                case "unitTest":
                    row.setUnitTestWeight(value);
                    list.set(i, row);
                    break;
                case "integration":
                    row.setIntegrationWeight(value);
                    list.set(i, row);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Updates the database with the new values for the given row.
     * @param rowToUpdate The row to update.
     * @throws SQLException If the statement could not complete.
     */
    public static void updateRowInDB(RequirementsRow rowToUpdate) throws SQLException
    {
        // The query to insert the data from the fields.
        String insertQuery = "UPDATE RequirementsData SET" +
                "[csc]=?, [csu]=?, [doors_id]=?, [paragraph]=?," +
                "[baseline]=?, [scicr]=?, [capability]=?, [add]=?," +
                "[change]=?, [delete]=?, [design]=?, [code]=?," +
                "[unitTest]=?, [integration]=?, [ri]=?, [rommer]=?," +
                "[program]=?, [build]=? " +
                "WHERE [Req_ID]=?";

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

        // Execute sql statement to update database
        st.executeUpdate();
    }

    /**
     * Gets the list of filters for each observable list.
     * @return An arraylist containing MapList Objects of type string.
     * @throws SQLException If the statement could not be complete by getMapListFromVal_Codes call.
     */
    public static ArrayList<MapList<String>> getFilterListsData() throws SQLException
    {
        ArrayList<MapList<String>> returnList = new ArrayList<MapList<String>>();

        // construct capability al
        returnList.add(RequirementsModel.getMapListFromVal_Codes("capability"));
        // construct csc al
        returnList.add(RequirementsModel.getMapListFromVal_Codes("csc"));
        // construct csu al
        returnList.add(RequirementsModel.getMapListFromVal_Codes("csu"));
        // construct program al
        returnList.add(RequirementsModel.getMapListFromVal_Codes("program"));
        // construct ri al
        returnList.add(RequirementsModel.getMapListFromVal_Codes("ri"));
        // construct rommer al
        returnList.add(RequirementsModel.getMapListFromVal_Codes("rommer"));
        // construct build al
        returnList.add(RequirementsModel.getMapListFromVal_Codes("build"));

        return returnList;
    }

    /**
     * Insert a new RequirementRow object into the table when the user
     * creates a new one.
     * @param row The row that will have its data written into the table.
     * @throws SQLException If the query could not complete successfully.
     */
    public static void insertNewReqRow(RequirementsRow row) throws SQLException {
        // The query to insert the data from the fields.
        String insertQuery =    "INSERT INTO RequirementsData ([csc], [csu], [doors_id], [paragraph], " +
                                                                "[baseline], [scicr], [capability], [add], " +
                                                                "[change], [delete], [design], [code], [unitTest], " +
                                                                "[integration], [ri], [rommer], [program], [build])" +
                                                                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        st.setString(1, row.getCsc().trim());
        st.setString(2, row.getCsu().trim());
        st.setString(3, row.getDoorsID().trim());
        st.setString(4, row.getParagraph().trim());
        st.setString(5, row.getBaseline().trim());
        st.setString(6, row.getScicr().trim());
        st.setString(7, row.getCapability().trim());
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
    }

    /**
     * Method that extracts the val_codes into a MapList of a particular Field_Name ordered by the table's Order_Id
     *      and returns the MapList
     *      ex: fieldName = days_of_week
     *          String for MapList name: days_of_week
     *          values in MapList list:
     *              Sunday
     *              Monday
     *              Tuesday
     *              Wednesday
     *              Thursday
     *              Friday
     *              Saturday
     *
     * @param fieldName The name of the field you are searching for.
     * @return The MapList of strings.
     * @throws SQLException If the query could not complete successfully.
     */
    private static MapList<String> getMapListFromVal_Codes(String fieldName) throws SQLException
    {
        String columnLabel = "Field_Value";
        ArrayList<String> fieldName_ArrayList = new ArrayList<String>();
        ArrayList<FilterItem> fieldName_FilterItemArrayList = new ArrayList<FilterItem>();
        fieldName_FilterItemArrayList.add(new FilterItem(fieldName, "Field_Name"));
        PreparedStatement fieldName_Statement = QueryBuilder.buildSelectWhereOrderByQuery("Val_Codes", "Field_Value", fieldName_FilterItemArrayList, false, "Order_Id", "asc");
        ResultSet fieldName_ResultSet = fieldName_Statement.executeQuery();
        while(fieldName_ResultSet.next())
        {
            fieldName_ArrayList.add(fieldName_ResultSet.getString(columnLabel));
        }
        MapList<String> fieldName_MapList = new MapList<String>(fieldName, fieldName_ArrayList);
        return fieldName_MapList;
    }

    /**
     *
     * Fills the table with the data from the database.
     * @throws SQLException If the query could not successfully complete.
     */
    public static ResultSet getReqDataForDDRpdf() throws SQLException
    {
/*        // Initialize rows list.
        ObservableList rows = FXCollections.observableArrayList();*/

        // Create query to grab all rows.
        String query = "SELECT * FROM RequirementsData";

        // Create the statement to send.
        Statement st = Main.conn.createStatement();

        // Return the result set from this query.
        ResultSet rs = st.executeQuery(query);
        return rs;
       /* while (rs.next()) { // Retrieve data from ResultSet

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
                    rs.getString("program")
            );
            tempRow.setId(rs.getInt("Req_ID"));
            rows.add(tempRow);
        }
        RequirementsModel.allReqData = rows;*/
    } // end getReqDataForDDRpdf()

}
