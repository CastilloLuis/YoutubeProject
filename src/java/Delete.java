/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import DBManager.DBConnection;
import FileManager.FileManager;
import static FileManager.FileManager.getVideoUrl;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lenri
 */
@WebServlet(urlPatterns = {"/Delete"})
public class Delete extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String path = getVideoUrl(DBConnection.getConnection(), id);
        System.out.println(path);
        try{
            FileManager.DeleteVideo(DBConnection.getConnection(), req, res);
            File filemedia = new File(path);
        if (filemedia.exists()) {
            System.out.println("existe");
            filemedia.delete();
        } else {
            System.out.println("no exise el file shit media bru");
        }
        }catch(IOException e){
            System.out.println("ERROR EN DELETE.java -> "+e.getMessage());
        }finally{
            res.sendRedirect("user/user-index.html");
        }
        
        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

    }

}
