package ROMdb.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EstimationController {

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
    @FXML private Label label_intergrationWeight;
    @FXML private Label label_defaultSlocs;

    @FXML private ComboBox<?> combo_estimateBaseline;

    @FXML private TextField field_staffDay;
    @FXML private TextField field_staffMonth;
    @FXML private TextField field_cprs;
    @FXML private TextField field_intergrationWeight;
    @FXML private TextField field_unitTestingWeight;
    @FXML private TextField field_codeWeight;
    @FXML private TextField field_defaultSlocs;
    @FXML private TextField field_designWeight;
    @FXML private TextField field_cpddDocument;
    @FXML private TextField field_cpddDate;
    @FXML private TextField field_budgetUpgrade;
    @FXML private TextField field_budgetMaint;
    @FXML private TextField field_ddrCwtSlocs;

    @FXML private Button button_esimateSubmit;


    public void test1() {
        System.out.println("Test");
    }

    public void writeToDB() {
        try
        {
            //Connection conn = DriverManager.getConnection(
            //            "jdbc:ucanaccess://TestDatabase.accdb");

            Connection conn = DriverManager.getConnection(
                    "jdbc:ucanaccess://C://Users//Anthony Orio//Desktop//Rowan//Software Engineering//Project//Gorillas-DB-Redesign//src//ROMdb//rom_dcti Update 2012 Rev 1.mdb");

            String query = "SELECT * FROM basicrom";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()) // Retrieve data from ResultSet
            {
                System.out.print("Serial number : "+ rs.getString(1)); //1st column of Table from database
                System.out.print(" , Name : "+ rs.getString(2)); //2nd column of Table
                System.out.print(" and Age : "+ rs.getString(3)); //4th column of Table
                System.out.println(" , City : "+ rs.getString(4)); //3rd column of Table

            }
            //s.close();
            conn.close();
        }
        catch (Exception e)
        {
            System.out.println("Exception : "+ e);
        }
    }
}
