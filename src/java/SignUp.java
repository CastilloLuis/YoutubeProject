
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import DBManager.DBConnection;
import DBManager.PropsManager;
import DBManager.passwordEncrypt;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

@WebServlet("/SignUp")
@MultipartConfig
public class SignUp extends HttpServlet {
    protected PropsManager myprops = PropsManager.getInstance();
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json");
        //myjson = new JSONObject(req.getReader().lines().collect(Collectors.joining(System.lineSeparator()))); //this is what we got from the client
        dbSignUp(DBConnection.getConnection(), req, res);
    }

    public void dbSignUp(Connection myConnection, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PropsManager myprops = PropsManager.getInstance();
        Part file = req.getPart("file");
        String name = req.getParameter("name");
        String lastname = req.getParameter("lastname");
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String bio = req.getParameter("bio");
        JSONObject myjson = new JSONObject();
        PreparedStatement mySt = null;
        String encryptedpw = null;
        String signupQuery = myprops.getProps("signupinsert");
        PrintWriter out = res.getWriter();

        try {  
            encryptedpw = passwordEncrypt.getMD5(password); //encrypt the password
            mySt = myConnection.prepareStatement(signupQuery);
            mySt.setString(1, name);
            mySt.setString(2, lastname);
            mySt.setString(3, username);
            mySt.setString(4, encryptedpw);
            mySt.setString(5, email);
            mySt.setString(6, bio);
            mySt.setString(7,uploadProfileImage(file));
            mySt.setString(8,uploadProfileImage(file));
            mySt.executeUpdate(); //use if no data will be returned... else use, executeQuery();
            System.out.println("AGREGADO A LA BBDD CON ÉXITO");

            myjson.put("status", 200).put("url", "index.html");

        } catch (SQLException | NoSuchAlgorithmException | JSONException e) {
            System.out.println("ERROR AL CONECTAR... -> " + e.getMessage());
            myjson.put("status", 404).put("url", "index.html");
        }

        out.print(myjson.toString());
    }

    protected String uploadProfileImage(Part file) throws IOException, ServletException {

        InputStream filecontent = file.getInputStream();
        OutputStream os = null;
        String fullurl = null;
        
        if (allowedFiles(this.getFileName(file))) {
            try {
                String baseDir = myprops.getProps("uploadedfilemig");
                os = new FileOutputStream(baseDir + "/" + this.getFileName(file));
                fullurl = baseDir + "/" + this.getFileName(file);
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    os.write(bytes, 0, read);
                }  
            } catch (IOException e) {
            } finally {
                if (filecontent != null) {
                    filecontent.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        } else {
            //NOT ALLOWED FILE
            return fullurl;
        }
        
        return fullurl;
    }

    // This function allows us to obtain the file name
    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private boolean allowedFiles(String myfile) {
        String[] allowedfiles = {".png", ".jpg"};
        boolean isallow;
        isallow = ((myfile.contains(allowedfiles[0])) || (myfile.contains(allowedfiles[1])));
        return isallow;
    }
}
