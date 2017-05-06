/**
 * This is the main driver that will be used to run the GUI
 * application and is the root of all functionality.
 */

package ROMdb.Driver;

import ROMdb.Helpers.*;
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
    public static Connection newconn = null;
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
     * @throws Exception If scene cannot start.
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        dbPath = dbPath + fileHandler.getFilePath();

        try
        {
            this.conn = DriverManager.getConnection(Main.dbPath);
            this.newconn = DriverManager.getConnection("jdbc:ucanaccess://E:\\derek\\Documents\\IntelliJ IDEA\\Projects\\Gorillas-DB-Redesign\\ROM_DB_V1.mdb");
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ROMdb/Views/MainMenuView.fxml"));
        Parent root = loader.load();


        primaryStage.setTitle("ROM Database");
        primaryStage.setScene(new Scene(root));
        //primaryStage.setResizable(false);
        primaryStage.show();
    }
}
