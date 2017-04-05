package ROMdb.Models;

import ROMdb.Driver.Main;
import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Tom on 3/24/2017.
 */
public class AddSCICRModel {

    /**
     * This method saves an SC/ICR to the database
     * @param baseline the current baseline
     * @param type the type, SC or ICR
     * @param number the number of the SC/ICR
     * @param title the title of the SC/ICR
     * @param build the build of the SC/ICR
     * @throws Exception if there is an error with the input caused by error checking, or an SQL exception
     */
    public static void saveSCICR(String baseline, String type, String number, String title, String build) throws Exception {

        errorsExist(baseline, title, number, build);

        // The query to insert the data from the fields.
        String insertQuery =    "INSERT INTO SCICRData ([Number], [Type], [Title], [Build], [Baseline]) VALUES (?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.conn.prepareStatement(insertQuery);

        /* Parse all of the information and stage for writing. */
        st.setString(1, number.trim());
        st.setString(2, type.trim());
        st.setString(3, title.trim());
        st.setString(4, build.trim());
        st.setString(5, baseline.trim());

        // Perform the update inside of the table of the database.
        st.executeUpdate();
    }

    /**
     * Check to see if the number for an SC/ICR is unique
     * @param number the number of the  SC/ICR
     * @param baseline the baseline of the SC/ICR
     * @return whether or not the number for an SC/ICR is unique
     * @throws Exception when the number is not unique or there is an SQL error with the input or connecting to database
     */
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
     * @param baseline The baseline to check.
     * @param title The title to check.
     * @param number The number to check.
     * @param build The build to check.
     * @throws InputFormatException If the input for each parameter is not acceptable.
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
