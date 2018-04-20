
import DBManager.DBConnection;
import FileManager.FileManager;
import Index.GetPopularVideos;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/Streaming"})
public class Streaming extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        boolean myjson = Boolean.parseBoolean(req.getParameter("mostliked"));
        System.out.println(myjson);
        String videourl = null;
        ServletOutputStream out = res.getOutputStream();

        if (myjson) {
            System.out.println("entro en true");
            videourl = GetPopularVideos.getMostLiked(DBConnection.getConnection(), 1);
        } else {
            System.out.println("entro en false");
            int myid = Integer.parseInt(req.getParameter("id"));
            System.out.println(myid);
            videourl = FileManager.getVideoUrl(DBConnection.getConnection(), myid);
            System.out.println("THIS FVCKING URL" + videourl);
        }

        InputStream in = new FileInputStream(videourl);
        String mimeType = "video/mp4";

        byte[] bytes = new byte[1024];
        int bytesRead;

        res.setContentType(mimeType);

        while ((bytesRead = in.read(bytes)) != -1) {
            out.write(bytes, 0, bytesRead);
        }

        in.close();
        out.close();

    }


}
