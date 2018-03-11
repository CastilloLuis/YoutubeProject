package DBManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    static String host;
    static String user;
    static String pw;
    //DB
    private static Connection myConnection = null; //WE DONT NEED TO INSTANCE IT
    private static PropsManager myProps = null; //WE DONT NEED TO INSTANCE IT

    public static Connection getConnection() {
        try {
            Class.forName(myProps.getProps().getProperty("dbdriver"));
            myConnection = DriverManager.getConnection(myProps.getProps().getProperty("dbhost"), myProps.getProps().getProperty("dbuser"), myProps.getProps().getProperty("dbpassword"));
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("mesaje" + e.getMessage());
        }
        return myConnection;
    }

}
