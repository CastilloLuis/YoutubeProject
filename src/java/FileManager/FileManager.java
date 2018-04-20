package FileManager;

import DBManager.PropsManager;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

public class FileManager {
    private static PropsManager myprops = PropsManager.getInstance();
    // METHOD 1: INSERT VIDEO
    public static boolean insertData(Connection con, HttpServletRequest req, HttpServletResponse res, String videotitle, String filename, String url, String description)
            throws ParseException, IOException {
        
        java.util.Date date = new java.util.Date(); //to get the correct date format for postgres
        java.sql.Date finaldate = new java.sql.Date(date.getTime()); //to get the correct date format for postgres
        PreparedStatement mySt = null;
        HttpSession mySession = req.getSession();
        JSONObject myjson = new JSONObject();
        PrintWriter out = res.getWriter();
        boolean ok;
        int id = Integer.parseInt((String) mySession.getAttribute("id")); //getting the id of the user that is logged
        // String signupQuery = PropsManager.getProps().getProperty("signupinsert");
        try {
            mySt = con.prepareStatement(myprops.getProps("insertfilesquery"));
            mySt.setInt(1, id);
            mySt.setString(2, url);
            mySt.setString(3, videotitle);
            mySt.setString(4, filename);
            mySt.setInt(5, 500);
            mySt.setString(6, description);
            mySt.setDate(7, finaldate);

            mySt.executeUpdate(); //use this if no (rs) data will be returned... else use, executeQuery();*/

            ok = true;

        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR... -> " + e.getMessage());
            myjson.put("success", false).put("message", "Error al subir archivo... -> " + e.getMessage());

            ok = false;
        }

        return ok;

    }

    // METHOD 2: REGISTER THE CATEGORIZE OF THE VIDEO
    public static boolean videoTag(Connection mycon, int media_id, int tag_id) throws IOException {
        PreparedStatement mySt;
        boolean isok;
        try {
            mySt = mycon.prepareStatement(myprops.getProps("insertcategory"));
            mySt.setInt(1, media_id);
            mySt.setInt(2, tag_id);
            mySt.executeUpdate();
            System.out.println("todo correcto");
            isok = true;

        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR A LA BASE DE DATOS XD:" + e.getMessage());
            isok = false;
        }
        return isok;
    }

    // METHOD 3: GET VIDEO URL.
    public static String getVideoUrl(Connection mycon, int media_id) throws IOException {

        PreparedStatement mySt;
        ResultSet myRs;
        String video_url = null;

        try {
            mySt = mycon.prepareStatement(myprops.getProps("getvideourl"));
            mySt.setInt(1, media_id);
            myRs = mySt.executeQuery();
            while (myRs.next()) {
                video_url = myRs.getString("media_url");
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR DATOS DEL VIDEO SELECCIONADO->" + e.getMessage());
        }

        return video_url;

    }

    // METHOD 4: GET CURRENT *LIKES* OR *COMMENTS*
    public static int getCurrent(Connection mycon, String table_name, int media_id) {
        PreparedStatement mySt;
        ResultSet myRs;
        int current = 0;
        try {
            mySt = mycon.prepareStatement("SELECT COUNT (*) AS rowcount FROM " + table_name + " WHERE media_id=?");
            mySt.setInt(1, media_id);
            myRs = mySt.executeQuery();
            while (myRs.next()) {
                current = myRs.getInt("rowcount");
            }
            System.out.println(current);
        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR LOS LIKES:asdasd " + e.getMessage());
        }
        return current;
    }
    
    // METHOD 5: SET MEDIA VIEWS
    public static int SetMediaViews(Connection mycon, int id, int currentviews) throws IOException {
        PreparedStatement myst;
        int setviews = currentviews + 1;
        try {
            myst = mycon.prepareStatement(myprops.getProps("setmediaviwes"));
            myst.setInt(1, setviews);
            myst.setInt(2, id);
            myst.executeUpdate();
            System.out.println("THE VIEW WAS UPDATE TO THE TABLE");
        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR (TAGS)... -> " + e.getMessage());
        }
        return setviews;
    }
    
    // METHOD 6: GET CURRENT VIEWS
    public static int getCurrentViews(Connection mycon, int media_id) {
        PreparedStatement mySt;
        ResultSet myRs;
        int currentViews = 0;
        try {
            mySt = mycon.prepareStatement(myprops.getProps("getcurrentviews"));
            mySt.setInt(1, media_id);
            myRs = mySt.executeQuery();
            while (myRs.next()) {
                currentViews = myRs.getInt("media_views");
            }
            System.out.println("curentview" + currentViews);
        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR LOS LIKES:asdasd " + e.getMessage());
        }
        return currentViews;
    }

    // METHOD 7: GET VIDEO ID
    public static int getVideoId(Connection mycon, String media_desc, String media_title) throws IOException {

        PreparedStatement mySt;
        ResultSet myRs;
        int media_id = 0;

        try {
            mySt = mycon.prepareStatement(myprops.getProps("getvideoid"));
            mySt.setString(1, media_desc);
            mySt.setString(2, media_title);
            myRs = mySt.executeQuery();
            while (myRs.next()) {
                media_id = myRs.getInt("media_id");
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR DATOS DEL VIDEO SELECCIONADO->" + e.getMessage());
        }

        return media_id;

    }
    
    // METHOD 8: DELETE SHIT
    public static String DeleteVideo(Connection mycon, HttpServletRequest req, HttpServletResponse res) throws IOException{
        int id = Integer.parseInt(req.getParameter("id"));
        String tbreq = req.getParameter("tn");
        PreparedStatement myst;
        String query = null;
        JSONObject myjson = new JSONObject();     
        //System.out.println(tablename+"  "+id);
        if(("mt").equals(tbreq)){
            DeleteDataVideo(mycon,"likes",id);
            DeleteDataVideo(mycon,"comments",id);            
            DeleteDataVideo(mycon,"tag_media",id);            
            query="DELETE FROM media_thumbnail WHERE media_id=?";
        }
        if(("cm").equals(tbreq)){
            query="DELETE FROM comments WHERE comment_id=?";          
        }
        try{
            myst = mycon.prepareStatement(query);
            myst.setInt(1, id);
            myst.executeUpdate();
            
            myjson.put("status", 200).put("msg","The item was successfully removed!");
            
        }catch(SQLException e){
            System.out.println("OCURRIÓ UN ERROR AL ELIMINAR EL COMENTARIO O VIDEO... ->" +e.getMessage());
            myjson.put("status",404).put("msg", "The video or comment was not removed. Try it later...");
        }
        
        return myjson.toString();
    }
    
    
    public static void DeleteDataVideo(Connection mycon, String tablename, int media_id){
        PreparedStatement myst;
        try{
            myst = mycon.prepareStatement("DELETE FROM "+tablename+" WHERE media_id=?");
            myst.setInt(1, media_id);
            myst.executeUpdate();
        }catch(SQLException e){
            System.out.println("OCURRIÓ UN ERROR AL ELIMINAR LA DATA DEL VIDEO... ->" +e.getMessage());
        }
    }    
    
    public static String GetCategory(Connection mycon, int media_id){
        PreparedStatement myst;
        ResultSet myrs;
        String category = null;
        //String q = "SELECT t.tag_des AS description FROM media_thumbnail AS m INNER JOIN tag_media as tm ON tm.media_id=? INNER JOIN tags AS t ON tm.tag_id=?";
        String q = "SELECT t.tag_des FROM media_thumbnail AS m INNER JOIN tag_media as tm USING (media_id) INNER JOIN tags AS t USING (tag_id) WHERE m.media_id = ?";
        try{
            myst = mycon.prepareStatement(q);
            //myst = mycon.prepareStatement("SELECT tag_des FROM tag_media tm JOIN media_thumbnail mt ON WHERE media_id=?");
            myst.setInt(1, 64);
            myrs = myst.executeQuery();
            while(myrs.next()){
                 category = myrs.getString("tag_des");
                 //category = myrs.getNString(0);
                 System.out.println("LA CATEGORIA ESSS: "+category);
            }
        }catch(SQLException e){
            System.out.println("Ocurrio un erroR al buscar categoria del video->" +e.getMessage());
        }
        
        return category;
    }
    
}
