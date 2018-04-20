/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import DBManager.DBConnection;
import User.UserData;
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
@WebServlet(urlPatterns = {"/ProfileImage"})
public class ProfileImage extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GetImage myimage = new GetImage();
        int id = Integer.parseInt(req.getParameter("id"));
        String imageurl = UserData.getProfileImage(DBConnection.getConnection(), id);
        myimage.getImage(imageurl, req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(req, res);
    }

}
