/**
 * This is the main driver that will be used to run the GUI
 * application and is the root of all functionality.
 */

package ROMdb;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.File;


public class Main extends Application
{
    public static String dbPath = "jdbc:ucanaccess://";
    /**
     * Main gateway.
     * @param args : command line arguments.
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Starts the FXML GUI created in scene builder.
     * @param primaryStage : the current GUI stage.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Create a file chooser object to select database.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database Resource File");

        // Filters out all other file types.
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Database Files", "*.mdb"));

        // Return the file and then extract its path.
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            dbPath += selectedFile.getPath();
        }


        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/MainView.fxml"));
        Parent root = loader.load();


        primaryStage.setTitle("ROM Database");
        primaryStage.setScene(new Scene(root, 890, 555));
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
