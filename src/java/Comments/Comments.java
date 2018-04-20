package Comments;

import DBManager.DBConnection;
import DBManager.PropsManager;
import User.UserData;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

public class Comments {

    private static PropsManager myprops = PropsManager.getInstance();

//METHOD 1: REGISTER COMMENT
    public static void createComment(Connection mycon, JSONObject myjson, HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession mySession = req.getSession();
        JSONObject myjsonr = new JSONObject();
        PrintWriter out = res.getWriter();
        PreparedStatement mySt;
        java.util.Date date = new java.util.Date(); //to get the correct date format for postgres
        java.sql.Date finaldate = new java.sql.Date(date.getTime()); //to get the correct date format for postgres
        int user_id = Integer.parseInt((String) mySession.getAttribute("id"));
        int media_id = Integer.parseInt(myjson.getString("id"));
        String comment = myjson.getString("commentfield");
        try {
            mySt = mycon.prepareStatement(myprops.getProps("createcomment"));
            mySt.setInt(1, media_id);
            mySt.setInt(2, user_id);
            mySt.setDate(3, finaldate);
            mySt.setString(4, comment);
            mySt.executeUpdate();

            myjsonr.put("success", true).put("username", mySession.getAttribute("username")).put("comment", comment).put("date", finaldate).put("message", "The comment was successfully submitted");
            System.out.println(myjsonr);

        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR A LA BASE DE DATOS" + e.getMessage());
            myjsonr.put("success", false).put("message", "The comment was not submitted");
        }

        out.print(myjsonr.toString());
    }

    //METHOD 2: GET CURRENT COMMENTS
    public static void getCurrentComments(Connection mycon, HttpServletRequest req, HttpServletResponse res) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        PrintWriter out = res.getWriter();
        HttpSession mySession = req.getSession();
        JSONObject json = new JSONObject();
        JSONArray myjsonarray = new JSONArray();
        JSONObject myjson;
        PreparedStatement myst;
        ResultSetMetaData rsmd = null;
        ResultSet myrs;

        try {
            myst = mycon.prepareStatement(myprops.getProps("getcurrentcomments"));
            myst.setInt(1, id);
            myrs = myst.executeQuery();
            rsmd = myrs.getMetaData();
            while (myrs.next()) {
                myjson = new JSONObject();
                String username;
                for(int i=1; i<= rsmd.getColumnCount();i++){
                    myjson.put(rsmd.getColumnLabel(i), myrs.getObject(i));
                    username = UserData.getUsername(DBConnection.getConnection(), Integer.parseInt(myrs.getString("id_user")));
                    myjson.put("username", username);
                }
                
                myjsonarray.put(myjson);
                System.out.println(myjsonarray);
            }

            out.print(myjsonarray);

        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR LOS COMENTARIOS: " + e.getMessage());
        }
    }
}
