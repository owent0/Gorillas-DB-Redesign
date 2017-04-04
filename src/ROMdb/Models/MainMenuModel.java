package ROMdb.Models;

import ROMdb.Controllers.EstimationBaseController;
import ROMdb.Controllers.SCICRController;
import ROMdb.Driver.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by derek on 3/25/2017.
 */
public class MainMenuModel {

    public static EstimationBaseController estimationBaseController;
    public static SCICRController sCICRController;

    public static ObservableStringValue selectedBaseline = new SimpleStringProperty("Baseline");
    public static ObservableList<String> baselines = fetchBaselinesFromDB();

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
    private static ObservableList<String> fetchBaselinesFromDB() {

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
}
