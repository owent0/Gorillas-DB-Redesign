/**
 * This controller will be used to perform all actions that are
 * on the estimation base menu. It will handle sending and retrieving
 * information to the database about ROM entries.
 */

package ROMdb.Controllers;

import ROMdb.Main;
import ROMdb.ScicrRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class EstimationController {

    // Connection for database.
    private Connection conn = null;

    // For calculating staff day.
    private final double STAFF_MONTH_DIVISOR = 20.92;

    /** The components on the GUI                           */
    @FXML private Pane estimationBase;

    @FXML private Label label_baseline;
    @FXML private Label label_cprs;
    @FXML private Label label_staffMonth;
    @FXML private Label label_staffDay;
    @FXML private Label label_ddrCwtSlocs;
    @FXML private Label label_cpddDocument;
    @FXML private Label label_cpddDate;
    @FXML private Label label_budgetUpgrade;
    @FXML private Label label_budgetMaintenance;
    @FXML private Label label_designWeight;
    @FXML private Label label_codeWeight;
    @FXML private Label label_unitTestingWeight;
    @FXML private Label label_integrationWeight;
    @FXML private Label label_defaultSlocs;

    @FXML private ComboBox<String> combo_estimateBaseline;

    @FXML private TextField field_staffDay;
    @FXML private TextField field_staffMonth;
    @FXML private TextField field_cprs;
    @FXML private TextField field_integrationWeight;
    @FXML private TextField field_unitTestingWeight;
    @FXML private TextField field_codeWeight;
    @FXML private TextField field_defaultSlocs;
    @FXML private TextField field_designWeight;
    @FXML private TextField field_cpddDocument;
    @FXML private TextField field_cpddDate;
    @FXML private TextField field_budgetUpgrade;
    @FXML private TextField field_budgetMaint;
    @FXML private TextField field_ddrCwtSlocs;

    @FXML private Button button_estimateSubmit;
    /** End GUI components                                          */


    /**
     * This method will evaluate each text field and ensure that
     * no illegal values are inserted into the fields. If fields
     * are suppose to contain numbers, then they will be compared
     * to a specific number format pattern. If fields are left blank,
     * then the user will be told to enter a value. Finally, the
     * weight fields will be evaluated to ensure they add up to 100.
     */
    @FXML
    public void errorChecking() {

        // Stores the error message if any.
        String error = "";

        // Stores the text from the next evaluated field.
        String parsed = "";

        // The pattern correlates to any decimal number up to 25
        // decimal digits.
        String pattern = "\\d+(\\.\\d{1,25})?";

        // Flag if an error is found during any checks.
        boolean errorExists = false;


        // SLOCS/Staff-Month check
        parsed = field_staffMonth.getText().trim();
        if (!parsed.matches(pattern))
        {
            error += "SLOCs/Staff Month\n";
            errorExists = true;
        }


        // Integration Weight check
        parsed = field_integrationWeight.getText();
        if (!parsed.matches(pattern))
        {
            error += "Integration Weight\n";
            errorExists = true;
        }

        // Unit Testing Weight check
        parsed = field_unitTestingWeight.getText();
        if (!parsed.matches(pattern))
        {
            error += "Unit Testing Weight\n";
            errorExists = true;
        }

        // Code Weight check
        parsed = field_codeWeight.getText();
        if (!parsed.matches(pattern))
        {
            error += "Code Weight\n";
            errorExists = true;
        }

        // Default SLOCS check
        parsed = field_defaultSlocs.getText();
        if (!parsed.matches(pattern))
        {
            error += "Default SLOCS\n";
            errorExists = true;
        }

        // Design Weight check
        parsed = field_designWeight.getText();
        if (!parsed.matches(pattern))
        {
            error += "Design Weight\n";
            errorExists = true;
        }

        // Budget Upgrade check
        parsed = field_budgetUpgrade.getText();
        if (!parsed.matches(pattern))
        {
            error += "Budget Upgrade\n";
            errorExists = true;
        }

        // Budget Maintenance check
        parsed = field_budgetMaint.getText();
        if (!parsed.matches(pattern))
        {
            error += "Budget Maintenance\n";
            errorExists = true;
        }

        // DDR/CWT/SLOCS check
        parsed = field_ddrCwtSlocs.getText();
        if (!parsed.matches(pattern))
        {
            error += "DDR CWR SLOCS\n";
            errorExists = true;
        }

        // CPDD Document check
        if (field_cpddDocument.getText() == null || field_cpddDocument.getText().trim().equals(""))
        {
            error += "CPDD Document\n";
            errorExists = true;
        }

        // CPDD Date check
        if (field_cpddDate.getText() == null || field_cpddDate.getText().trim().equals(""))
        {
            error += "CPDD Date\n";
            errorExists = true;
        }

        // CPRS# check
        if (field_cprs.getText() == null || field_cprs.getText().trim().equals(""))
        {
            error += "CPRS#\n";
            errorExists = true;
        }


        if (errorExists)    // If flag set during evaluations.
        {
            // Assemble the error message.
            error = "You've entered the wrong input for: \n" + error;

            // Display box containing the error message.
            JOptionPane.showMessageDialog(null, error);

            return;     // Do not continue
        }


        // We need to check to make sure the weights add to 100.
        if (   (Double.parseDouble(field_designWeight.getText())        +
                Double.parseDouble(field_codeWeight.getText())          +
                Double.parseDouble(field_integrationWeight.getText())   +
                Double.parseDouble(field_unitTestingWeight.getText())   ) != 100)
        {

            // Assemble the error message.
            String weightError = "Your weights do not add up to 100";

            // Display box containing the error message.
            JOptionPane.showMessageDialog(null, weightError);

            return;     // Do not continue
        }

        // All evaluations have passed.
        // Call method to write the valid data.
        writeTextfieldsToDB();
    }


    /**
     * This method will calculate the SLOCS/Staff-Day field
     * based on the following formula:
     *         (SLOCS/Staff-Month / 20.92) = SLOCS/Staff-Day
     *
     */
    @FXML
    private void calculateStaffDay()
    {
        // We will store the answer here.
        double staffDay;

        // The formatted result #.##
        String result = "";

        // If the field is currently empty then result is 0.0
        if (field_staffMonth.getText().equals(""))
        {
            result = "0.0";
        }

        // Otherwise generate the value.
        else
        {
            // Declare the format
            DecimalFormat df = new DecimalFormat("#.##");

            // Find the value.
            staffDay = Double.parseDouble(field_staffMonth.getText()) / STAFF_MONTH_DIVISOR ;

            // Format the value.
            result = df.format(staffDay);
        }

        // Set the field text with the formatted value.
        field_staffDay.setText(result);
    }

    /**
     * This method will parse the information currently out of the text
     * fields and right them into the database column specified by the
     * names within the insertQuery string.
     */
    public void writeTextfieldsToDB()
    {
        try
        {
            // The currently selected baseline from the drop down.
            String baseline = combo_estimateBaseline.getSelectionModel().getSelectedItem();

            // The query to insert the data from the fields.
            String insertQuery =    "UPDATE basicrom SET [slocspermanday]=?, [slocspermanmonth]=?, [cprs]=?, [IntergrationWeight]=?, "
                                 + "[UnitTestWeight]=?, [CodeWeight]=?, [DefaultSLOCS]=?, [DesignWeight]=?, [CPDDDocument]=?, [CPDDDate]=?, [Budget Upgrade]=?, "
                                 + "[Budget Maintenance]=?, [DDR/CWT SLOCS]=? WHERE [baseline]=?";

            // Create a new statement.
            PreparedStatement st = conn.prepareStatement(insertQuery);

            /** Parse all of the information and stage for writing. */
            st.setString(1, field_staffDay.getText());
            st.setString(2, field_staffMonth.getText());
            st.setString(3, field_cprs.getText());
            st.setString(7, field_defaultSlocs.getText());
            st.setString(9, field_cpddDocument.getText());
            st.setString(10, field_cpddDate.getText());
            st.setString(11, field_budgetUpgrade.getText());
            st.setString(12, field_budgetMaint.getText());
            st.setString(13, field_ddrCwtSlocs.getText());
            st.setString(14, baseline);


            /** The weights needs to be divided since they are percentages in database. */
            st.setString(4, Double.toString(
                    (Double.parseDouble(field_integrationWeight.getText()) / 100)));

            st.setString(5, Double.toString(
                    (Double.parseDouble(field_unitTestingWeight.getText()) / 100)));

            st.setString(6, Double.toString(
                    (Double.parseDouble(field_codeWeight.getText()) / 100)));

            st.setString(8, Double.toString(
                    (Double.parseDouble(field_designWeight.getText()) / 100)));


            // Perform the update inside of the table of the database.
            st.executeUpdate();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }


    /**
     * This method will automatically run as soon as the program
     * is started. It will establish a database connection and
     * set the baseline drop down menu with the values from the
     * baseline table in the database.
     */
    @FXML
    public void initialize()
    {
        try
         {
            // Establish the connection.
            this.conn = DriverManager.getConnection(Main.dbPath);

            // Fill the baseline drop down menu with current baselines.
            combo_estimateBaseline.setItems(fillBaselineFromDB());
            //fillTextFieldsFromDB();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method will take the information from the database table
     * "basicrom" and display it inside the correct text field when
     * the user chooses the baseline from the drop down menu. This information
     * can then be updated if desired.
     */
    @FXML
    private void fillTextFieldsFromDB()
    {
        try
        {
            // Grab the current selected baseline from drop down.
            String baseline = combo_estimateBaseline.getSelectionModel().getSelectedItem();

            // Create query
            String query = "SELECT * FROM basicrom";

            // Create the statement to send.
            Statement st = conn.createStatement();

            // Return the result set from this query.
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) // Retrieve data from ResultSet
            {
                if( rs.getString(3).equals(baseline) )
                {

                    /** Write all of the information into the correct text field. */
                    field_staffDay.setText(rs.getString     ("slocspermanday"));
                    field_staffMonth.setText(rs.getString   ("slocspermanmonth"));
                    field_cprs.setText(rs.getString         ("cprs"));
                    field_defaultSlocs.setText(rs.getString ("DefaultSLOCS"));
                    field_cpddDocument.setText(rs.getString ("CPDDDocument"));
                    field_cpddDate.setText(rs.getString     ("CPDDDate"));
                    field_budgetUpgrade.setText(rs.getString("Budget Upgrade"));
                    field_budgetMaint.setText(rs.getString  ("Budget Maintenance"));
                    field_ddrCwtSlocs.setText(rs.getString  ("DDR/CWT SLOCS"));

                    /** These numbers are percentages in the table and must be multiplied by 100. */
                   field_integrationWeight.setText(Double.toString(
                            Double.parseDouble(
                                    rs.getString("IntergrationWeight")) * 100));

                    field_unitTestingWeight.setText(Double.toString(
                            Double.parseDouble(
                                    rs.getString("UnitTestWeight")) * 100));

                    field_codeWeight.setText(Double.toString(
                            Double.parseDouble(
                                    rs.getString("CodeWeight")) * 100));

                    field_designWeight.setText(Double.toString(
                            Double.parseDouble(
                                    rs.getString("DesignWeight")) * 100));

                    break;
                }
            }
        }
        catch (Exception e)
        {
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



}

