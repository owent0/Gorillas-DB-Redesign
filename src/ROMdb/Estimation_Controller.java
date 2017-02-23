package ROMdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Anthony Orio on 2/22/2017.
 */
public class Estimation_Controller {
    //Will have to make query for baseline Do that in controller store all baselines in arraylist to populate combo box
    double staffDay, seSupport, cscPmo, elementAdmin, elementTest, cpddGeneration, engineeringTest, cscMeitTesting, defaultSlocs, designWeight, codeWeight, unitTestingWeight, intergrationWeight;
    String cpddDocument, cprs, cpddDate;
    int budgetUpgrade, budgetMaint, ddrCwtSlocs;



    public Estimation_Controller() {

    }



    public double getStaffDay() {
        return staffDay;
    }

    public void setStaffDay(double staffDay) {
        this.staffDay = staffDay;
    }

    public double getSeSupport() {
        return seSupport;
    }

    public void setSeSupport(double seSupport) {
        this.seSupport = seSupport;
    }

    public double getCscPmo() {
        return cscPmo;
    }

    public void setCscPmo(double cscPmo) {
        this.cscPmo = cscPmo;
    }

    public double getElementAdmin() {
        return elementAdmin;
    }

    public void setElementAdmin(double elementAdmin) {
        this.elementAdmin = elementAdmin;
    }

    public double getElementTest() {
        return elementTest;
    }

    public void setElementTest(double elementTest) {
        this.elementTest = elementTest;
    }

    public double getCpddGeneration() {
        return cpddGeneration;
    }

    public void setCpddGeneration(double cpddGeneration) {
        this.cpddGeneration = cpddGeneration;
    }

    public double getEngineeringTest() {
        return engineeringTest;
    }

    public void setEngineeringTest(double engineeringTest) {
        this.engineeringTest = engineeringTest;
    }

    public double getCscMeitTesting() {
        return cscMeitTesting;
    }

    public void setCscMeitTesting(double cscMeitTesting) {
        this.cscMeitTesting = cscMeitTesting;
    }

    public double getDefaultSlocs() {
        return defaultSlocs;
    }

    public void setDefaultSlocs(double defaultSlocs) {
        this.defaultSlocs = defaultSlocs;
    }

    public double getDesignWeight() {
        return designWeight;
    }

    public void setDesignWeight(double designWeight) {
        this.designWeight = designWeight;
    }

    public double getCodeWeight() {
        return codeWeight;
    }

    public void setCodeWeight(double codeWeight) {
        this.codeWeight = codeWeight;
    }

    public double getUnitTestingWeight() {
        return unitTestingWeight;
    }

    public void setUnitTestingWeight(double unitTestingWeight) {
        this.unitTestingWeight = unitTestingWeight;
    }

    public double getIntergrationWeight() {
        return intergrationWeight;
    }

    public void setIntergrationWeight(double intergrationWeight) {
        this.intergrationWeight = intergrationWeight;
    }

    public String getCpddDocument() {
        return cpddDocument;
    }

    public void setCpddDocument(String cpddDocument) {
        this.cpddDocument = cpddDocument;
    }

    public String getCprs() {
        return cprs;
    }

    public void setCprs(String cprs) {
        this.cprs = cprs;
    }

    public String getCpddDate() {
        return cpddDate;
    }

    public void setCpddDate(String cpddDate) {
        this.cpddDate = cpddDate;
    }

    public int getBudgetUpgrade() {
        return budgetUpgrade;
    }

    public void setBudgetUpgrade(int budgetUpgrade) {
        this.budgetUpgrade = budgetUpgrade;
    }

    public int getBudgetMaint() {
        return budgetMaint;
    }

    public void setBudgetMaint(int budgetMaint) {
        this.budgetMaint = budgetMaint;
    }

    public int getDdrCwtSlocs() {
        return ddrCwtSlocs;
    }

    public void setDdrCwtSlocs(int ddrCwtSlocs) {
        this.ddrCwtSlocs = ddrCwtSlocs;
    }

    public static void oldWriteToDB() {
        try
        {
            //Connection conn = DriverManager.getConnection(
            //            "jdbc:ucanaccess://TestDatabase.accdb");

            Connection conn = DriverManager.getConnection(
                    "jdbc:ucanaccess://C://Users//Anthony Orio//Desktop//Rowan//Software Engineering//Project//Gorillas-DB-Redesign//src//ROMdb//rom_dcti Update 2012 Rev 1.mdb");
            Statement st = conn.createStatement();
            String query = "SELECT * FROM basicrom";
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

    public void writeToDB(double staffDay, double seSupport, double cscPmo, double elementAdmin, double elementTest, double cpddGeneration, double engineeringTest, double cscMeitTesting, double defaultSlocs, double designWeight, double codeWeight, double unitTestingWeight, double intergrationWeight, String cpddDocument, String cprs, String cpddDate, int budgetUpgrade, int budgetMaint, int ddrCwtSlocs) {
        //TODO write to DB
    }

    public static void main(String[] args) {
        oldWriteToDB();
    }
}
