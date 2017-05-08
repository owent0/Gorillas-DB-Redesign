package ROMdb.Models;

import ROMdb.Controllers.ChangeAdminPasswordController;
import ROMdb.Controllers.EstimationBaseController;
import ROMdb.Controllers.LoginController;
import ROMdb.Controllers.SCICRController;
import ROMdb.Driver.Main;
import ROMdb.Helpers.ComboItem;
import ROMdb.Helpers.QueryBuilder;
import ROMdb.Helpers.FilterItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by derek on 3/25/2017.
 */
public class MainMenuModel
{
    public static EstimationBaseController estimationBaseController;
    public static SCICRController sCICRController;
    public static LoginController loginController;
    public static ChangeAdminPasswordController changeAdminPasswordController;

    public static ObservableList<String> baselines = fetchBaselinesFromDB();
    public static ObservableStringValue selectedBaseline = new SimpleStringProperty(baselines.get(0));
    public static ObservableList<String> scicrs = fetchSCICRsFromDB();

    private static HashMap<String, Integer> baselineLookupMap;
    private static HashMap<Integer, String> valCodesLookupMap = new HashMap<Integer, String>();

    /**
     * Pulls latest baseline data from the database before returning a reference to the baselineLookupMap
     * This map is used throughout the program when it is neccessary to determine the baseline_id of a particular
     * baseline when you only have the baseline_desc on hand.
     * returns null if there is a problem
     * @return
     */
    public static HashMap<String, Integer> getBaselineLookupMap()
    {
        String baselineDataQuery = "SELECT [baseline_desc], [baseline_id] FROM Baseline";
        try
        {
            Statement st = Main.newconn.createStatement();
            ResultSet rs = st.executeQuery(baselineDataQuery);
            MainMenuModel.baselineLookupMap = new HashMap<String, Integer>();
            while (rs.next()) // Retrieve data from ResultSet
            {
                MainMenuModel.baselineLookupMap.put(rs.getString("baseline_desc"),
                        Integer.parseInt(rs.getString("baseline_id")));
            }
            return MainMenuModel.baselineLookupMap;
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Could not retrieve baselines from database... Please contact program admin",
                    ButtonType.OK);
            alert.showAndWait();
        }
        return null;
    }

    /**
     * Get the observable list that contains all the baselines for the program
     * @return the observable list that contains all the baselines
     */
    public static ObservableList<String> getBaselines() {
        return baselines;
    }

    /**
     * Set the observable list that will contain all the baselines for the program
     * @param baselines the new observable list containing all the baselines
     */
    public static void setBaselines(ObservableList<String> baselines) {
        MainMenuModel.baselines = baselines;
    }

    /**
     * Get the currently selected baseline from the combobox
     * @return the currently selected baseline
     */
    public static String getSelectedBaseline() {
        return selectedBaseline.getValue();
    }

    /**
     * Set the selected baseline for the combobox
     * @param selectedBaseline the new selected baseline
     */
    public static void setSelectedBaseline(String selectedBaseline) {
        MainMenuModel.selectedBaseline = new SimpleStringProperty(selectedBaseline);
    }

    /**
     * This method will read all of the baselines currently stored within
     * the baseline database table.
     *
     * @return ObservableList the list containing the baseline from the baselines table.
     */
    private static ObservableList<String> fetchBaselinesFromDB()
    {
        // The list to store the baselines in temporarily.
        ArrayList<String> baselines = new ArrayList<String>();

        try
        {
            // Grab all the baselines.
            String query = "SELECT baseline_desc FROM Baseline ORDER BY baseline_id asc";

            // Create the statement.
            Statement st = Main.newconn.createStatement();

            // Get the result set from the query.
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) // Retrieve data from ResultSet
            {
                baselines.add(rs.getString("baseline_desc")); //4th column of Table
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }

        // Convert to observable list for FXML purposes.
        ObservableList bases = FXCollections.observableArrayList(baselines);

        return bases;
    }

    /**
     * Retrieves scicrs from database and stores them here for global program reference
     * @return
     */
    private static ObservableList<String> fetchSCICRsFromDB()
    {
        try
        {
            String baselineDesc = MainMenuModel.selectedBaseline.getValue();
            // get baseline_id the corresponds to the baseline_desc
            int baselineId = MainMenuModel.getBaselineLookupMap().get(baselineDesc);

            ArrayList<FilterItem> filterList = new ArrayList<FilterItem>();
            filterList.add(new FilterItem(Integer.toString(baselineId), "baseline_id"));

            // construct scicr al
            String scicr_ColumnLabel = "number";
            ArrayList<String> scicr_ArrayList = new ArrayList<String>();
            PreparedStatement scicr_Statement = QueryBuilder.buildSelectWhereOrderByQuery("SCICR", scicr_ColumnLabel, filterList, false, scicr_ColumnLabel, "asc");
            ResultSet scicr_ResultSet = scicr_Statement.executeQuery();
            while(scicr_ResultSet.next())
            {
                scicr_ArrayList.add(scicr_ResultSet.getString(scicr_ColumnLabel));
            }

            ObservableList<String> returnOL = FXCollections.observableArrayList(scicr_ArrayList);

            return returnOL;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Could not load SCICRs into global observable list." +
                            "Program closing.", ButtonType.OK);
            alert.showAndWait();
            System.exit(1);
        }
        return null; // should never return null
    }

    public static HashMap<Integer, String> getValCodesLookuMap()
    {
        try
        {
            /*
            String countQuery = "SELECT COUNT(*) AS rowcount FROM ValCodes";
            Statement countst = Main.newconn.createStatement();
            ResultSet countrs = countst.executeQuery(countQuery);
            int rowcount = 0;
            while(countrs.next())
            {
                rowcount = countrs.getInt("rowcount");
            }
            */

            //MainMenuModel.valCodesLookupMap = new HashMap<Integer, String>();

            String query = "SELECT [val_id], [field_value] FROM ValCodes ORDER BY val_id ASC";
            Statement st = Main.newconn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                MainMenuModel.valCodesLookupMap.put(Integer.parseInt(rs.getString("val_id")),
                        rs.getString("field_value"));
            }
            return MainMenuModel.valCodesLookupMap;
        }
        catch(Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Could not load valcodes lookup array into memory from the database.", ButtonType.OK);
            alert.showAndWait();
            return null;
        }
    }

    public static void refreshSCICRsFromDB()
    {
        MainMenuModel.scicrs = fetchSCICRsFromDB();
    }
}
