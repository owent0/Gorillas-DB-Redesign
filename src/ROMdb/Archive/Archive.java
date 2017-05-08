//package ROMdb.Archive;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.sql.SQLException;
//
///**
// * Created by Anthony Orio on 4/6/2017.
// *
// * The purpose of this class is to store entries, selected by the user,
// * into a database table. These selected entries are intended to be stored
// * for long term storage.
// */
//public abstract class Archive<T>
//{
//
//    private ObservableList<T> records;
//
//    /**
//     * Default constructor for the archive.
//     */
//    public Archive()
//    {
//        records = FXCollections.observableArrayList();
//    }
//
//    /**
//     * The purpose of this method is to add a single record to the list
//     * records.
//     * @param object The object to store into the records list.
//     * @throws SQLException If the SQL query cannot be completed properly.
//     */
//    public abstract void addRecord(T object) throws SQLException;
//
//    /**
//     * The purpose of this method is to store a list of records into the list
//     * records. Used when the user wants to pass a list off records.
//     * @param list The list of objects to append to the current list.
//     * @throws SQLException If the SQL query fails.
//     */
//    public abstract void addListOfRecords(ObservableList<T> list) throws SQLException;
//
//    /**
//     * The purpose of this method is to take a list of elements from the archive
//     * and either delete or place them back into the original place they came from.
//     * @param list The list to bring back from the archive.
//     * @throws SQLException If the SQL query fails.
//     */
//    public abstract void removeListOfRecords(ObservableList<T> list) throws SQLException;
//
//    /**
//     * Fills a table view with the data read from the database table.
//     * @throws SQLException If the SQL query fails.
//     */
//    public abstract void fillRows() throws  SQLException;
//
//
//    /**
//     * Returns the list of records to the caller.
//     * @return The records list.
//     */
//    public ObservableList<T> getRecords()
//    {
//        return records;
//    }
//
//    /**
//     * The override of toString to print all of the objects stored inside
//     * of the records list.
//     * @return The string representation of the records list.
//     */
//    @Override public String toString()
//    {
//        String output = "";
//        for (T record : records)
//        {
//            output += output + "\n";
//        }
//
//        return output;
//    }
//}
