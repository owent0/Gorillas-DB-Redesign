package ROMdb.Controllers;

import ROMdb.Main;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;
import java.sql.PreparedStatement;


/**
 * Created by Anthony Orio on 3/20/2017.
 */
public class AddBaselineController {

    @FXML private TextField field_addBaseline;
    @FXML private Button button_cancelNewBaseline;
    @FXML private ListView<String> list_baselineList;
    final private Tooltip listViewTooltip = new Tooltip("To edit an item, double click.\nHit 'Enter' when done.");

    public AddBaselineController() {  }

    @FXML
    public void initialize() {
        ObservableList list = MainMenuController.baselines;
        list_baselineList.setItems(list);
        list_baselineList.setTooltip(listViewTooltip);

        list_baselineList.setEditable(true);
        list_baselineList.setCellFactory(TextFieldListCell.forListView());

        list_baselineList.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> t) {
                String oldBaseline = list_baselineList.getSelectionModel().getSelectedItem();
                list_baselineList.getItems().set(t.getIndex(), t.getNewValue());

                String newBaseline = list_baselineList.getSelectionModel().getSelectedItem();

                writeBaselineEditToDB(oldBaseline, newBaseline);
            }
        });
    }

    @FXML
    public void writeBaselineEditToDB(String oldBaseline, String newBaseline) {
        try
        {
            // The query to insert the data from the fields.
            String insertQuery =    "UPDATE baseline SET [baseline]=? WHERE [baseline]=? ";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            st.setString(2, oldBaseline);

            /** Parse all of the information and stage for writing. */
            st.setString(1, newBaseline);


            // Perform the update inside of the table of the database.
            st.executeUpdate();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot read database.\n" + e, ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    public void writeBaseline() throws Exception {

        String baselineToAdd = field_addBaseline.getText().trim();

        if(SCICRController.map.containsKey(baselineToAdd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Baseline already exists", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(baselineToAdd.equals("") || baselineToAdd == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No baseline entered.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        MainMenuController.baselines.add(baselineToAdd);

        try {
            // The query to insert the data from the fields.
            String insertQuery =    "INSERT INTO baseline ([baseline]) VALUES (?)";

            // Create a new statement.
            PreparedStatement st = Main.conn.prepareStatement(insertQuery);

            /** Parse all of the information and stage for writing. */
            st.setString(1, baselineToAdd);

            // Perform the update inside of the table of the database.
            st.executeUpdate();
        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't add baseline to database.", ButtonType.OK);
            alert.showAndWait();
        }

    }

    @FXML
    private void closeScene() {
        Stage stage = (Stage) button_cancelNewBaseline.getScene().getWindow();
        stage.close();
    }
}
