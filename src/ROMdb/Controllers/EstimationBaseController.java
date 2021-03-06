/**
 * This controller will be used to perform all actions that are
 * on the estimation base menu. It will handle sending and retrieving
 * information to the database about ROM entries.
 */

package ROMdb.Controllers;

import ROMdb.Models.EstimationBaseModel;
import ROMdb.Models.LoginModel;
import ROMdb.Models.MainMenuModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.util.ArrayList;

public class EstimationBaseController {

    @FXML private Button button_estimateSubmit;

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

    /** End GUI components                                          */


    /**
     * This method will automatically run as soon as the program
     * is started. It will establish a database connection and
     * set the baseline drop down menu with the values from the
     * baseline table in the database.
     */
    @FXML
    public void initialize()
    {
        MainMenuModel.estimationBaseController = this;
        LoginModel.estimationBaseController = this;
    }

    /**
     * Enable all of the components inside of the estimation base
     * when the user is an admin.
     */
    public void enableComponents()
    {
        field_staffDay.setDisable(true);
        field_staffMonth.setDisable(false);
        field_cprs.setDisable(false);
        field_integrationWeight.setDisable(false);
        field_unitTestingWeight.setDisable(false);
        field_codeWeight.setDisable(false);
        field_defaultSlocs.setDisable(false);
        field_designWeight.setDisable(false);
        field_cpddDocument.setDisable(false);
        field_cpddDate.setDisable(false);
        field_budgetUpgrade.setDisable(false);
        field_budgetMaint.setDisable(false);
        field_ddrCwtSlocs.setDisable(false);

        button_estimateSubmit.setDisable(false);
    }

    /**
     * Disable all of the components in estimation base
     * when the user is not admin.
     */
    public void disableComponents()
    {
        field_staffDay.setDisable(true);
        field_staffMonth.setDisable(true);
        field_cprs.setDisable(true);
        field_integrationWeight.setDisable(true);
        field_unitTestingWeight.setDisable(true);
        field_codeWeight.setDisable(true);
        field_defaultSlocs.setDisable(true);
        field_designWeight.setDisable(true);
        field_cpddDocument.setDisable(true);
        field_cpddDate.setDisable(true);
        field_budgetUpgrade.setDisable(true);
        field_budgetMaint.setDisable(true);
        field_ddrCwtSlocs.setDisable(true);

        button_estimateSubmit.setDisable(true);
    }


    /**
     * This method will evaluate each text field and ensure that
     * no illegal values are inserted into the fields. If fields
     * are suppose to contain numbers, then they will be compared
     * to a specific number format pattern. If fields are left blank,
     * then the user will be told to enter a value. Finally, the
     * weight fields will be evaluated to ensure they add up to 100.
     */
    @FXML
    public void saveChanges() {
        try {
            EstimationBaseModel.errorChecking(MainMenuModel.getSelectedBaseline(), field_staffDay.getText(), field_staffMonth.getText(), field_integrationWeight.getText(),
                    field_unitTestingWeight.getText(), field_codeWeight.getText(),
                    field_defaultSlocs.getText(), field_designWeight.getText(), field_budgetUpgrade.getText(), field_budgetMaint.getText(),
                    field_ddrCwtSlocs.getText(), field_cpddDocument.getText(), field_cpddDate.getText(), field_cprs.getText());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
            alert.showAndWait();
        }
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
        // Set the field text with the formatted value.
        field_staffDay.setText(EstimationBaseModel.calculateStaffDay(field_staffMonth.getText()));
    }

    /**
     * Filles the text fields in the estimation base with the data from the database.
     */
    public void fillTextFieldsFromDB() {
        try
        {
            String selectedBaseline = MainMenuModel.getSelectedBaseline();
            ArrayList<String> values = EstimationBaseModel.fillTextFieldsFromDB(selectedBaseline);

            field_cprs.setText(values.get(0));
            field_staffDay.setText(values.get(1));
            field_staffMonth.setText(values.get(2));
            field_defaultSlocs.setText(values.get(3));
            field_ddrCwtSlocs.setText(values.get(4));
            field_cpddDocument.setText(values.get(5));
            field_cpddDate.setText(values.get(6));
            field_budgetUpgrade.setText(values.get(7));
            field_budgetMaint.setText(values.get(8));
            field_designWeight.setText(values.get(9));
            field_codeWeight.setText(values.get(10));
            field_integrationWeight.setText(values.get(11));
            field_unitTestingWeight.setText(values.get(12));

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot read database.", ButtonType.OK);
            alert.showAndWait();
        }
    }
}

