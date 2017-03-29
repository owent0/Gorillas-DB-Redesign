package ROMdb.Models;

import ROMdb.Controllers.MainMenuController;
import ROMdb.Driver.Main;
import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import ROMdb.Helpers.SCICRRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Anthony Orio on 3/28/2017.
 */
public class AddBaselineModel {
    public static MainMenuController mainMenuController;



    /**
     * Updating a baseline that is already in the baseline table in MS Access
     * Precondition: newBaseline is validated already
     * @param oldBaseline the current baseline in the baseline table
     * @param newBaseline the new value for the baseline in the baseline table
     *
     */
    public static void writeBaselineEditToDB(String oldBaseline, String newBaseline) throws SQLException {


        // The query to insert the data from the fields.
        String insertQuery =    "UPDATE baseline SET [baseline]=? WHERE [baseline]=? ";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        // This will search for the column name.
        st.setString(2, oldBaseline);

        // Parse all of the information and stage for writing.
        st.setString(1, newBaseline);


        // Perform the update inside of the table of the database.
        st.executeUpdate();

    }

    /**
     * Writing the baseline to the baseline table in MS Access
     * @throws Exception If it fails.
     */
    public static void writeBaseline(String baseline) throws Exception {

        baseline = baseline.trim();

        // Add the new baseline to the map.
        MainMenuModel.baselines.add(baseline);
        //System.out.println("Adding new baseline " + MainMenuModel.getBaselines().add(baseline));

        MainMenuModel.setSelectedBaseline(baseline);

        // The query to insert the data from the fields.
        String insertQuery =    "INSERT INTO baseline ([baseline]) VALUES (?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        /** Parse all of the information and stage for writing. */
        st.setString(1, baseline);

        // Perform the update inside of the table of the database.
        st.executeUpdate();

        ObservableList<SCICRRow> temp = FXCollections.observableArrayList();
        SCICRModel.getMap().put(baseline, temp);

        // The query to insert the data from the fields.
        insertQuery =    "INSERT INTO basicrom ([slocspermanday], [slocspermanmonth], [cprs], [IntergrationWeight],"
                + "[UnitTestWeight], [CodeWeight], [DefaultSLOCS], [DesignWeight], [CPDDDocument], [CPDDDate], [Budget Upgrade], "
                + "[Budget Maintenance], [DDR/CWT SLOCS], [baseline]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        st = Main.conn.prepareStatement(insertQuery);

        st.setString(1, "0.0");
        st.setString(2, "0.0");
        st.setString(3, "0.0");
        st.setString(4, "0.0");
        st.setString(5, "0.0");
        st.setString(6, "0.0");
        st.setString(7, "0.0");
        st.setString(8, "0.0");
        st.setString(9, "0.0");
        st.setString(10, "0.0");
        st.setString(11, "0.0");
        st.setString(12, "0.0");
        st.setString(13, "0.0");
        st.setString(14, baseline);

        // Perform the update inside of the table of the database.
        st.executeUpdate();
    }
}
