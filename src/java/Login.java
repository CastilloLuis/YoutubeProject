
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
import Proxy.SaveAttr;

@WebServlet("/LoginSv")
public class Login extends HttpServlet {

    protected JSONObject myjson;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        myjson = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));

        validLogin(DBConnection.getConnection(), myjson, req, res);

    }

    public void validLogin(Connection myConnection, JSONObject myjson, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("application/json");
        PropsManager myprops = PropsManager.getInstance();
        PrintWriter out = res.getWriter();
        PreparedStatement mySt = null;
        JSONObject myjsonr = new JSONObject();
        passwordEncrypt myhash = new passwordEncrypt();
        String encryptedpw = null;
        String loginQuery = myprops.getProps("loginselect");
        HttpSession mySession;
        try {
            encryptedpw = myhash.getMD5(myjson.getString("password")); //encrypt the password
            mySt = myConnection.prepareStatement(loginQuery);
            mySt.setString(1, (String) myjson.get("username"));
            mySt.setString(2, encryptedpw);
            ResultSet rs = mySt.executeQuery(); //exeuteQuery cause it will return if a row exits or no
            SaveAttr saveAttr = new SaveAttr();
            if (rs.next()) {
                    String user = myjson.getString("username");
                    String id = rs.getString("id_user");                
                if (checkUserType(rs.getInt("type_id"))) {
                    System.out.println("SI ES ADMIN");
                    mySession = req.getSession();
                    // se supone que aqui guardo proxy
                    saveAttr.saveAttr(mySession, "username", user);
                    saveAttr.saveAttr(mySession, "id", id);
                    // se supone que aqui guardo proxy
                    myjsonr.put("success", true).put("isadmin",200).put("username",user).put("id",id).put("url", "user/user-index.html").put("message", "Login successful");                    
                    System.out.println("EL VALUE DE ATTR ES:->"+mySession.getAttribute("username"));
                    System.out.println("EL VALUE DE ATTR ES:->"+mySession.getAttribute("id"));
                } else {
                    System.out.println("NO ES ADMIN");
                    mySession = req.getSession();
                    // se supone que aqui guardo proxy
                    saveAttr.saveAttr(mySession, "username", user);
                    saveAttr.saveAttr(mySession, "id", id);
                    // se supone que aqui guardo proxy
                    System.out.println("EL VALUE DE ATTR ES:->"+mySession.getAttribute("username"));
                    System.out.println("EL VALUE DE ATTR ES:->"+mySession.getAttribute("id"));
                    myjsonr.put("success", true).put("isadmin",404).put("username",user).put("id",id).put("url", "user/user-index.html").put("message", "Login successful");
                }

            } else {
                myjsonr.put("success", false).put("isadmin",404).put("url", "index.html").put("message", "You are not registered");
            }

            out.print(myjsonr.toString());

        } catch (SQLException | NoSuchAlgorithmException | JSONException e) {
            System.out.println("ERROR ... -> " + e.getMessage());
        }
    }

    public boolean checkUserType(int type_id) {
        boolean isAdmin = false;
        isAdmin = ((type_id == 1) ? true : false);
        return isAdmin;
    }

}//END OF CODE
