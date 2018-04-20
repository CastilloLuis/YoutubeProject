import Comments.Comments;
import DBManager.DBConnection;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lenri
 */
@WebServlet(urlPatterns = {"/getComments"})
public class getComments extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Comments.getCurrentComments(DBConnection.getConnection(), req, res);
    }

}
