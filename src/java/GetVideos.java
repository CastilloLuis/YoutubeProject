
import DBManager.DBConnection;
import Index.GetPopularVideos;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
   
@WebServlet(urlPatterns = {"/GetVideos"})
public class GetVideos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;text/html;charset=utf-8");
        JSONObject myjson = new JSONObject();
        PrintWriter out = res.getWriter();
        ArrayList <Integer> videos_id = GetPopularVideos.getPopular(DBConnection.getConnection());
        
        myjson.put("status",200).put("videoid1",videos_id.get(0)).put("videoid2",videos_id.get(1)).put("videoid3",videos_id.get(2));
        
        out.print(myjson.toString());
    } 
}
