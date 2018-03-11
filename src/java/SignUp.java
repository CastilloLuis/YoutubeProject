
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import DBManager.passwordEncrypt;
import static java.lang.System.out;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet {

    private static final long serialVersionUID = 1L;
    protected DBConnection con;  //My static var from my dbconnection
    protected static JSONObject myjson;
    private static PropsManager myProps = null; //WE DONT NEED TO INSTANCE IT

    public SignUp() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        myjson = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator()))); //this is what we got from the client
        dbSignUp(con.getConnection(), myjson, res);
    }

    public void dbSignUp(Connection myConnection, JSONObject myjson, HttpServletResponse res) throws IOException {
        //passwordEncrypt myhash = new passwordEncrypt();
        PreparedStatement mySt = null;
        String encryptedpw = null;
        String insertQuery = PropsManager.getProps().getProperty("signupinsert");
        PrintWriter out = res.getWriter();
        
        try {
            encryptedpw = passwordEncrypt.getMD5(myjson.getString("password")); //encrypt the password
            mySt = myConnection.prepareStatement(insertQuery);
            mySt.setString(1, (String) myjson.get("name"));
            mySt.setString(2, (String) myjson.get("lastname"));
            mySt.setString(3, (String) myjson.get("username"));
            mySt.setString(4, encryptedpw);
            mySt.setString(5, (String) myjson.get("email"));

            mySt.executeUpdate(); //use if no data will be returned... else use, executeQuery();
            System.out.println("AGREGADO A LA BBDD CON �XITO");
            
            myjson.put("success", true).put("url", "index.html");
            
        } catch (SQLException | NoSuchAlgorithmException | JSONException e) {
            System.out.println("ERROR AL CONECTAR... -> " + e.getMessage());
        }
        
        out.print(myjson.toString());
    }
}
