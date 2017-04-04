package ROMdb.Helpers.Database;

import ROMdb.Exceptions.UnknownDatabaseTypeException;
import ROMdb.Helpers.Config;
import ROMdb.Helpers.Database.DatabaseType;

import java.sql.PreparedStatement;

/**
 * Class which will eventually be used to abstract any database specific code
 * into generic database functions that can be performed without checking anything
 * except the type of database indicated in the config file.
 * Most likely it will be used to instantiate connections to databases
 *
 * Note:    This class is barely implemented at the moment.
 *          Also the static fields located in the Config class are serving as the temporary config file
 *
 * Created by Derek Gaffney on 4/2/2017.
 */
public class DatabaseUtilities
{

    public static void initializeConnection() throws Exception
    {
        if(Config.dbType == DatabaseType.MS_ACCESS)
        {
            System.out.println("Initalize connection with MS Access settings");
        }
        else
        {
            throw new UnknownDatabaseTypeException();
        }
    }
}
