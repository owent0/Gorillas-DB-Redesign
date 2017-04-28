package ROMdb.Controllers;

import ROMdb.Models.AddItemsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

/**
 * The controller that will produce the handshake between the
 * AddItemsView and AddItemsModel.
 *
 * Created by Anthony Orio on 4/25/2017.
 */
public class AddItemsController
{
    private ObservableList currListItems = FXCollections.observableArrayList();
    private ObservableList<String> capabilityList = FXCollections.observableArrayList();
    private ObservableList<String> cscList = FXCollections.observableArrayList();
    private ObservableList<String> csuList = FXCollections.observableArrayList();
    private ObservableList<String> programList = FXCollections.observableArrayList();
    private ObservableList<String> riList = FXCollections.observableArrayList();
    private ObservableList<String> rommerList = FXCollections.observableArrayList();
    private ObservableList<String> buildList = FXCollections.observableArrayList();

    @FXML private ComboBox<String> combo_itemType;

    @FXML private TextField field_newItem;

    @FXML private ListView<String> list_values;

    @FXML private Button button_saveVal;
    @FXML private Button button_up;
    @FXML private Button button_down;
    @FXML private Button button_delete;

    @FXML
    public void initialize()
    {
        this.fillComboBox();

        try {
            AddItemsModel.fillHashMap();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load database.", ButtonType.OK);
            alert.showAndWait();
        }

        this.fillObservableLists();
    }

    private void fillComboBox()
    {
        ObservableList vals = FXCollections.observableList(
                        new ArrayList<>(Arrays.asList(AddItemsModel.getValTypes()))
        );

        combo_itemType.setItems(new SortedList(vals));
    }

    private void fillObservableLists()
    {
        capabilityList = FXCollections.observableArrayList(AddItemsModel.getMap().get("capability"));
        cscList = FXCollections.observableArrayList(AddItemsModel.getMap().get("csc"));
        csuList = FXCollections.observableArrayList(AddItemsModel.getMap().get("csu"));
        programList = FXCollections.observableArrayList(AddItemsModel.getMap().get("program"));
        riList = FXCollections.observableArrayList(AddItemsModel.getMap().get("ri"));
        rommerList = FXCollections.observableArrayList(AddItemsModel.getMap().get("rommer"));
        buildList = FXCollections.observableArrayList(AddItemsModel.getMap().get("build"));
    }

    @FXML
    public void switchListItems()
    {
        ObservableList<String> list;

        String selectedVal = combo_itemType.getSelectionModel().getSelectedItem();
        switch (selectedVal)
        {
            case "Capability":  list_values.setItems(capabilityList);break;
            case "CSC":         list_values.setItems(cscList);       break;
            case "CSU":         list_values.setItems(csuList);       break;
            case "Program":     list_values.setItems(programList);   break;
            case "RI":          list_values.setItems(riList);        break;
            case "Rommer":      list_values.setItems(rommerList);    break;
            case "Build":       list_values.setItems(buildList);     break;
            default:                                                 break;
        }
    }

    @FXML
    public void addNewItem()
    {
        String newItem = field_newItem.getText();
        String valType = combo_itemType.getSelectionModel().getSelectedItem().toLowerCase();

        AddItemsModel.getMap().get(valType).add(newItem);

        switch (valType)
        {
            case "capability":  capabilityList.add(newItem);break;
            case "csc":         cscList.add(newItem);       break;
            case "csu":         csuList.add(newItem);       break;
            case "program":     programList.add(newItem);   break;
            case "ri":          riList.add(newItem);        break;
            case "rommer":      rommerList.add(newItem);    break;
            case "build":       buildList.add(newItem);     break;
            default:                                        break;
        }
    }
}
