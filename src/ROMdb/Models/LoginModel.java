package ROMdb.Models;

import ROMdb.Controllers.EstimationBaseController;
import ROMdb.Controllers.LoginController;
import ROMdb.Controllers.MainMenuController;
import ROMdb.Driver.Main;
import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;
import ROMdb.Helpers.InputValidator;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.*;

/**
 * Created by chrisrmckane on 4/11/17.
 */
public class LoginModel {

    public static MainMenuController mainMenuController;
    public static EstimationBaseController estimationBaseController;
    public static boolean isAdmin = false;
    public static boolean isLoggedIn = false;
    public static boolean passMatches = false;
    private static StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    /**
     * Writes and saves the new admin password to the database. Takes the plain text password and
     * converts it to a one way hash for security
     *
     * @param newAdminPassword the new admin password plain text
     */
    public static void writeAdminPasswordToDB (String newAdminPassword) {

        String encryptedPassword = passwordEncryptor.encryptPassword(newAdminPassword);

        try {

            // The query to insert the data from the fields.
            String insertQuery = "UPDATE DBUsers SET [Code]=? WHERE [ID]= 1";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            // Parse all of the information and stage for writing.
            st.setString(1, encryptedPassword);

            // Perform the update inside of the table of the database.
            st.executeUpdate();

        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "There was an error with the SQL code: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();

        }
    }

    /**
     * Gets the stored admin password
     *
     * @return the one-way hashed admin password that is stored in the database
     * @throws SQLException the SQL code was not able to pull the password from the table correctly
     */
    public static String pullAdminPasswordFromDB() throws SQLException {

        // Create query to grab all rows.
        String query = "SELECT Code FROM DBUsers WHERE UserName = 'Admin'";

        // Create the statement to send.
        Statement st = Main.conn.createStatement();

        // Return the result set from this query.
        ResultSet rs = st.executeQuery(query);

        String pass = "";

        // Pulls the code column from the database table.
        // Since there is only one there is pulls the stored password.
        while(rs.next()){
            pass = rs.getString("Code");
        }

        // Returns the one-way hashed password stored in the database table.
        return pass;
    }

    /**
     * Changes the current admin password to a new one.
     *
     * Checks to see if the user has the correct current password and the new password
     * was entered correctly twice. If so the new password is written to the database. If not
     * the user is informed that one of the passwords was wrong or did not match and to try again.
     *
     * @param oldPassword the current admin password to make sure the user is allowed to change the password
     * @param newAdminPassword the desired new admin password plain text
     * @param confirmNewPassword the new admin password in plain text again to make sure the user is setting the
     *                           correct password
     */
    public static void changeAdminPassword(String oldPassword, String newAdminPassword, String confirmNewPassword) {

        try {

            InputValidator.checkPasswordPatternMatch(newAdminPassword, InputType.COMPLEX_PASSWORD);

            if(checkInputPassword(oldPassword) && newAdminPassword.equals(confirmNewPassword)) {

                passMatches = true;
                writeAdminPasswordToDB(newAdminPassword);

            } else {

                passMatches = false;

                Alert alert = new Alert(Alert.AlertType.ERROR, "Old or New Passwords do not match!", ButtonType.OK);
                alert.showAndWait();

            }

        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "There was an error with the SQL code: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();

        } catch (InputFormatException e) {

            passMatches = false;

            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Checks to see if the currently input password matches the stored password
     *
     * @param inputPassword the plain text password being checked against the stored password
     * @return true if the input password matches the stored password, false if they do no match
     * @throws SQLException if the SQL code messes up pulling the stored admin password
     */
    public static boolean checkInputPassword(String inputPassword) throws SQLException {
        return passwordEncryptor.checkPassword(inputPassword, pullAdminPasswordFromDB());
    }

    /**
     * Writes and saves the initial admin password to the database. Takes the plain text password and
     * converts it to a one way hash for security. Only needs to be used IF there is ever an error with the
     * DBUsers DB table, where the password needs to be deleted.
     */
    public static void writeInitAdminPasswordToDB () {

        String encryptedPassword = passwordEncryptor.encryptPassword("");

        try {

            // The query to insert the data from the fields.
            String insertQuery = "UPDATE DBUsers SET [Code]=? WHERE [ID]= 1";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            // Parse all of the information and stage for writing.
            st.setString(1, encryptedPassword);

            // Perform the update inside of the table of the database.
            st.executeUpdate();

        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "There was an error with the SQL code: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();

        }
    }
}
