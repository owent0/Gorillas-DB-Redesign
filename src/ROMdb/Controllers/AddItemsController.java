package ROMdb.Controllers;

import ROMdb.Models.AddItemsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

    @FXML private ComboBox<String> combo_itemType;

    @FXML private TextField field_newItem;

    @FXML private ListView<?> list_values;

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
            System.out.println("failed");
        }
    }

    private void fillComboBox()
    {
        ObservableList vals = FXCollections.observableList(
                        new ArrayList<>(Arrays.asList(AddItemsModel.getValTypes()))
        );

        combo_itemType.setItems(new SortedList(vals));
    }


    @FXML
    public void switchListItems()
    {
        ObservableList<String> list;

        String selectedVal = combo_itemType.getSelectionModel().getSelectedItem();
        switch (selectedVal)
        {
            case "Capability":
                list = FXCollections.observableArrayList(AddItemsModel.getMap().get("capability"));
                break;
            case "CSC":
                list = FXCollections.observableArrayList(AddItemsModel.getMap().get("csc"));
                break;
            case "CSU":
                list = FXCollections.observableArrayList(AddItemsModel.getMap().get("csu"));
                break;
            case "Program":
                list = FXCollections.observableArrayList(AddItemsModel.getMap().get("program"));
                break;
            case "RI":
                list = FXCollections.observableArrayList(AddItemsModel.getMap().get("ri"));
                break;
            case "Rommer":
                list = FXCollections.observableArrayList(AddItemsModel.getMap().get("rommer"));
                break;
            case "Build":
                list = FXCollections.observableArrayList(AddItemsModel.getMap().get("build"));
                break;
            default:
                list = FXCollections.observableArrayList(new ArrayList<String>());
                break;

        }
        list_values.setItems(new SortedList(list));
    }

}
