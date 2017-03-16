package ROMdb.Controllers;

import ROMdb.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by chris on 3/15/2017.
 */
public class SCICRCreationController {

    private Connection conn = null;

    @FXML private Label label_scicr;
    @FXML private Label label_title;
    @FXML private Label label_number;
    @FXML private Label label_build;
    @FXML private Label label_baseline;

    @FXML private TextField field_title;
    @FXML private TextField field_number;
    @FXML private TextField field_build;

    @FXML private RadioButton radio_sc;
    @FXML private RadioButton radio_icr;

    @FXML private ComboBox<String> combo_baseline;

    @FXML private Button button_save;
    @FXML private Button button_newBaseline;


    @FXML
    public void initialize()
    {
        try {
            // Establish the connection.
            this.conn = DriverManager.getConnection(Main.dbPath);

            combo_baseline.setItems(fillBaselineFromDB());
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method will read all of the baselines currently stored within
     * the baseline database table.
     *
     * @return ObservableList the list containing the baseline from the baselines table.
     */
    private ObservableList<String> fillBaselineFromDB() {

        // The list to store the baselines in temporarily.
        ArrayList<String> baselines = new ArrayList<String>();

        try
        {
            // Grab all the baselines.
            String query = "SELECT * FROM basicrom";

            // Create the statement.
            Statement st = conn.createStatement();

            // Get the result set from the query.
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) // Retrieve data from ResultSet
            {
                baselines.add(rs.getString(3)); //4th column of Table
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
     * This method will parse the information currently in the text
     * fields and write them into the database column specified by the
     * names within the insertQuery string.
     */
    @FXML
    public void saveSCICR()
    {
        try
        {
            // The currently selected baseline from the drop down.
            String baseline = combo_baseline.getSelectionModel().getSelectedItem();
            String SCorICR = "";

            if(radio_icr.isSelected()){
                SCorICR = radio_icr.getText();
            }else{
                SCorICR = radio_sc.getText();
            }

            System.out.println(SCorICR);

            // The query to insert the data from the fields.
            String insertQuery =    "INSERT INTO scdata ([SC_ICR Number], [type], [title], [function], [SC Baseline]) VALUES (?, ?, ?, ?, ?)";

            // Create a new statement.
            PreparedStatement st = conn.prepareStatement(insertQuery);

            /** Parse all of the information and stage for writing. */
            st.setString(1, field_number.getText());
            st.setString(2, SCorICR);
            st.setString(3, field_title.getText());
            st.setString(4, field_build.getText());
            st.setString(5, baseline);

            // Perform the update inside of the table of the database.
            st.executeUpdate();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }

        //button_newSCICR.getScene().getWindow().hide();
    }


}
