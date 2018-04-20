/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import DBManager.PropsManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author lenri
 */
public class UserData {
    private static PropsManager myprops = PropsManager.getInstance();
    
    public static String getUserData(HttpServletRequest req, HttpServletResponse res, Connection mycon, int id) {
        PreparedStatement mySt;
        String email = null, name = null, lastname = null, username = null, bio = null, views = null, profile_pic = null, bg_pic = null;
        JSONObject json = new JSONObject();
        JSONObject myjson;
        try {
            ResultSet myRs;
            mySt = mycon.prepareStatement(myprops.getProps("getuserdata"));
            mySt.setInt(1, id);
            myRs = mySt.executeQuery();

            while (myRs.next()) {
                email = myRs.getString("email");
                name = myRs.getString("name");
                lastname = myRs.getString("lastname");
                username = myRs.getString("username");
                bio = myRs.getString("user_bio");
                profile_pic = myRs.getString("user_profilepicture");
                bg_pic = myRs.getString("user_bg");
            }

            json.put("email", email).put("name", name).put("lastname", lastname).put("username", username).put("bio", bio).put("profilepicture", profile_pic).put("bgpicture", bg_pic);

            myjson = json;
            System.out.println(myjson);
        } catch (SQLException e) {
            json.put("status", "404").put("msg", "ERROR");
            System.out.println("ERROR AL CONECTAR.... -> 404: " + e.getMessage());
            myjson = json;
        }

        return myjson.toString();
    }

    public static void getVideos(Connection mycon, HttpServletResponse res, int user_id) throws IOException, SQLException {
        PrintWriter out = res.getWriter();
        PreparedStatement myst;
        ResultSetMetaData rsmd = null;
        ResultSet myrs;
        JSONObject json = new JSONObject();
        JSONArray myjsonarray = new JSONArray();
        JSONObject myjson;

        try {
            myst = mycon.prepareStatement(myprops.getProps("getvideos"));
            myst.setInt(1, user_id);
            myrs = myst.executeQuery();
            rsmd = myrs.getMetaData();
            while (myrs.next()) {
                System.out.println("ok");
                myjson = new JSONObject();
                for(int i=1; i<= rsmd.getColumnCount();i++){
                    System.out.println("excelente");
                    myjson.put(rsmd.getColumnLabel(i), myrs.getObject(i));
                    myjson.put("status",200);
                }
                
                myjsonarray.put(myjson);
                System.out.println(myjsonarray);
            }

            out.print(myjsonarray);
        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR... -> " + e.getMessage());
        }
    }
    
    
    /*    public static void getVideos(Connection mycon, HttpServletResponse res, int user_id) throws IOException, SQLException {
        //JSONObject myjson;
        String json;
        PrintWriter out = res.getWriter();
        PreparedStatement myst;
        ResultSet myrs = null;
        ArrayList<String> videos_id = new ArrayList<>();
        ArrayList<String> videos_name = new ArrayList<>();
        ArrayList<String> videos_url = new ArrayList<>();

        try {
            myst = mycon.prepareStatement(myprops.getProps("getvideos"));
            myst.setInt(1, user_id);
            myrs = myst.executeQuery();

            while (myrs.next()) {
                videos_name.add(myrs.getString("media_name"));
                videos_id.add(myrs.getString("media_id"));
                videos_url.add(myrs.getString("media_url"));
            }
            //System.out.println(videos_date);
            json = "{"
                    + "\"success\": true";
            for (int i = 0; i < videos_name.size(); i++) {
                json += ",\"video" + i + "\": ";
                json += "{"
                        + "\"name\":'" + videos_name.get(i) + "',"
                        + "\"id\":" + Integer.parseInt(videos_id.get(i)) + ""
                        + "}";
            }
            json += "}";

            JSONObject myjson = new JSONObject(json);
            out.print(myjson);
        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR... -> " + e.getMessage());
        }
    }*/

    public static String getUsername(Connection mycon, int id) {
        String username_app = null;
        PreparedStatement myst;
        try {
            myst = mycon.prepareStatement(myprops.getProps("getusername"));
            myst.setInt(1, id);
            ResultSet myrs = myst.executeQuery();

            while (myrs.next()) {
                username_app = myrs.getString("username");
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR GETUSERNAME... -> " + e.getMessage());
        }

        return username_app;
    }

    public static String getProfileImage(Connection mycon, int id) {
        String user_profilepicture = null;
        PreparedStatement myst;
        try {
            myst = mycon.prepareStatement(myprops.getProps("getprofilepicture"));
            myst.setInt(1, id);
            ResultSet myrs = myst.executeQuery();

            while (myrs.next()) {
                user_profilepicture = myrs.getString("user_profilepicture");
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR GETUSERNAME... -> " + e.getMessage());
        }

        return user_profilepicture;
    }

}
