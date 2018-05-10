import be.leerstad.Cafe;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.*;






public class _1_Logtest {



    private static String chezJavaLogFileLocation;
    private static String dbaseFileLogLocation;
    private static String emailLogFileLocation;
    private static String frontendLogFileLocation;


    private static void loadProps(){
        String propertiesName = "log4j.properties";
        Properties props = new Properties ();

        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream (propertiesName)) {

            props.load (inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

        chezJavaLogFileLocation = props.getProperty ("log4j.appender.file.File");
        dbaseFileLogLocation = props.getProperty ("log4j.appender.dbase.File");
        emailLogFileLocation = props.getProperty ("log4j.appender.email.File");
        frontendLogFileLocation = props.getProperty ("log4j.appender.frontend.File");
    }

    private static void wisFile(String fileName)
    {

        File file = new File(fileName);

        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @BeforeClass
    public static void readProperties()
    {
        loadProps();
    }


    @Test
    public void MakeFiles()
    {
        wisFile(chezJavaLogFileLocation);
        wisFile(dbaseFileLogLocation);
        wisFile(emailLogFileLocation);
        wisFile(frontendLogFileLocation);

        assertFalse(Files.exists(Paths.get(chezJavaLogFileLocation)));
        assertFalse(Files.exists(Paths.get(dbaseFileLogLocation)));
        assertFalse(Files.exists(Paths.get(emailLogFileLocation)));
        assertFalse(Files.exists(Paths.get(frontendLogFileLocation)));


        Cafe testOpCreatie = new Cafe("test");
        assertTrue(Files.exists(Paths.get(chezJavaLogFileLocation)));
        assertTrue(Files.exists(Paths.get(dbaseFileLogLocation)));
        assertTrue(Files.exists(Paths.get(emailLogFileLocation)));
        assertTrue(Files.exists(Paths.get(frontendLogFileLocation)));

        assertTrue(testOpCreatie.getTafelNaam().trim().toLowerCase().equals("not logged in"));
    }

}


