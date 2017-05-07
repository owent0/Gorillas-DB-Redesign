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
public class AddSCICRModel
{
    /**
     * This method saves an SC/ICR to the database
     * @param baselineDesc the current baseline
     * @param type the type, SC or ICR
     * @param number the number of the SC/ICR
     * @param title the title of the SC/ICR
     * @param build the build of the SC/ICR
     * @throws Exception if there is an error with the input caused by error checking, or an SQL exception
     */
    public static void saveSCICR(String baselineDesc, String type, String number, String title, String build) throws Exception
    {
        checkIfErrorsExist(baselineDesc, title, number, build);

        // get baseline_id the corresponds to the baseline_desc
        int baselineId = MainMenuModel.getBaselineLookupMap().get(baselineDesc);

        // The query to insert the data from the fields.
        String insertQuery = "INSERT INTO SCICR ([number], [type], [title], [build], [baseline_id]) VALUES (?, ?, ?, ?, ?)";

        // Create a new statement.
        PreparedStatement st = Main.newconn.prepareStatement(insertQuery);

        /* Parse all of the information and stage for writing. */
        st.setString(1, number.trim());
        st.setString(2, type.trim());
        st.setString(3, title.trim());
        st.setString(4, build.trim());
        st.setInt(5, baselineId);

        // Perform the update inside of the table of the database.
        st.executeUpdate();
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
    public static void checkIfErrorsExist(String baseline, String title, String number, String build) throws InputFormatException
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
