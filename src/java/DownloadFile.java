
import DBManager.DBConnection;
import DBManager.PropsManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DownloadFile")
public class DownloadFile extends HttpServlet {
    private PropsManager myprops = PropsManager.getInstance();
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        int file_id = Integer.parseInt(req.getParameter("id"));
        String file_name = getFile(DBConnection.getConnection(), file_id);
        File my_file = new File(myprops.getProps("downloadedfiledir") + file_name);
        res.setContentType("file");
        res.setHeader("Content-disposition", "attachment; filename=" + file_name + "");
        OutputStream out = res.getOutputStream();
        FileInputStream in = new FileInputStream(my_file);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.flush();

    }

    protected String getFile(Connection mycon, int id) {
        String myfile = null;
        try {
            PreparedStatement myst = mycon.prepareStatement(myprops.getProps("selectfilequery"));
            myst.setInt(1, id);
            ResultSet myrs = myst.executeQuery();
            myfile = null;

            while (myrs.next()) {
                myfile = myrs.getString("media_filename");
            }
        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR... -> " + e.getMessage());
        }
        System.out.println(myfile);
        return myfile;
    }

} //end of the code
