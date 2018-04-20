
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import DBManager.DBConnection;
import DBManager.PropsManager;
import javax.servlet.annotation.MultipartConfig;

@WebServlet("/UpdateUserData")
@MultipartConfig
public class UpdateUserData extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        res.setContentType("application/json");
        JSONObject myjson = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator()))); //this is what we got from the client
        updateUser(DBConnection.getConnection(), req, res, myjson);
        
    }

    public void updateUser(Connection mycon, HttpServletRequest req, HttpServletResponse res, JSONObject myjson) throws IOException, ServletException {
        PropsManager myprops = PropsManager.getInstance();
        PreparedStatement mySt = null;
        String updateQuery = myprops.getProps("updateuser");
        PrintWriter out = res.getWriter();

        try {  
            mySt = mycon.prepareStatement(updateQuery);
            mySt.setString(1, myjson.getString("bio"));
            mySt.setInt(2, myjson.getInt("id"));
            
            mySt.executeUpdate(); //use if no data will be returned... else use, executeQuery();
            System.out.println("AGREGADO A LA BBDD CON ÉXITO");

            myjson.put("status", 200).put("url", "user-profile.html?id="+myjson.getInt("id"));

        } catch (SQLException | JSONException e) {
            System.out.println("ERROR AL CONECTAR... -> " + e.getMessage());
            myjson.put("success", false).put("url", "index.html");
        }

        out.print(myjson.toString());
    }
    
}