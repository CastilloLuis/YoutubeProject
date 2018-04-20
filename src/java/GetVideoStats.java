
import DBManager.DBConnection;
import FileManager.FileManager;
import Likes.LikeClass;
import User.UserData;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

@WebServlet(urlPatterns = {"/GetVideoStats"})
public class GetVideoStats extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;text/html;charset=utf-8");
        try {
            getVideos(DBConnection.getConnection(), req, res);
        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR... " + e.getMessage());
        }
    }

    protected void getVideos(Connection mycon, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
        boolean myjson = Boolean.parseBoolean(req.getParameter("mostliked"));
        boolean isvisitor = Boolean.parseBoolean(req.getParameter("isvisitor"));
        HttpSession mySession = req.getSession();
        JSONObject myjsonr = new JSONObject();
        PrintWriter out = res.getWriter();
        PreparedStatement myst;
        ResultSet myrs = null;
        String myquery = null;
        String media_name = null;
        String media_description = null;
        String media_date = null;
        String media_userid = null;
        String media_tag = null;
        int media_likes = 0;
        int media_id = 0;
        int media_comments = 0;
        int media_views = 0;
        String app_username = null;
        System.out.println("value json stats" + myjson);
        if (myjson) {
            //myquery = "SELECT media_name FROM media_thumbnail ORDER BY media_likes DESC limit 1";
        } else {
            media_id = Integer.parseInt(req.getParameter("id"));
            myquery = "SELECT * FROM media_thumbnail WHERE media_id =" + media_id;
        }

        try {
            myst = mycon.prepareStatement(myquery);
            myrs = myst.executeQuery();
            while (myrs.next()) {
                media_name = myrs.getString("media_name");
                media_description = myrs.getString("media_des");
                media_date = myrs.getString("created_at");
                media_userid = myrs.getString("media_userid");
            }
            app_username = UserData.getUsername(mycon, Integer.parseInt(media_userid));
            media_likes = FileManager.getCurrent(mycon, "likes", media_id);
            media_comments = FileManager.getCurrent(mycon, "comments", media_id);
            media_views = FileManager.SetMediaViews(mycon, media_id, FileManager.getCurrentViews(DBConnection.getConnection(), media_id))-1;
            media_tag = FileManager.GetCategory(mycon, media_id);
        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR EL VIDEO CON MÁS LIKES->" + e.getMessage());
        }

        if (isvisitor) {
            myjsonr.put("mediaviews", media_views).put("userid", media_userid).put("name", media_name).put("likes", media_likes).put("comments", media_comments).put("description", media_description).put("date", media_date).put("username", app_username);
        } else {
            /*verify if user exits in likes to put the icon red*/
            if (LikeClass.exitsInLikes(mycon, Integer.parseInt((String) mySession.getAttribute("id")), media_id)) {
                myjsonr.put("mediaviews", media_views).put("userid", media_userid).put("name", media_name).put("likes", media_likes).put("comments", media_comments).put("description", media_description).put("date", media_date).put("username", app_username).put("likexists", true);
            } else {
                myjsonr.put("mediaviews", media_views).put("userid", media_userid).put("name", media_name).put("likes", media_likes).put("comments", media_comments).put("description", media_description).put("date", media_date).put("username", app_username).put("likexists", false);
            }
        }
        out.print(myjsonr.toString());

    }

}
