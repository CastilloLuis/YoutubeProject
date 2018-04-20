
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


@WebFilter("/user/user-profile.html")
public class SessionFilter implements Filter {

    public SessionFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        HttpServletRequest myReq = (HttpServletRequest) req;
        HttpServletResponse myRes = (HttpServletResponse) res;
        HttpSession mySession = myReq.getSession();// don't create if it doesn't exist

        if (mySession.isNew()) {
            //myjson.put("success", false).put("url", "index.html").put("message", "You are not allowed to get here");
            mySession.invalidate();
            myRes.sendRedirect("../index.html");
        } else {
            //We can not clear the browser cache, but we can make that our pages will not be cached
            myRes.setHeader("Cache-Control", "no-cache"); //Forces caches to obtain a new copy of the page from the origin server
            myRes.setHeader("Cache-Control", "no-store"); //Directs caches not to store the page under any circumstance
            myRes.setDateHeader("Expires", 0); //Causes the proxy cache to see the page as "stale"
            myRes.setHeader("Pragma", "no-cache"); //HTTP 1.0 backward compatibility                
            //System.out.println("ESTAS LOGGEADO, PUEDES ENTRAR...");
        }
        chain.doFilter(req, res);
        //out.print(myjson.toString());
    }

    public void init(FilterConfig fConfig) throws ServletException {

    }

}
