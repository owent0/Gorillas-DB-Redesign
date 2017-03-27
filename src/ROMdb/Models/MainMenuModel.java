package ROMdb.Models;

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

    public static ObservableStringValue selectedBaseline = new SimpleStringProperty("Baseline");
    //public static String selectedBaseline = "Baseline";
    public static ObservableList<String> baselines = fetchBaselinesFromDB();
    // public static ObservableList<String> baselines = FXCollections.observableArraylist


    public static ObservableList<String> getBaselines() {
        return baselines;
    }

    public static void setBaselines(ObservableList<String> baselines) {
        MainMenuModel.baselines = baselines;
    }

    public static String getSelectedBaseline() {
        return selectedBaseline.getValue();
    }

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
