package ROMdb.Models;

import ROMdb.Driver.Main;
import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Tom on 3/24/2017.
 */
public class AddSCICRModel {
    public static void saveSCICR(String baseline, String type, String number, String title, String build) throws Exception {

        errorsExist(baseline, title, number, build);

        // The query to insert the data from the fields.
        String insertQuery =    "INSERT INTO SCICRData ([Number], [Type], [Title], [Build], [Baseline]) VALUES (?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        /** Parse all of the information and stage for writing. */
        st.setString(1, number.trim());
        st.setString(2, type.trim());
        st.setString(3, title.trim());
        st.setString(4, build.trim());
        st.setString(5, baseline.trim());

        // Perform the update inside of the table of the database.
        st.executeUpdate();
    }

    public static boolean isNumberUnique(String number, String baseline) throws Exception{
        // The query to insert the data from the fields.
        boolean inDatabase = true;
        String insertQuery =    "SELECT COUNT(*) as NUM_MATCHES FROM SCICRData WHERE Number = ? AND Baseline = ?";

        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        st.setString(1, number.trim());
        st.setString(2, baseline.trim());

        int count = 0;
        ResultSet rs = null;
        try
        {
            rs = st.executeQuery();
            while(rs.next())
            {
                count = rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //...
        }


        if (count == 0) {
            inDatabase = false;
            System.out.println("Not in table for baseline");
        }
        return inDatabase;
    }

    /**
     * Makes sure that the user inputted correct input that matches the patterns
     * specified by the programmer.
     * @param inputString the string to check validity for.
     * @return true if the string is valid.
     * @throws InputFormatException If input is not correct.
     */
    public static boolean isValidInput(String inputString) throws InputFormatException
    {
        try{
            // The value is alpha numeric only with no spaces.
            InputValidator.checkPatternMatch(inputString, InputType.ALPHA_NUMERIC_SPACE);

            // The value contains no white space.
            InputValidator.checkPatternDoesNotMatch(inputString, InputType.WHITE_SPACE);
            return true;
        }
        catch(Exception e) {

            // If it failed.
            return false;
        }
    }

    /**
     * Checks to see if any errors exist, such as non alpha-numeric
     * characters.
     * @return true if there exists an error in the input.
     */
    public static void errorsExist(String baseline, String title, String number, String build) throws InputFormatException
    {
        // Check that each field is valid to insert.
        if(!isValidInput(title)
                || !isValidInput(number)
                || !isValidInput(build))
        {
            throw new InputFormatException("Input incorrect.");
        }


        if(baseline == null)
        {
            throw new InputFormatException("Blank field.");
        }
    }
}
