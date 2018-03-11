
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

@WebFilter("/user/*")
public class SessionFilter implements Filter {

    public SessionFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        HttpServletRequest myReq = (HttpServletRequest) req;
        HttpServletResponse myRes = (HttpServletResponse) res;
        HttpSession mySession = myReq.getSession();// don't create if it doesn't exist
        JSONObject myjson = new JSONObject();

        if (!mySession.isNew()) {
            chain.doFilter(req, res);
            System.out.println("ESTAS LOGGEADO PUEDES ENTRAR");
        } else {
            //myjson.put("success", false).put("url", "index.html").put("message", "You are not allowed to get here");
            mySession.invalidate();
            myRes.sendRedirect("../index.html");
        }
        //out.print(myjson.toString());
    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

}
