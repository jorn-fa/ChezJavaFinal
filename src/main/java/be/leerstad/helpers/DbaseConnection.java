package be.leerstad.helpers;

import com.mysql.jdbc.Driver;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public final class DbaseConnection {

    private static Logger logger = Logger.getLogger("dbase");
    private final static String fileName = "dbase.properties";
    private final static String directory = System.getProperty("user.dir") + "/src/main/resources/".replace("/", File.separator);

    private static String Propfile = directory + fileName;

    private static String url;
    private static String username;
    private static String password;
    private static DbaseConnection instance = null;

    private DbaseConnection() {
        // Exists only to defeat instantiation.
    }

    //Singleton pattern
    public static DbaseConnection getInstance(){
        if (instance == null) {
            instance = new DbaseConnection();
        }
        return instance;
    }

    private static void inlezen() {

        try {
            Properties properties = PropReader.ReadProperties(Propfile);
            url = properties.getProperty("url");
            username = properties.getProperty("user");
            password = properties.getProperty("password");
            logger.debug("Property file read - ok ");
        } catch (Exception e) {
            logger.error("Verify Property file: " + fileName);
        }
    }


    public static Connection getConnection()

    {
        inlezen();

        try {
            DriverManager.registerDriver(new Driver());
            return DriverManager.getConnection(url, username, password);

        } catch (SQLException ex) {

            logger.error("error connection to database: " );
            throw new RuntimeException("Error connecting to the database", ex);
        }

    }

    public static boolean isAlive() {

        inlezen();

        try {
            getConnection();
            return true;
        } catch (Exception e) {
            logger.debug("Database is not running, check connection");
        }

        return false;
    }
}

