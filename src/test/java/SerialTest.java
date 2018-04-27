import be.leerstad.Cafe;
import be.leerstad.Consumption;
import be.leerstad.Ober;
import be.leerstad.helpers.ObjectToSerialize;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class SerialTest {

    private Ober ober = new Ober(1,"Peters","Wout","password");
    private Consumption consumptie1;
    private  Cafe cafe =new Cafe("testCafe");
    private ObjectToSerialize ob = new ObjectToSerialize();
    private String waar = ob.getWaar().toString();


    private void wisser()
    {

        for (int teller=1;teller<10;teller++) {
            try {
                File file = new File(waar + File.separator + "Tafel." + teller);

                if (file.delete()) {
                    System.err.println(file.getName() + " is deleted!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private long fileTeller(){
        try {
            try (Stream<Path> files = Files.list(Paths.get(waar))) {
                return files.count();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 123456789;
    }

     @Before
     public void setUp() {
        wisser();
        consumptie1 = new Consumption(1,"Cola", 2.40, 1 );
        cafe.inloggen(ober);
         cafe.wisselTafel(3);
         cafe.currentTafel.addConsumption(consumptie1,ober);
    }

    @Test
    public void hasOrders()
    {
        assertTrue(cafe.currentTafel.hasOrders());
    }


    @Test
    public void noFiles()
    {
        assertTrue(fileTeller()==0);
    }


    @Test
    public void oneFile()
    {
        cafe.stop();
        assertTrue(fileTeller()==1);
        wisser();
    }

    @Test
    public void treeFiles()
    {
        cafe.wisselTafel(1);
        cafe.currentTafel.addConsumption(consumptie1,ober);
        cafe.wisselTafel(2);
        cafe.currentTafel.addConsumption(consumptie1,ober);
        cafe.stop();
        assertTrue(fileTeller()==3);
    }

    @Test
    public void correctFilename()
    {
        cafe.stop();
        File file = new File(waar + File.separator + "Tafel.3");
        assertTrue(file.exists());
        wisser();
    }



}
