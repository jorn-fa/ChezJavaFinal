import be.leerstad.Cafe;
import be.leerstad.Consumption;
import be.leerstad.Ober;
import be.leerstad.Tafel;
import be.leerstad.helpers.DeSerializer;
import be.leerstad.helpers.ObjectToSerialize;
import org.jfree.io.FileUtilities;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;



import static org.junit.Assert.*;



public class DeserialTest {

    private Ober ober = new Ober(1,"Peters","Wout","password");
    private Consumption consumptie1;
    private  Cafe cafe =new Cafe("testCafe");
    private static SerialTest serialTest = new SerialTest();
    private DeSerializer  ds = new DeSerializer();
    private Tafel tafel1;
    private Tafel tafel2;
    private Tafel tafel3;
    private Tafel tafel4;
    private Tafel tafel5;
    private Tafel tafel6;
    private Tafel[] testTafels;

        @Before
    public void setUp() {
            tafel1 = new Tafel("1");
            tafel2 = new Tafel("2");
            tafel3 = new Tafel("3");
            tafel4 = new Tafel("4");
            tafel5 = new Tafel("5");
            tafel6 = new Tafel("6");
            testTafels = new Tafel[]{tafel1,tafel2,tafel3,tafel4,tafel5,tafel6};

        serialTest.wisser();
        consumptie1 = new Consumption(1,"Cola", 2.40, 2 );
        cafe.inloggen(ober);
        cafe.wisselTafel(3);
        cafe.currentTafel.addConsumption(consumptie1,ober);
        cafe.wisselTafel(2);
        cafe.currentTafel.addConsumption(consumptie1,ober);
        cafe.stop();
    }

    @AfterClass
    public static void shutDown(){
        serialTest.wisser();
    }

    @Test
    public void hasFiles(){
        assertTrue(serialTest.fileTeller()==2);
    }

    @Test
    public void hasTafels(){
        assertTrue(testTafels.length==6);
    }


    @Test
    public void hasOrders(){
        for (Tafel tafel: testTafels
             ) {assertFalse(tafel.hasOrders());
        }
    }


    @Test
    public void hasOrdersAfterDeserial()
    {
        testTafels=ds.giveTafel(testTafels);
        assertFalse(testTafels[0].hasOrders());
        assertTrue(testTafels[1].hasOrders());
        assertTrue(testTafels[2].hasOrders());
        assertFalse(testTafels[3].hasOrders());
        assertFalse(testTafels[4].hasOrders());
        assertFalse(testTafels[5].hasOrders());
    }

    @Test
    public void checkConsumtionAfterDeserial()
    {
        hasOrdersAfterDeserial(); // uitvoeren om correcte lijst te verkrijgen
        assertTrue(testTafels[1].getLijstForPayment().indexOf(consumptie1)==0);
        assertTrue((testTafels[1].getLijstForPayment().get(0).getNaam().matches(consumptie1.getNaam())));
    }

    @Test(expected = NullPointerException.class)
    public void tryReadTafelWithoutDirectory(){
        String naam="tableWithaRandomName";
        Tafel verkeerdeNaam = new Tafel(naam);
        Tafel tafels[] = new Tafel[]{verkeerdeNaam};
        ObjectToSerialize ob = new ObjectToSerialize();
        ob.Serialize(verkeerdeNaam);

        String waar = System.getProperty("user.dir")+"-src-main-resources-serialize-Tafel."+naam+"";
        waar=waar.replace("-", File.separator);

        File file = new File(waar );
        assertTrue(file.exists());
        //null forceren = folder leegmaken en wissen -> onmogelijk om file in te lezen

        String folder = System.getProperty("user.dir")+"-src-main-resources-serialize-".replace("-", File.separator);
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File folderDirectory = new File(folder);
        if(folderDirectory.isDirectory()){
            File[] files = folderDirectory.listFiles();
            for (File wisFile:files) {
                wisFile.delete();
            }
        }
        try {
            folderDirectory.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tafels=null;
        assertFalse(folderDirectory.exists());
        ds.giveTafel(tafels);
        assertTrue(folderDirectory.exists());
        assertNotEquals(null,tafels[0].getNaam());
    }


}
