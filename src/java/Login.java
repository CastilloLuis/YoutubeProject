
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import DBManager.DBConnection;
import DBManager.PropsManager;
import DBManager.passwordEncrypt;

@WebServlet("/LoginSv")
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;
    protected DBConnection con;
    protected JSONObject myjson;
    private static PropsManager myProps = null; //WE DONT NEED TO INSTANCE IT

    public Login() {
        super();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        myjson = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));

        validLogin(con.getConnection(), myjson, req, res);

    }

    public void validLogin(Connection myConnection, JSONObject myjson, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        PreparedStatement mySt = null;
        JSONObject myjsonr = new JSONObject();
        passwordEncrypt myhash = new passwordEncrypt();
        String encryptedpw = null;
        String selectQuery = myProps.getProps().getProperty("loginselect");

        try {
            encryptedpw = myhash.getMD5(myjson.getString("password")); //encrypt the password
            mySt = myConnection.prepareStatement("SELECT * FROM app_user WHERE username=? AND password=?");
            mySt.setString(1, (String) myjson.get("username"));
            mySt.setString(2, encryptedpw);
            ResultSet rs = mySt.executeQuery(); //exeuteQuery cause it will return if a row exits or no
            if (rs.next()) {
                if (checkUserType(myConnection, rs.getString("email"), rs.getInt("type_id"))) {
                    System.out.println("SI ES ADMIN");
                } else {
                    //System.out.println("NO ES ADMIN");
                    //the register exists, we store the values
                    String user = myjson.getString("username");
                    String pw = myjson.getString("password");
                    //we create the SESSION with the username founded
                    HttpSession mySession = req.getSession();
                    //mySession.setAttribute("username", user); //we store the value of the user that has successfully login
                    myjsonr.put("success", true).put("url", "user/page1.html").put("message", "Login successful");
                }

            } else {
                myjsonr.put("success", false).put("url", "index.html").put("message", "You are not registered");
            }

            out.print(myjsonr.toString());

        } catch (SQLException | NoSuchAlgorithmException | JSONException e) {
            System.out.println("ERROR ... -> " + e.getMessage());
        }
    }

    public boolean checkUserType(Connection myConnection, String email, int type_id) {
        PreparedStatement mySt = null;
        boolean isAdmin = false;
        isAdmin = ((type_id == 1) ? true : false);
        return isAdmin;
    }

}//END OF CODE
