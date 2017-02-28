package ROMdb.Controllers;

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

    private final static String path =
            "jdbc:ucanaccess://C://Users//Anthony Orio//Desktop//Rowan//Software Engineering//Project//Gorillas-DB-Redesign//src//ROMdb//rom_dcti Update 2012 Rev 1.mdb";

    private double  staffDay, staffMonth, cprs, integrationWeight,
                    unitTestingWeight, codeWeight, defaultSlocs,
                    designWeight, budgetUpgrade, budgetMaint,
                    ddrCwtSlocs;

    private final double STAFF_MONTH_DIVISOR = 20.92;

    private Connection conn = null;

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


    @FXML
    public void errorChecking() {
        String error = "";
        String parsed = "";
        String pattern = "\\d+(\\.\\d{1,25})?";
        boolean errorExists = false;


        parsed = field_staffMonth.getText();
        if (parsed.matches(pattern)) {
            this.staffMonth = Double.parseDouble(parsed);
        } else {
            error += "SLOCs/Staff Month\n";
            errorExists = true;
        }

        parsed = field_cprs.getText();
        if (parsed.matches(pattern)) {
            this.cprs = Double.parseDouble(parsed);
        } else {
            error += "CPRS#\n";
            errorExists = true;
        }

        parsed = field_integrationWeight.getText();
        if (parsed.matches(pattern)) {
            this.integrationWeight = Double.parseDouble(parsed);
        } else {
            error += "Integration Weight\n";
            errorExists = true;
        }

        parsed = field_unitTestingWeight.getText();
        if (parsed.matches(pattern)) {
            this.unitTestingWeight = Double.parseDouble(parsed);
        } else {
            error += "Unit Testing Weight\n";
            errorExists = true;
        }

        parsed = field_codeWeight.getText();
        if (parsed.matches(pattern)) {
            this.codeWeight = Double.parseDouble(parsed);
        } else {
            error += "Code Weight\n";
            errorExists = true;
        }

        parsed = field_defaultSlocs.getText();
        if (parsed.matches(pattern)) {
            this.defaultSlocs = Double.parseDouble(parsed);
        } else {
            error += "Default SLOCS\n";
            errorExists = true;
        }

        parsed = field_designWeight.getText();
        if (parsed.matches(pattern)) {
            this.designWeight = Double.parseDouble(parsed);
        } else {
            error += "Design Weight\n";
            errorExists = true;
        }

        parsed = field_budgetUpgrade.getText();
        if (parsed.matches(pattern)) {
            this.budgetUpgrade = Double.parseDouble(parsed);
        } else {
            error += "Budget Upgrade\n";
            errorExists = true;
        }

        parsed = field_budgetMaint.getText();
        if (parsed.matches(pattern)) {
            this.budgetMaint = Double.parseDouble(parsed);
        } else {
            error += "Budget Maintenance\n";
            errorExists = true;
        }

        parsed = field_ddrCwtSlocs.getText();
        if (parsed.matches(pattern)) {
            this.ddrCwtSlocs = Double.parseDouble(parsed);
        } else {
            error += "DDR CWR SLOCS\n";
            errorExists = true;
        }

        if ((designWeight + codeWeight + integrationWeight + unitTestingWeight) != 100) {
            error = "Your weights do not add up to 100";
            JOptionPane.showMessageDialog(null, error);
        }

        if (errorExists) {
            error = "You've entered the wrong input for: \n" + error;
            JOptionPane.showMessageDialog(null, error);
        }
        else {
            writeTextfieldsToDB();
        }


    }

    @FXML
    private void calculateStaffDay() {

        String result = "";
        if (field_staffMonth.getText().equals("")) {
            result = "0.0";
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            staffDay
                    = Double.parseDouble(field_staffMonth.getText()) / STAFF_MONTH_DIVISOR ;
            result = df.format(staffDay);
        }

        field_staffDay.setText(result);
    }

    public void writeTextfieldsToDB() {

        try
        {

            // Connection conn = DriverManager.getConnection(path);

            //INSERT INTO CUSTOMERS (ID,NAME,AGE,ADDRESS,SALARY)
            //VALUES (2, 'Khilan', 25, 'Delhi', 1500.00 );

            /*

            UPDATE table_name
            SET column1 = value1, column2 = value2...., columnN = valueN
            WHERE [condition];


            String q = "INSERT INTO db1 ([ID], [NAME]) VALUES (?, ?)";
            PreparedStatement st = cn.prepareStatement (q);
            st.setString(1, "a");
            st.setString(2, "b");
            st.executeUpdate();
            */

            String baseline = combo_estimateBaseline.getSelectionModel().getSelectedItem();
            //System.out.println(baseline);
            String insertQuery = "UPDATE basicrom SET [slocspermanday]=?, [slocspermanmonth]=?, [cprs]=?, [IntergrationWeight]=?, "
                + "[UnitTestWeight]=?, [CodeWeight]=?, [DefaultSLOCS]=?, [DesignWeight]=?, [CPDDDocument]=?, [CPDDDate]=?, [Budget Upgrade]=?, "
                + "[Budget Maintenance]=?, [DDR/CWT SLOCS]=? WHERE [baseline]=?";

            PreparedStatement st = conn.prepareStatement(insertQuery);

            st.setString(1, field_staffDay.getText());
            st.setString(2, field_staffMonth.getText());
            st.setString(3, field_cprs.getText());
            st.setString(4, Double.toString((Double.parseDouble(field_integrationWeight.getText()) / 100)));
            st.setString(5, Double.toString((Double.parseDouble(field_unitTestingWeight.getText()) / 100)));
            st.setString(6, Double.toString((Double.parseDouble(field_codeWeight.getText()) / 100)));
            st.setString(7, field_defaultSlocs.getText());
            st.setString(8, Double.toString((Double.parseDouble(field_designWeight.getText()) / 100)));
            st.setString(9, field_cpddDocument.getText());
            st.setString(10, field_cpddDate.getText());
            st.setString(11, field_budgetUpgrade.getText());
            st.setString(12, field_budgetMaint.getText());
            st.setString(13, field_ddrCwtSlocs.getText());
            st.setString(14, baseline);

            System.out.println(st.toString());

            st.executeUpdate();

/*

            insertQuery += Double.parseDouble(field_staffDay.getText()) + ", ";
            insertQuery += Double.parseDouble(field_staffMonth.getText()) + ", ";
            insertQuery += field_cprs.getText() + ", ";
            insertQuery += Double.parseDouble(field_integrationWeight.getText()) + ", ";
            insertQuery += Double.parseDouble(field_unitTestingWeight.getText()) + ", ";
            insertQuery += Double.parseDouble(field_codeWeight.getText()) + ", ";

            insertQuery += Double.parseDouble(field_defaultSlocs.getText()) + ", ";
            insertQuery += Double.parseDouble(field_designWeight.getText()) + ", ";
            insertQuery += field_cpddDocument.getText() + ", ";
            insertQuery += field_cpddDate.getText() + ", ";
            insertQuery += Double.parseDouble(field_budgetUpgrade.getText()) + ", ";
            insertQuery += Double.parseDouble(field_budgetMaint.getText()) + ", ";
            insertQuery += Double.parseDouble(field_ddrCwtSlocs.getText());

            insertQuery += ")";

            System.out.println("'" + insertQuery + "'");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeUpdate(insertQuery);
*/
            //conn.close();
        }
        catch (Exception e)
        {
            System.out.println("Exception : " + e);
        }

    }


    public void readFromDB() {

    }

    @FXML
    public void initialize()
    {
        try
        {
            this.conn = DriverManager.getConnection(path);

            combo_estimateBaseline.setItems(fillBaselineFromDB());

            fillTextFieldsFromDB();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @FXML
    private void fillTextFieldsFromDB() {

        try {
           // Connection conn = DriverManager.getConnection(path);

            String baseline = combo_estimateBaseline.getSelectionModel().getSelectedItem();
            System.out.println(baseline);
            String query = "SELECT * FROM basicrom";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);


            while (rs.next()) // Retrieve data from ResultSet
            {
                if( rs.getString(3).equals(baseline) )
                {

                    field_staffDay.setText(rs.getString("slocspermanday"));
                    field_staffMonth.setText(rs.getString("slocspermanmonth"));
                    field_cprs.setText(rs.getString("cprs"));
                    field_integrationWeight.setText(Double.toString(Double.parseDouble(rs.getString("IntergrationWeight")) * 100));
                    field_unitTestingWeight.setText(Double.toString(Double.parseDouble(rs.getString("UnitTestWeight")) * 100));
                    field_codeWeight.setText(Double.toString(Double.parseDouble(rs.getString("CodeWeight")) * 100));

                    field_defaultSlocs.setText(rs.getString("DefaultSLOCS"));
                    field_designWeight.setText(Double.toString(Double.parseDouble(rs.getString("DesignWeight")) * 100));
                    field_cpddDocument.setText(rs.getString("CPDDDocument"));
                    field_cpddDate.setText(rs.getString("CPDDDate"));
                    field_budgetUpgrade.setText(rs.getString("Budget Upgrade"));
                    field_budgetMaint.setText(rs.getString("Budget Maintenance"));
                    field_ddrCwtSlocs.setText(rs.getString("DDR/CWT SLOCS"));

                    break;
                }


            }
            //conn.close();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }


    private ObservableList<String> fillBaselineFromDB() {
        ArrayList<String> baselines = new ArrayList<String>();

        try {
            //Connection conn = DriverManager.getConnection(path);

            String query = "SELECT * FROM basicrom";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) // Retrieve data from ResultSet
            {
                baselines.add(rs.getString(3)); //4th column of Table
            }
            //conn.close();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }

        ObservableList bases = FXCollections.observableArrayList(baselines);
        return bases;
    }
}
















//        try
//        {
//            //Connection conn = DriverManager.getConnection(
//            //            "jdbc:ucanaccess://TestDatabase.accdb");
//
//            Connection conn = DriverManager.getConnection(
//                    "jdbc:ucanaccess://C://Users//Anthony Orio//Desktop//Rowan//Software Engineering//Project//Gorillas-DB-Redesign//src//ROMdb//rom_dcti Update 2012 Rev 1.mdb");
//
//            String query = "SELECT * FROM basicrom";
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(query);
//
//            while(rs.next()) // Retrieve data from ResultSet
//            {
//                System.out.print("Serial number : "+ rs.getString(1)); //1st column of Table from database
//                System.out.print(" , Name : "+ rs.getString(2)); //2nd column of Table
//                System.out.print(" and Age : "+ rs.getString(3)); //4th column of Table
//                System.out.println(" , City : "+ rs.getString(4)); //3rd column of Table
//
//            }
//            //s.close();
//            conn.close();
//        }
//        catch (Exception e)
//        {
//            System.out.println("Exception : "+ e);
//        }










/*
String baseline = combo_estimateBaseline.getSelectionModel().getSelectedItem();
            System.out.println(baseline);
            String insertQuery = "INSERT INTO basicrom VALUES ([slocspermanday], [slocspermanmonth], [cprs], [IntergrationWeight], "
                + "[UnitTestWeight], [CodeWeight], [DefaultSLOCS], [DesignWeight], [CPDDDocument], [CPDDDate], [Budget Upgrade], "
                + "[Budget Maintenance], [DDR/CWT SLOCS]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement st = conn.prepareStatement(insertQuery);

            st.setString(1, field_staffDay.getText());
            st.setString(2, field_staffMonth.getText());
            st.setString(3, field_cprs.getText());
            st.setString(4, field_integrationWeight.getText());
            st.setString(5, field_unitTestingWeight.getText());
            st.setString(6, field_codeWeight.getText());
            st.setString(7, field_defaultSlocs.getText());
            st.setString(8, field_designWeight.getText());
            st.setString(9, field_cpddDocument.getText());
            st.setString(10, field_cpddDate.getText());
            st.setString(11, field_budgetUpgrade.getText());
            st.setString(12, field_budgetMaint.getText());
            st.setString(13, field_ddrCwtSlocs.getText());

            st.executeUpdate();
 */