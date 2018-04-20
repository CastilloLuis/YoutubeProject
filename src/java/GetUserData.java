
import DBManager.DBConnection;
import User.UserData;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lenri
 */
@WebServlet(urlPatterns = {"/GetUserData"})
public class GetUserData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        int user_id = Integer.parseInt((String) req.getParameter("id"));
        System.out.println(user_id);
        String myjson = UserData.getUserData(req, res, DBConnection.getConnection(), user_id);
        // data = UserData.getUserStats(req, res, DBConnection.getConnection(), user_id);
        out.print(myjson);
        
    }
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    }

}
