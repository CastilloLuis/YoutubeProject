/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lenri
 */
@WebServlet(urlPatterns = {"/GetImage"})
public class GetImage extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String myurl = req.getParameter("url");
        getImage(myurl,req,res);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(req, res);
    }

    public void getImage(String myurl,HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("image/png;image/jpg");
        File f = new File(myurl);
        BufferedImage bi = ImageIO.read(f);
        OutputStream out = res.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        out.close();
    }
               

}
