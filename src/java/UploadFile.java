
import DBManager.DBConnection;
import DBManager.PropsManager;
import FileManager.FileManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONObject;

@MultipartConfig
@WebServlet("/UploadFile")
public class UploadFile extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PropsManager myprops = PropsManager.getInstance();
        Part file = req.getPart("file");
        String title = req.getParameter("title");
        String des = req.getParameter("description");
        int tagid = Integer.parseInt(req.getParameter("tagid"));
        InputStream filecontent = file.getInputStream();
        OutputStream os = null;
        JSONObject myjson = new JSONObject();
        PrintWriter out = res.getWriter();
        Connection mycon = DBConnection.getConnection();
        if (allowedFiles(this.getFileName(file))) {
            try {
                String baseDir = myprops.getProps("uploadedfiledir");
                os = new FileOutputStream(baseDir + "/" + this.getFileName(file));
                String fullurl = baseDir + "/" + this.getFileName(file);
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    os.write(bytes, 0, read);
                }
                if(FileManager.insertData(mycon, req, res, title, this.getFileName(file), fullurl, des)){
                  myjson.put("success", true).put("message", "File uploaded successfully");
                  int videoid = FileManager.getVideoId(mycon, des, title);
                  FileManager.videoTag(mycon, videoid, tagid);
                }
            } catch (IOException | ParseException e) {
            } finally {
                if (filecontent != null) {
                    filecontent.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        } else {
            //NOT ALLOWED FILE
            myjson.put("success", false).put("message", "This file is not allowed");
        }
        out.print(myjson.toString());
    }

    // This function allows us to obtain the file name
    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private boolean allowedFiles(String myfile) {
        String[] allowedfiles = {".mp4", ".avi"};
        boolean isallow;
        return isallow = ((myfile.contains(allowedfiles[0])) || (myfile.contains(allowedfiles[1])));
    }
 
}
