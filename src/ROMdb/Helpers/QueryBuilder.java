package ROMdb.Helpers;

import ROMdb.Driver.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Anthony Orio on 4/1/2017.
 * Modified by Derek Gaffney on 4/2/2017.
 *
 * The purpose of this class is to generate SQL statements on the fly instead
 * of manually creating them inside of each model class. It will attempt to
 * prevent code duplication by building SQL queries within the methods.
 */

public class QueryBuilder
{
    /**
     * Versatile method that takes the name of the table you wish to update,
     *      the set you wish to select,
     *      an arraylist of filterCriteria items,
     *      and a boolean for whether or not you want to use the LIKE keyword to use the filters as a pattern matcher or force exact match.
     *
     * The method then constructs the SQL Query from your arguments and returns a PreparedStatement ready to be executed.
     * @param tableName The name of the table to look for inside of the database.
     * @param selectSet The criteria to SELECT from.
     * @param filterCriteria The filter criteria.
     * @param useLike This is used to return values that might have similar inputs. EX: an input of L may return Leroy and Ellie.
     * @return The prepared statement to use during execution of the query.
     * @throws SQLException If the statement could not be completed.
     */
    public static PreparedStatement buildSelectWhereQuery(String tableName, String selectSet, ArrayList<FilterItem> filterCriteria, boolean useLike) throws SQLException
    {
        String query = "";

        // Like attribute.
        String eq_or_like_1 = useLike ? "LIKE " : "= ";
        String eq_or_like_2 = useLike ? "%" : "";

        // Build the main components of a SELECT statement.
        String selectPart = "SELECT " + selectSet + " ";
        String fromPart = "FROM " + tableName + " ";
        String wherePart = "WHERE ";

        boolean firstCriteria = true;

        // Build where part with question marks (?) in place of arguments
        for(FilterItem fi : filterCriteria)
        {
            String andPart = firstCriteria ? "" : "AND ";

            // Check for white space.
            if(!fi.getValue().matches(InputType.WHITE_SPACE.getPattern()))
            {
                wherePart += andPart + "[" + fi.getName() + "] " + eq_or_like_1 + "?";
                firstCriteria = false;
            }

        }

        query = selectPart + fromPart + wherePart;

        PreparedStatement st = Main.conn.prepareStatement(query);

        // replace question marks (?) with actual arguments
        int argNum = 1;
        for(FilterItem fi : filterCriteria)
        {
            if(!fi.getValue().matches(InputType.WHITE_SPACE.getPattern()))
            {
                st.setString(argNum++, ("" + eq_or_like_2 + fi.getValue() + eq_or_like_2) );
            }
        }

        return st;
    }

    /**
     * This method will build a SELECT-FROM-WHERE query by a specific order.
     * @param tableName The table to reference in the database.
     * @param selectSet The SELECT criteria.
     * @param filterCriteria The filtering of the select set.
     * @param useLike To use group of similar results.
     * @param orderBy To order the result set.
     * @param directionOfOrder The direction to order by. asc is scending or desc is descending order.
     * @return The prepared statement to run the query that was built.
     * @throws SQLException If the SQL statement could not be prepared properly.
     */
    public static PreparedStatement buildSelectWhereOrderByQuery(String tableName, String selectSet, ArrayList<FilterItem> filterCriteria, boolean useLike, String orderBy, String directionOfOrder) throws SQLException
    {
        String query = "";

        String eq_or_like_1 = useLike ? "LIKE " : "= ";
        String eq_or_like_2 = useLike ? "%" : "";

        String dir = "";

        /**
         * ensure that dir is a valid keyword
         */
        switch(directionOfOrder.toLowerCase())
        {
            case "asc":
                dir = "asc";
                break;
            case "desc":
                dir = "desc";
                break;
            default:
                dir = "";
                break;
        }

        String selectPart = "SELECT " + selectSet + " ";
        String fromPart = "FROM " + tableName + " ";
        String wherePart = "WHERE ";
        String orderByPart = "ORDER BY " + orderBy + " " + dir;

        boolean firstCriteria = true;

        // Build where part with question marks (?) in place of arguments
        for(FilterItem fi : filterCriteria)
        {
            String andPart = firstCriteria ? "" : "AND ";

            if(!fi.getValue().matches(InputType.WHITE_SPACE.getPattern()))
            {
                wherePart += andPart + "[" + fi.getName() + "] " + eq_or_like_1 + "?";
                firstCriteria = false;
            }

        }

        query = selectPart + fromPart + wherePart + orderByPart;

        PreparedStatement st = Main.conn.prepareStatement(query);

        // replace question marks (?) with actual arguments
        int argNum = 1;
        for(FilterItem fi : filterCriteria)
        {
            if(!fi.getValue().matches(InputType.WHITE_SPACE.getPattern()))
            {
                st.setString(argNum++, ("" + eq_or_like_2 + fi.getValue() + eq_or_like_2) );
            }
        }

        return st;
    }

    /**
     * Use "" for directionOfOrder if you want the default order direction (ASC, DEC)
     *
     * @param tableName The name of the table to reference in the database.
     * @param selectSet The SELECT criteria.
     * @param orderBy The way in which to order the set.
     * @param directionOfOrder The direction of the order. asc is ascending and desc is descending.
     * @return The prepared statement.
     * @throws SQLException If the query could not be completed.
     */
    public static PreparedStatement buildSelectOrderByQuery(String tableName, String selectSet, String orderBy, String directionOfOrder) throws SQLException
    {
        String query = "";
        String dir = "";

        /**
         * ensure that dir is a valid keyword
         */
        switch(directionOfOrder.toLowerCase())
        {
            case "asc":
                dir = "asc";
                break;
            case "desc":
                dir = "desc";
                break;
            default:
                dir = "";
                break;
        }

        String selectPart = "SELECT " + selectSet + " ";
        String fromPart = "FROM " + tableName + " ";
        String orderByPart = "ORDER BY " + orderBy + " " + dir;

        query = selectPart + fromPart + orderByPart;

        PreparedStatement st = Main.conn.prepareStatement(query);

        return st;
    }

    /**
     * Updates a column that contains the text attribute.
     * @param tableName The table to reference inside of the database.
     * @param columnName The name of the column within the specified table.
     * @param textToInsert The text to insert to this column.
     * @param rowID The rowID is the id of the row to update.
     * @return The prepared statement.
     * @throws SQLException If the prepared stated could not be completed correctly.
     */
    public static PreparedStatement updateColumnText(String tableName, String columnName, String textToInsert, int rowID) throws SQLException {
        String query = "";
        query = "UPDATE " + tableName + " SET " + "[" + columnName + "]" + " = ? WHERE [Req_ID] = ?";

        PreparedStatement st = Main.conn.prepareStatement(query);

        st.setString(1, textToInsert);
        st.setInt(2, rowID);

        return st;
    }

    /**
     * Updates a column that contains the double attribute.
     * @param tableName The table to reference inside of the database.
     * @param columnName The name of the column within the specified table.
     * @param valueToInsert The double to insert to this column.
     * @param rowID The rowID is the id of the row to update.
     * @return The prepared statement.
     * @throws SQLException If the prepared stated could not be completed correctly.
     */
    public static PreparedStatement updateColumnDouble(String tableName, String columnName, double valueToInsert, int rowID) throws SQLException
    {
        String query = "";
        query = "UPDATE " + tableName + " SET " + "[" + columnName + "]" + " = ? WHERE [Req_ID] = ?";

        PreparedStatement st = Main.conn.prepareStatement(query);

        st.setDouble(1, valueToInsert);
        st.setInt(2, rowID);

        return st;
    }
}
