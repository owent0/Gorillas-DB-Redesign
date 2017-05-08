package ROMdb.Models;

import ROMdb.Archive.RequirementsArchive;
import ROMdb.Driver.Main;
import ROMdb.Helpers.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anthony Orio on 3/28/2017.
 *
 * This is the model class that the RequirementsController
 * will be altering. All information is stored in memory here.
 */
public class RequirementsModel
{
    public static RequirementsArchive archive = new RequirementsArchive();

    //public static final String SKELETON_KEY = "Skeleton Key";

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

    public static final String[] gridViewColumnHeadings = { "CSC", "CSU", "Door", "Par/Fig", "Build", "BL",
                                                                "SC/ICR", "Cap", "Add", "Chg", "Del",
                                                                "Design Wt", "Code Wt", "Unit Test Wt", "Integ Wt",
                                                                "Resp Indv", "Rom", "Prog"
                                                            };

    /**
     * Archives a list of RequirementRows into the RequirementsData_Archive table
     * in the database.
     * @param rows The rows to archive.
     * @throws SQLException If the SQL query could not complete properly.
     */
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
        String selectSet = "[requirement_id], [csc_val_code_id], [csu_val_code_id], [doors_id], [paragraph], " +
                "[capability_val_code_id], [num_lines_added], [num_lines_changed], [num_lines_deleted], " +
                "[design_percentage], [code_percentage], [unit_test_percentage], [integration_percentage], " +
                "[responsible_individual_val_code_id], [rommer_val_code_id], [program_val_code_id], [number], " +
                "[build_val_code_id], [baseline_desc]";
        String tableFrom = "Requirement INNER JOIN SCICR ON Requirement.scicr_id = SCICR.scicr_id INNER JOIN Baseline ON SCICR.baseline_id = Baseline.baseline_id";
        PreparedStatement st = QueryBuilder.buildSelectWhereQuery(tableFrom, selectSet, RequirementsModel.filters, true);

        ResultSet rs = st.executeQuery();

        HashMap<Integer, String> vclmap = MainMenuModel.getValCodesLookuMap();

        while (rs.next())
        {
            RequirementsRow tempRow = new RequirementsRow(
                    vclmap.get(rs.getInt("csc_val_code_id")),
                    vclmap.get(rs.getInt("csu_val_code_id")),
                    rs.getString("doors_id"),
                    rs.getString("paragraph"),
                    rs.getString("baseline_desc"),
                    rs.getString("number"),
                    vclmap.get(rs.getInt("capability_val_code_id")),
                    rs.getDouble("num_lines_added"),
                    rs.getDouble("num_lines_changed"),
                    rs.getDouble("num_lines_deleted"),
                    rs.getDouble("design_percentage"),
                    rs.getDouble("code_percentage"),
                    rs.getDouble("unit_test_percentage"),
                    rs.getDouble("integration_percentage"),
                    vclmap.get(rs.getInt("responsible_individual_val_code_id")),
                    vclmap.get(rs.getInt("rommer_val_code_id")),
                    vclmap.get(rs.getInt("program_val_code_id")),
                    vclmap.get(rs.getInt("build_val_code_id"))
            );
            tempRow.setId(rs.getInt("requirement_id"));
            tempRow.setCsc_val_code_id(rs.getString("csc_val_code_id"));
            tempRow.setCsu_val_code_id(rs.getString("csu_val_code_id"));
            tempRow.setCapability_val_code_id(rs.getString("capability_val_code_id"));
            tempRow.setResponsible_individual_val_code_id(rs.getString("responsible_individual_val_code_id"));
            tempRow.setRommer_val_code_id(rs.getString("rommer_val_code_id"));
            tempRow.setProgram_val_code_id(rs.getString("program_val_code_id"));
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
        String updateQuery = "UPDATE Requirement SET " +
                "[csc_val_code_id]=?, [csu_val_code_id]=?, [doors_id]=?, [paragraph]=?, " +
                "[capability_val_code_id]=?, [num_lines_added]=?, " +
                "[num_lines_changed]=?, [num_lines_deleted]=?, [design_percentage]=?, " +
                "[code_percentage]=?, [unit_test_percentage]=?, [integration_percentage]=?, " +
                "[responsible_individual_val_code_id]=?, [rommer_val_code_id]=?, [program_val_code_id]=? " +
                "WHERE [requirement_id]=?";

        // Create a new statement.
        PreparedStatement st = Main.newconn.prepareStatement(updateQuery);

        /** Parse all of the information and stage for writing. */
        st.setString(1, rowToUpdate.getCsc_val_code_id());
        st.setString(2, rowToUpdate.getCsu_val_code_id());
        st.setString(3, rowToUpdate.getDoorsID().trim());
        st.setString(4, rowToUpdate.getParagraph().trim());
        st.setString(5, rowToUpdate.getCapability_val_code_id());
        st.setDouble(6, rowToUpdate.getAdd());
        st.setDouble(7, rowToUpdate.getChange());
        st.setDouble(8, rowToUpdate.getDelete());
        st.setDouble(9, rowToUpdate.getDesignWeight());
        st.setDouble(10, rowToUpdate.getCodeWeight());
        st.setDouble(11, rowToUpdate.getUnitTestWeight());
        st.setDouble(12, rowToUpdate.getIntegrationWeight());
        st.setString(13, rowToUpdate.getResponsible_individual_val_code_id());
        st.setString(14, rowToUpdate.getRommer_val_code_id());
        st.setString(15, rowToUpdate.getProgram_val_code_id());

        st.setInt(16, rowToUpdate.getId());

        // Execute sql statement to update database
        st.executeUpdate();
    }

    /**
     * Gets the list of filters for each observable list.
     * @return An arraylist containing MapList Objects of type string.
     * @throws SQLException If the statement could not be complete by getMapListFromVal_Codes call.
     */
    public static ArrayList<MapList<ComboItem>> getFilterListsData() throws SQLException
    {
        ArrayList<MapList<ComboItem>> returnList = new ArrayList<MapList<ComboItem>>();

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
    public static void insertNewReqRow(RequirementsRow row) throws SQLException
    {
        // The query to insert the data from the fields.
        String insertQuery = "INSERT INTO Requirement ([csc], [csu], [doors_id], [paragraph], " +
                                                                "[baseline], [scicr], [capability], [add], " +
                                                                "[change], [delete], [design], [code], [unitTest], " +
                                                                "[integration], [ri], [rommer], [program], [build])" +
                                                                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.newconn.prepareStatement(insertQuery);

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
    private static MapList<ComboItem> getMapListFromVal_Codes(String fieldName) throws SQLException
    {
        ArrayList<ComboItem> fieldName_ArrayList = new ArrayList<ComboItem>();
        ArrayList<FilterItem> fieldName_FilterItemArrayList = new ArrayList<FilterItem>();
        fieldName_FilterItemArrayList.add(new FilterItem(fieldName, "field_name"));
        PreparedStatement fieldName_Statement = QueryBuilder.buildSelectWhereOrderByQuery("ValCodes", "val_id, field_value", fieldName_FilterItemArrayList, false, "Order_Id", "asc");
        ResultSet fieldName_ResultSet = fieldName_Statement.executeQuery();
        while(fieldName_ResultSet.next())
        {
            fieldName_ArrayList.add(new ComboItem(fieldName_ResultSet.getString("val_id"), fieldName_ResultSet.getString("field_value")));
        }
        MapList<ComboItem> fieldName_MapList = new MapList<ComboItem>(fieldName, fieldName_ArrayList);
        return fieldName_MapList;
    }
}
