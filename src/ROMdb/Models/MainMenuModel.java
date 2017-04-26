package ROMdb.Models;

import ROMdb.Controllers.ChangeAdminPasswordController;
import ROMdb.Controllers.EstimationBaseController;
import ROMdb.Controllers.LoginController;
import ROMdb.Controllers.SCICRController;
import ROMdb.Driver.Main;
import ROMdb.Helpers.QueryBuilder;
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

/**
 * Created by derek on 3/25/2017.
 */
public class MainMenuModel
{
    public static EstimationBaseController estimationBaseController;
    public static SCICRController sCICRController;
    public static LoginController loginController;
    public static ChangeAdminPasswordController changeAdminPasswordController;

    public static ObservableStringValue selectedBaseline = new SimpleStringProperty("Baseline");
    public static ObservableList<String> baselines = fetchBaselinesFromDB();
    public static ObservableList<String> scicrs = fetchSCICRsFromDB();

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
            String query = "SELECT * FROM baseline";

            // Create the statement.
            Statement st = Main.conn.createStatement();

            // Get the result set from the query.
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) // Retrieve data from ResultSet
            {
                baselines.add(rs.getString("baseline")); //4th column of Table
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
            // construct scicr al
            String scicr_ColumnLabel = "Number";
            ArrayList<String> scicr_ArrayList = new ArrayList<String>();
            PreparedStatement scicr_Statement = QueryBuilder.buildSelectOrderByQuery("SCICRData", scicr_ColumnLabel, scicr_ColumnLabel, "asc");
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
}
