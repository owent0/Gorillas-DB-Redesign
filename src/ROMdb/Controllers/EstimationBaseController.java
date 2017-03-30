/**
 * This controller will be used to perform all actions that are
 * on the estimation base menu. It will handle sending and retrieving
 * information to the database about ROM entries.
 */

package ROMdb.Controllers;

import ROMdb.Models.EstimationBaseModel;
import ROMdb.Models.MainMenuModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.util.ArrayList;

public class EstimationBaseController {

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
        //combo_estimateBaseline.setItems(MainMenuModel.getBaselines());
        MainMenuModel.estimationBaseController = this;
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


    public void fillTextFieldsFromDB() {
        try
        {
            ArrayList<String> values = EstimationBaseModel.fillTextFieldsFromDB(MainMenuModel.getSelectedBaseline());

            field_staffDay.setText(values.get(0));
            field_staffMonth.setText(values.get(1));
            field_cprs.setText(values.get(2));
            field_defaultSlocs.setText(values.get(3));
            field_cpddDocument.setText(values.get(4));
            field_cpddDate.setText(values.get(5));
            field_budgetUpgrade.setText(values.get(6));
            field_budgetMaint.setText(values.get(7));
            field_ddrCwtSlocs.setText(values.get(8));
            field_integrationWeight.setText(values.get(9));
            field_unitTestingWeight.setText(values.get(10));
            field_codeWeight.setText(values.get(11));
            field_designWeight.setText(values.get(12));

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot read database.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * This method will take the information from the database table
     * "basicrom" and display it inside the correct text field when
     * the user chooses the baseline from the drop down menu. This information
     * can then be updated if desired.
     */
    /*@FXML
    private void fillTextFieldsFromDB()
    {

        try
        {
            ArrayList<String> values = EstimationBaseModel.fillTextFieldsFromDB(combo_estimateBaseline.getSelectionModel().getSelectedItem());

            field_staffDay.setText(values.get(0));
            field_staffMonth.setText(values.get(1));
            field_cprs.setText(values.get(2));
            field_defaultSlocs.setText(values.get(3));
            field_cpddDocument.setText(values.get(4));
            field_cpddDate.setText(values.get(5));
            field_budgetUpgrade.setText(values.get(6));
            field_budgetMaint.setText(values.get(7));
            field_ddrCwtSlocs.setText(values.get(8));
            field_integrationWeight.setText(values.get(9));
            field_unitTestingWeight.setText(values.get(10));
            field_codeWeight.setText(values.get(11));
            field_designWeight.setText(values.get(12));

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot read database.", ButtonType.OK);
            alert.showAndWait();
        }
    }*/
}

