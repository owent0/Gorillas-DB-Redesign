package ROMdb.Models;

import ROMdb.Controllers.MainMenuController;
import ROMdb.Driver.Main;
import ROMdb.Helpers.SCICRRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Anthony Orio on 3/28/2017.
 */
public class AddBaselineModel
{
    public static MainMenuController mainMenuController;


    /**
     * Writing a new baseline to the baseline table in MS Access
     * @param  baseline The baseline to write to the database.
     * @throws Exception If it fails.
     */
    public static void writeBaseline(String baseline) throws Exception {

        String insertBaselineQuery = "INSERT INTO Baseline ([baseline_desc], [cprs], [slocs_per_day], " +
                "[slocs_per_month], [slocs_default], [slocs_ddr_cwt], [cpdd_document], [cpdd_date], [budget_upgrade], " +
                "[budget_maintenance], [design_weight], [code_weight], [integration_weight], [unit_test_weight]) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.newconn.prepareStatement(insertBaselineQuery);

        // Estimation base default data
        st.setString(1, baseline);
        st.setString(2, "WS-xxxx");
        st.setDouble(3, 17.21);
        st.setDouble(4, 360);
        st.setDouble(5, 100.00);
        st.setDouble(6, 125.00);
        st.setString(7, "WS-xxxxx");
        st.setString(8, "");
        st.setDouble(9, 0.0);
        st.setDouble(10, 0.0);
        st.setDouble(11, 30.0);
        st.setDouble(12, 30.0);
        st.setDouble(13, 25.0);
        st.setDouble(14, 15.0);

        // Perform the update inside of the table of the database.
        st.executeUpdate();
    }
}
