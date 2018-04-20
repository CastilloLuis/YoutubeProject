package DBManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection myConnection = null; //WE DONT NEED TO INSTANCE IT
    private static PropsManager myprops = PropsManager.getInstance();
    public static Connection getConnection() {
        try {
            //if(myConnection == null){
                Class.forName(myprops.getProps("dbdriver"));
                myConnection = DriverManager.getConnection(myprops.getProps("dbhost"), myprops.getProps("dbuser"), myprops.getProps("dbpassword"));
            //}
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error al crear conexión a la BBDD (DBConnection) -> " + e.getMessage());
        }
        return myConnection;
    }
}
