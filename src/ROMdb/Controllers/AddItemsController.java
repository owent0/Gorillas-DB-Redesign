package ROMdb.Controllers;

import ROMdb.Models.AddItemsModel;
import ROMdb.Models.RequirementsModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * This method will execute each time the window loads.
     * Will fill combo boxes, fill hash map, and fill the
     * observable lists. fillHashMap will create a connection
     * to the database.
     */
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

    /**
     * This method will fill the combo boxes with the val types.
     */
    private void fillComboBox()
    {
        /* Grab the list of values from the model and place into observable list. */
        ObservableList vals = FXCollections.observableList(
                        new ArrayList<>(Arrays.asList(AddItemsModel.getValTypes()))
        );

        /* Set the combo box with these items as a sorted list. */
        combo_itemType.setItems(new SortedList(vals));
    }

    /**
     * Each val type will have it's own observable list. These items are
     * retrieved from the models hash map.
     */
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

    /**
     * Each val type selected will have a list of current items
     * within that val type. This method will switch the list view
     * items with the currently selected val type.
     */
    @FXML
    public void switchListItems()
    {
        /* Figure out which val type is selected in the combo box and set list view. */
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
    public void finish()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "For your changes to take effect please restart the program", ButtonType.OK);
        alert.showAndWait();
        System.exit(1);
    }


    /**
     * Adds a new item to the currently selected val type in the combo box.
     * The new item will be placed into the hash map in the model, as well
     * as the observable list in this class so that it can update in real
     * time.
     */
    @FXML
    public void addNewItem()
    {
        /* Retrieve the new item name from the field. */
        String newItem = field_newItem.getText();
        String valType = "";


        try {
            /* We must lower case the combo box item so that we can use it as a key to the map. */
            valType = combo_itemType.getSelectionModel().getSelectedItem().toLowerCase();

            int order = AddItemsModel.getMap().get(valType).size() + 1;
            AddItemsModel.writeItemToDb(valType, newItem, order);
            field_newItem.clear();

            // Add to appropriate observable list.
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
            /* Add the item to the hash map. */
            AddItemsModel.getMap().get(valType).add(newItem);

        }
        catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Could not insert " + newItem + " into " + valType, ButtonType.OK);
            alert.showAndWait();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "The item: " + newItem + " already exists!", ButtonType.OK);
            alert.showAndWait();
        }

    }
}
