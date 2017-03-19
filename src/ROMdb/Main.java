/**
 * This is the main driver that will be used to run the GUI
 * application and is the root of all functionality.
 */

package ROMdb;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;


public class Main extends Application
{
    public static Connection conn = null;
    public static String dbPath = "jdbc:ucanaccess://";
    public static FileHandler fileHandler = new FileHandler();
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
        dbPath = dbPath + fileHandler.getFilePath();

        try {
            this.conn = DriverManager.getConnection(Main.dbPath);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/MainView.fxml"));
        Parent root = loader.load();


        primaryStage.setTitle("ROM Database");
        primaryStage.setScene(new Scene(root, 890, 555));
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
