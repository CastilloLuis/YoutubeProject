package Likes;

import DBManager.PropsManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LikeClass {
    
    private static PropsManager myprops = PropsManager.getInstance();
    
    //METHOD 1: REGISTER LIKE.
    public static boolean registerLike(Connection mycon, HttpServletRequest req, HttpServletResponse res, int media_id) throws IOException {
        HttpSession mySession = req.getSession();
        PreparedStatement mySt;
        int id_user = Integer.parseInt((String) mySession.getAttribute("id"));
        boolean isok;
        try {
            mySt = mycon.prepareStatement(myprops.getProps("registerlike"));
            mySt.setInt(1, id_user);
            mySt.setInt(2, media_id);
            mySt.executeUpdate();

            isok = true;

        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR A LA BASE DE DATOS XD:" + e.getMessage());
            isok = false;
        }
        return isok;
    }

    //METHOD 2: DISLIKE    
    public static void dislike(Connection mycon, int user_id, int media_id) {
        PreparedStatement mySt;
        try {
            mySt = mycon.prepareStatement(myprops.getProps("dislike"));
            mySt.setInt(1, user_id);
            mySt.setInt(2, media_id);
            mySt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERROR AL CONECTAR A LA BASE DE DATOS XD:" + e.getMessage());
        }
    }

    //METHOD 3: CHECK IF THE USER THAT LIKED THE VIDEO, LIKED IT BEFORE...
    public static boolean exitsInLikes(Connection mycon, int id_user, int media_id) {
        PreparedStatement myst;
        boolean userexitsinlikes = false;
        try {
            myst = mycon.prepareStatement(myprops.getProps("checklikes"));
            myst.setInt(1, id_user);
            myst.setInt(2, media_id);

            ResultSet myrs = myst.executeQuery();

            userexitsinlikes = ((myrs.next()) ? true : false);
            System.out.println("----->" + userexitsinlikes);
        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR IF THE USER'S LIKES ALREADY EXITS... -> " + e.getMessage());
        }
        return userexitsinlikes;
    }

}
