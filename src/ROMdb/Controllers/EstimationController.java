package ROMdb.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EstimationController {

    private final String path = "";

    private double  staffDay, staffMonth, cprs, integrationWeight,
                    unitTestingWeight, codeWeight, defaultSlocs,
                    designWeight, budgetUpgrade, budgetMaint, ddrCwtSlocs;

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

    @FXML private ComboBox<?> combo_estimateBaseline;


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

    public void test1() {
        System.out.println("Test");

    }


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

        if (errorExists) {
            error = "You've entered the wrong input for: \n" + error;
            JOptionPane.showMessageDialog(null, error);
        }

        if( (designWeight + codeWeight + integrationWeight + unitTestingWeight) != 100) {
            error = "Your weights do not add up to 100";
            JOptionPane.showMessageDialog(null, error);
        }
    }



    //          String s = "          Hello World                    ";
    // ---->     s.trim()
    // ---->     s = "Hello World"
    public void writeToDB() {


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