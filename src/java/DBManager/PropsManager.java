package DBManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropsManager {

    private static PropsManager instancia = null;
    private Properties p;

    public PropsManager() {
        p = new Properties();
        try {
            p.load(new FileInputStream(new File("C:\\Users\\lenri\\Documents\\JavaProjectsNB\\YoutubeApp\\src\\java\\props\\database.properties")));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error al cargar archivo de propiedades... -> "+e.getMessage());
        }
    }

    public static PropsManager getInstance() {
        return instancia = ((instancia == null) ? instancia = new PropsManager() : instancia);
    }

    public String getProps(String prop) {
        return p.getProperty(prop);
    }
}

