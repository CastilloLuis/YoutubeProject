
import Comments.Comments;
import DBManager.DBConnection;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet(urlPatterns = {"/CommentServlet"})
public class CommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        JSONObject myjson = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
        Comments.createComment(DBConnection.getConnection(), myjson, req, res);
    }


}
