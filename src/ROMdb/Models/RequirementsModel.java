package ROMdb.Models;

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
 */
public class RequirementsModel
{
    //public static HashMap<String, ObservableList<RequirementsRow>> map = new HashMap<>();
    public static final String SKELETON_KEY = "Skeleton Key";

    public static ObservableList<RequirementsRow> allReqData;
    public static ObservableList<RequirementsRow> currentFilteredList = FXCollections.observableArrayList();

    public static ArrayList<FilterItem> filters = null;

    /**
     * Fills the table with the data from the database.
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
                    rs.getString("program")
            );
            tempRow.setId(rs.getInt("Req_ID"));
            rows.add(tempRow);
        }
        //RequirementsModel.map.put(SKELETON_KEY, rows);
        RequirementsModel.allReqData = rows;
    }

    public static ObservableList getReqDataWithFilter() throws SQLException
    {
        ObservableList<RequirementsRow> filteredList = FXCollections.observableArrayList();
        //filteredList.removeAll();

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
                    rs.getString("program")
            );
            tempRow.setId(rs.getInt("Req_ID"));
            filteredList.add(tempRow);
        }
        currentFilteredList = filteredList;
        return filteredList;
    }

    public static void updateTextColumnInDB(String tableName, String columnName, String textToWrite) throws SQLException
    {

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
     * @param rowToUpdate the row to update.
     */
    public static void updateRowInDB(RequirementsRow rowToUpdate) throws SQLException
    {
        // The query to insert the data from the fields.
        String insertQuery = "UPDATE RequirementsData SET" +
                "[csc]=?, [csu]=?, [doors_id]=?, [paragraph]=?," +
                "[baseline]=?, [scicr]=?, [capability]=?, [add]=?," +
                "[change]=?, [delete]=?, [design]=?, [code]=?," +
                "[unitTest]=?, [integration]=?, [ri]=?, [rommer]=?," +
                "[program]=? " +
                "WHERE [Req_ID]=?";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        st.setInt(18, rowToUpdate.getId());

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

        // Execute sql statement to update database
        st.executeUpdate();
    }

    public static ArrayList<MapList<String>> getFilterListsData() throws SQLException
    {
        ArrayList<MapList<String>> returnList = new ArrayList<MapList<String>>();

        // construct scicr al
        String scicr_ColumnLabel = "Number";
        ArrayList<String> scicr_ArrayList = new ArrayList<String>();
        PreparedStatement scicr_Statement = QueryBuilder.buildSelectOrderByQuery("SCICRData", scicr_ColumnLabel, scicr_ColumnLabel, "asc");
        ResultSet scicr_ResultSet = scicr_Statement.executeQuery();
        while(scicr_ResultSet.next())
        {
            scicr_ArrayList.add(scicr_ResultSet.getString(scicr_ColumnLabel));
        }
        MapList<String> scicr_MapList = new MapList<String>("scicr", scicr_ArrayList);
        returnList.add(scicr_MapList);

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

        return returnList;
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
     * @param fieldName
     * @return
     * @throws SQLException
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

}
