package ROMdb.Helpers;

import ROMdb.Driver.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Anthony Orio on 4/1/2017.
 */

public class SearchQueries {

    public static ResultSet determineFilters(
                                    String csc,
                                   String csu,
                                   String doorsID,
                                   String paragraph,
                                   String baseline,
                                   String scicr,
                                   String capability,
                                   String ri,
                                   String rommer,
                                   String build) throws SQLException {

        String query = "SELECT * FROM RequirementsData WHERE (";

        int i = 1;

        if (!csc.equals("")) {
            query += "csc = ? AND";
        }
        if (!csu.equals("")) {
            query += " csu = ? AND";
        }
        if (!doorsID.equals("")) {
            query += "[doors_id] = ? AND";
        }
        if (!paragraph.equals("")) {
            query += " paragraph = ? AND";
        }
        if (!baseline.equals("")) {
            query += " [baseline] = ? AND";
        }
        if (!scicr.equals("")) {
            query += " scicr = ? AND";
        }
        if (!capability.equals("")) {
            query += " capability = ? AND";
        }
        if (!ri.equals("")) {
            query += " ri = ? AND";
        }
        if (!rommer.equals("")) {
            query += " rommer = ? AND";
        }
        if (!build.equals("")) {
            query += " build = ?";
        }


        String firstWords = query.substring(0, query.lastIndexOf(" "));
        String lastWord = query.substring(query.lastIndexOf(" ") + 1);

        if (lastWord.equals("AND") || lastWord.equals("OR")) {
            query = firstWords;
        }
        query = query.trim() + ")";
        PreparedStatement st = Main.conn.prepareStatement(query);


        if (!csc.equals("")) {
            st.setString(i++, csc);
        }
        if (!csu.equals("")) {
            st.setString(i++, csu);
        }
        if (!doorsID.equals("")) {
            st.setString(i++, doorsID);
        }
        if (!paragraph.equals("")) {
            st.setString(i++, paragraph);
        }
        if (!baseline.equals("")) {
            st.setString(i++, baseline);
        }
        if (!scicr.equals("")) {
            st.setString(i++, scicr);
        }
        if (!capability.equals("")) {
            st.setString(i++, capability);
        }
        if (!ri.equals("")) {
            st.setString(i++, ri);
        }
        if (!rommer.equals("")) {
            st.setString(i++, rommer);
        }
        if (!build.equals("")) {
            st.setString(i++, build);
        }

        ResultSet rs = st.executeQuery();
        return rs;

    }
}
