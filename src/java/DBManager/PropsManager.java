package DBManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsManager {

    public static Properties getProps() {
        Properties myProps = new Properties();
        InputStream myFile = null;
        try {
            myFile = new FileInputStream("C:\\Users\\lenri\\Documents\\JavaProjectsNB\\YoutubeApp\\src\\java\\props\\database.properties");
            myProps.load(myFile);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return myProps;
    }
}
