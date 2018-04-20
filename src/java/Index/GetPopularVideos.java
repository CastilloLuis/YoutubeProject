/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Index;

import DBManager.PropsManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetPopularVideos {
    
    private static PropsManager myprops = PropsManager.getInstance();
    
    public static String getMostLiked(Connection mycon, int limit) {

        PreparedStatement mySt;
        ResultSet myRs;
        String video_url = null;
        try {
            mySt = mycon.prepareStatement(myprops.getProps("getmostliked")+limit);
            myRs = mySt.executeQuery();
            while (myRs.next()) {
                video_url = myRs.getString("media_url");
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR EL VIDEO CON MÁS LIKES->" + e.getMessage());
            video_url = e.getMessage();
        }

        return video_url;

    }
    
    public static ArrayList<Integer> getPopular(Connection mycon){
        PreparedStatement mySt;
        ResultSet myRs;
        ArrayList <Integer> videos_id = new ArrayList <>();
        try {
            mySt = mycon.prepareStatement(myprops.getProps("getpopular"));
            myRs = mySt.executeQuery();
            while (myRs.next()) {
                videos_id.add(Integer.parseInt(myRs.getString("media_id")));
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL BUSCAR EL VIDEO CON MÁS LIKES->" + e.getMessage());
        }        
        return videos_id;
    }
}
