import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class DbasePropertiesTest {

    File bestand = new File(System.getProperty("user.dir") + "*src*main*resources*dbase.properties".replace("*", File.separator));
    Properties eigenschappen = new Properties();


    @Test
    public void existant() {
        assertTrue(bestand.exists());
    }

    @Test
    public void notEmpty() {
        assertTrue(bestand.length() > 0);
    }

    @Test
    public void hasProperties() {

        try {
            FileInputStream in = new FileInputStream(bestand);
            eigenschappen.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(eigenschappen.keySet().size() > 0);
    }

    @Test
    public void verifyAmount() {
        if (eigenschappen.keySet().size() == 0) {
            hasProperties();
        }
        assertTrue(eigenschappen.keySet().size() == 3);
    }

    @Test
    public void minimumProperties()
    {
        if (eigenschappen.keySet().size() == 0) {
            hasProperties();
        }
        assertTrue(eigenschappen.keySet().contains("user"));
        assertTrue(eigenschappen.keySet().contains("password"));
    }

}
