
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
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String myjson = req.getParameter("key");
        System.out.println(myjson);
        searchVideo(DBConnection.getConnection(), myjson, res);        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

    }

    public void searchVideo(Connection mycon, String jsonres, HttpServletResponse res) throws IOException {
        PropsManager myprops = PropsManager.getInstance();
        JSONObject json = new JSONObject();
        JSONArray myjsonarray = new JSONArray();
        JSONObject myjson;        
        PrintWriter out = res.getWriter();
        PreparedStatement myst;
        ResultSet myrs;
                ResultSetMetaData rsmd = null;
        try {
            myst = mycon.prepareStatement(myprops.getProps("searchquery"));
            myrs = myst.executeQuery();

            myrs = myst.executeQuery();
            rsmd = myrs.getMetaData();
            
            while (myrs.next()) {
                myjson = new JSONObject();
                for(int i=1; i<= rsmd.getColumnCount();i++){
                    myjson.put(rsmd.getColumnLabel(i), myrs.getObject(i));
                }
                myjsonarray.put(myjson);
                System.out.println(myjsonarray);
            }
            
            out.print(myjsonarray);
            
        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR CON LIKE... -> " + e.getMessage());
        }
    }
    
    /*public void searchVideo(Connection mycon, String jsonres, HttpServletResponse res) throws IOException {
        PropsManager myprops = PropsManager.getInstance();
        JSONObject myjson;
        String json;
        PrintWriter out = res.getWriter();
        ArrayList<String> videoname = new ArrayList<>();
        ArrayList<Integer> videoid = new ArrayList<>();
        PreparedStatement myst;
        ResultSet myrs;
        try {
            myst = mycon.prepareStatement(myprops.getProps("searchquery"));
            myrs = myst.executeQuery();

            while (myrs.next()) {
                videoname.add(myrs.getString("media_name"));
                videoid.add(Integer.parseInt(myrs.getString("media_id")));
            }

            json = "{"
                    + "\"status\": 200";
            for (int i = 0; i < videoname.size(); i++) {
                json += ",\"video" + i + "\": ";
                json += "{"
                        + "\"videoid\":" + videoid.get(i) +","
                        + "\"videoname\":'" + videoname.get(i)
                        + "' }";
            }
            json += "}";

            myjson = new JSONObject(json);
            //System.out.println("json->"+myjson);
            out.print(myjson);
        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR CON LIKE... -> " + e.getMessage());
        }
    }*/
    
    
}
