package ROMdb.Archive;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**
 * Created by Anthony Orio on 4/6/2017.
 *
 * The purpose of this class is to store entries, selected by the user,
 * into a database table. These selected entries are intended to be stored
 * for long term storage.
 */
public abstract class Archive<T>
{

    private ObservableList<T> records;

    /**
     * Default constructor for the archive.
     */
    public Archive()
    {
        records = FXCollections.observableArrayList();
    }

    /**
     * The purpose of this method is to add a single record to the list
     * records.
     * @param object The object to store into the records list.
     */
    public abstract void addRecord(T object) throws SQLException;

    /**
     * The purpose of this method is to store a list of records into the list
     * records. Used when the user wants to pass a list off records.
     * @param list The list of objects to append to the current list.
     */
    public abstract void addListOfRecords(ObservableList<T> list) throws SQLException;


    /**
     * Returns the list of records to the caller.
     * @return The records list.
     */
    public ObservableList<T> getRecords()
    {
        return records;
    }

    /**
     * The override of toString to print all of the objects stored inside
     * of the records list.
     * @return The string representation of the records list.
     */
    @Override public String toString()
    {
        String output = "";
        for (T record : records)
        {
            output += output + "\n";
        }

        return output;
    }
}
