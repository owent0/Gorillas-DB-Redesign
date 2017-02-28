package ROMdb;
/**
 * @author Team Gorillas
 * 2-13-17
 */


import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import ROMdb.Controllers.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/MainView.fxml"));

        //Controller controller = loader.<Controller>getController();
        //controller.setLoader(loader);

        Parent root = loader.load();
        primaryStage.setTitle("ROM Database");
        primaryStage.setScene(new Scene(root, 890, 555));
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
