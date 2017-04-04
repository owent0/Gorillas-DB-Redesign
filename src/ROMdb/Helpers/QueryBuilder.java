package ROMdb.Helpers;

import ROMdb.Driver.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Anthony Orio on 4/1/2017.
 * Modified by Derek Gaffney on 4/2/2017.
 */

public class QueryBuilder
{
    /*
     * Versatile method that takes the name of the table you wish to update,
     *      the set you wish to select,
     *      an arraylist of filterCriteria items,
     *      and a boolean for whether or not you want to use the LIKE keyword to use the filters as a pattern matcher or force exact match.
     *
     * The method then constructs the SQL Query from your arguments and returns a PreparedStatement ready to be executed.
     */
    public static PreparedStatement buildSelectWhereQuery(String tableName, String selectSet, ArrayList<FilterItem> filterCriteria, boolean useLike) throws SQLException
    {
        String query = "";

        String eq_or_like_1 = useLike ? "LIKE " : "= ";
        String eq_or_like_2 = useLike ? "%" : "";

        String selectPart = "SELECT " + selectSet + " ";
        String fromPart = "FROM " + tableName + " ";
        String wherePart = "WHERE ";

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
     * @param tableName
     * @param selectSet
     * @param orderBy
     * @param directionOfOrder
     * @return
     * @throws SQLException
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

    public static PreparedStatement updateColumnText(String tableName, String columnName, String textToInsert, int rowID) throws SQLException {
        String query = "";
        query = "UPDATE " + tableName + " SET " + "[" + columnName + "]" + " = ? WHERE [Req_ID] = ?";

        PreparedStatement st = Main.conn.prepareStatement(query);

        st.setString(1, textToInsert);
        st.setInt(2, rowID);

        return st;
    }

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
