
import DBManager.DBConnection;
import FileManager.FileManager;
import Likes.LikeClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

@WebServlet(urlPatterns = {"/LikeServlet"})
public class LikeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        int media_id = Integer.parseInt(req.getParameter("id"));
        PrintWriter out = res.getWriter();
        JSONObject myjson = new JSONObject();
        HttpSession mySession = req.getSession();
        Connection mycon = DBConnection.getConnection();
        
        int user_id = Integer.parseInt((String) mySession.getAttribute("id"));
        int currentlikes = (FileManager.getCurrent(mycon, "LIKES", media_id) + 1);

        if (!LikeClass.exitsInLikes(DBConnection.getConnection(), user_id, media_id)) {
            if (LikeClass.registerLike(mycon, req, res, media_id)) {
                myjson.put("success", true).put("likes", currentlikes).put("message", "The like was successfully made!");
            }
        } else {
            myjson.put("success", false).put("likes", currentlikes).put("message", "The like was not inserted, try later...").put("likexists", false);
            LikeClass.dislike(mycon, user_id, media_id); //delete the like's register
        }
        out.print(myjson.toString());
    }
    
}