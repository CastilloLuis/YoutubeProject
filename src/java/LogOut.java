
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

@WebServlet("/LogOut")
public class LogOut extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        JSONObject myjson = new JSONObject();
        HttpSession mySession = req.getSession();
        mySession.invalidate();
        System.out.println("HAS CERRADO SESION");
        res.sendRedirect("index.html");
        //myjson.put("success", false).put("url", "index.html").put("message", "You've logged out");
        //out.write(myjson.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
