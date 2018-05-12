import be.leerstad.Cafe;
import be.leerstad.Consumption;
import be.leerstad.Database.OrdersDAOImpl;
import be.leerstad.Ober;
import be.leerstad.Tafel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CafeTest {

    private Ober ober = new Ober (1,"peters","wout");
    private Ober andereOber = new Ober (2, "Segers","Nathalie");
    private Consumption testConsumptie = new Consumption(50,"test",5d);
    private Consumption correcteConsumptie = new Consumption(1,"Cola",2.4d);
    private Cafe cafe;


    @Before
    public void setUp() {
        cafe = new Cafe("1");
    }

    @After
    public void tearDown(){
        String waar = System.getProperty("user.dir")+"-src-main-resources-serialize-Tafel.6".replace("-", File.separator);
        File file = new File(waar );

        if(file.exists()){
        file.setWritable(true);
        file.delete();}
    }

    @Test
    public void loadPropertiesTest(){
        assertTrue(cafe.loadProps());
    }



    @Test
    public void inlogTest(){
        assertTrue(cafe!=null);
        assertTrue(cafe.getOber()==null);
        assertEquals("please log in.",(cafe.getOberNaam()));
        cafe.inloggen(ober);
        assertFalse(cafe.getOber()==null);
        assertTrue(cafe.isIngelogd());
        assertTrue(cafe.getOber().getID()==1);
        assertEquals("Peters",cafe.getOber().getNaam());
    }

    @Test
    public void inlogtestWithWrongWaiter(){
        Ober magNietInloggen = new Ober(2,"wont","login");
        assertFalse(cafe.isIngelogd());
        cafe.inloggen(magNietInloggen);
        assertFalse(cafe.isIngelogd());
    }

    @Test
    public void fullWaiterName(){
        cafe.inloggen(ober);
        assertTrue(cafe.isIngelogd());
        assertEquals("Wout Peters",cafe.getOberNaam());
        assertNotEquals("wout peters",cafe.getOberNaam());
    }

    @Test
    public void forceIncorrectLogin(){
        assertTrue(cafe!=null);
        assertTrue(cafe.getOber()==null);
        Ober failWaiter = new Ober(500,null,null);
        cafe.inloggen(failWaiter);
        assertFalse(cafe.isIngelogd());
    }

    @Test
    public void uitlogTest(){
        inlogTest();
        assertEquals("Wout",cafe.getOber().getVoornaam());
        cafe.uitloggen();
        assertTrue(cafe.getOber()==null);
        assertFalse(cafe.isIngelogd());

    }

    @Test
    public void tafelSizeTest(){
        assertTrue(cafe.getTafels()!=null);
        assertEquals(6,cafe.getTafels().length);
    }

    @Test
    public void checkTafelNames()
    {
       int teller = 1;
        for (Tafel tafel:cafe.getTafels()) {
            assertEquals(String.valueOf(teller),tafel.getNaam());
            teller++;
        }
    }



    @Test
    public void getTafelNaam()

    {
        assertTrue(cafe!=null);
        assertEquals(cafe.getTafelNaam(),"Not Logged In");
        cafe.inloggen(ober);
        assertEquals(cafe.getTafelNaam(),"Current table: 1");

    }

    @Test
    public void checkBeveragelist(){
        assertTrue(cafe.getBeverageList().isEmpty());
        cafe.inloggen(ober);
        assertFalse(cafe.getBeverageList().isEmpty());
        assertEquals(17,cafe.getBeverageList().size());
    }

    @Test
    public void beveragelistRemainsInMemory()
    {
     checkBeveragelist();
     assertFalse(cafe.getBeverageList().isEmpty());
     cafe.uitloggen();
     assertFalse(cafe.getBeverageList().isEmpty());
    }

    @Test
    public void getTafelWaiterWithoutActiveOrders()
    {
        cafe.inloggen(ober);
        assertTrue(cafe.isIngelogd());
        // wissen actieve orders indien bestaande
        for (Tafel tafel:cafe.getTafels()) {tafel.hasPaid();}
        assertTrue(cafe.getCurrentTafelWaiterId()==ober.getID());
    }

    @Test
    public void getTafelWaiterWithActiveOrder()
    {
        cafe.inloggen(ober);
        assertTrue(cafe.isIngelogd());
        cafe.currentTafel.addConsumption(testConsumptie,ober);
        assertEquals(1,cafe.getCurrentTafelWaiterId());
    }

    @Test
    public void getTafelWaiterWithChangedWaiter(){
        cafe.inloggen(ober);
        assertTrue(cafe.isIngelogd());
        cafe.currentTafel.addConsumption(testConsumptie,ober);
        assertEquals(1,cafe.getCurrentTafelWaiterId());
        assertEquals(1,cafe.getOber().getID());
        cafe.uitloggen();
        assertFalse(cafe.isIngelogd());
        cafe.inloggen(andereOber);
        cafe.currentTafel.addConsumption(testConsumptie,ober);
        assertEquals(2,cafe.getCurrentTafelWaiterId());
        //bestuurselementen toevoegen consumptie niet beschikbaar op javaFX

    }

    @Test
    public void emptyOrderAfterPayment(){
        testConsumptie.aantal=1;
        cafe.inloggen(andereOber);
        assertTrue(cafe.isIngelogd());
        cafe.currentTafel.addConsumption(testConsumptie,andereOber);
        assertEquals(1,cafe.currentTafel.getLijstForPayment().size());
        assertTrue(cafe.currentTafel.hasOrders());
        cafe.currentTafel.hasPaid();
        assertFalse(cafe.currentTafel.hasOrders());
    }

    @Test(expected = IllegalArgumentException.class)
    public void payOrdersTroughCafeWithFailedExeptionThrown() {
        //will throw invalid SQL message  for beverage ID out of range
        cafe.inloggen(andereOber);
        testConsumptie.aantal=1;
        assertTrue(cafe.isIngelogd());
        cafe.currentTafel.addConsumption(testConsumptie,andereOber);
        assertEquals(1,cafe.currentTafel.getLijstForPayment().size());
        cafe.afrekenen();
        assertNotEquals(1,cafe.currentTafel.getLijstForPayment().size());
        assertEquals(0,cafe.currentTafel.getLijstForPayment().size());

    }

    @Test
    public void payOrdersTroughCafe() {
        cafe.inloggen(ober);
        correcteConsumptie.aantal=2;
        assertTrue(cafe.isIngelogd());
        cafe.currentTafel.addConsumption(correcteConsumptie,ober);
        assertEquals(1,cafe.currentTafel.getLijstForPayment().size());
        cafe.afrekenen();
        assertTrue(cafe.currentTafel.getTotalPrice()==0);
    }

    @Test
    public void payOrdersWithWrongWaiter(){
        cafe.inloggen(ober);
        correcteConsumptie.aantal=25;
        assertTrue(cafe.isIngelogd());
        cafe.currentTafel.addConsumption(correcteConsumptie,andereOber);
        assertEquals(1,cafe.currentTafel.getLijstForPayment().size());
        cafe.afrekenen();
        assertFalse(cafe.currentTafel.getTotalPrice()==0);
    }


    @Test
    public void wisselTafel()
    {
        cafe.inloggen(ober);
        assertEquals("1" ,cafe.currentTafel.getNaam());
        cafe.wisselTafel(2);
        assertNotEquals("1" ,cafe.currentTafel.getNaam());
        assertEquals("2" ,cafe.currentTafel.getNaam());
    }

    @Test
    public void wisselTafelGoesInCatchExeption()
    {
        cafe.inloggen(ober);
        assertEquals("1" ,cafe.currentTafel.getNaam());
        cafe.wisselTafel(-20);
        assertEquals("1" ,cafe.currentTafel.getNaam());
        //logfile nalezen op warning, geen throw exeption
        try {
            String waar = System.getProperty("user.dir")+"-src-main-logs-frontend.log".replace("-", File.separator);
            Path logfile = Paths.get(waar);
            String content = new String(Files.readAllBytes(logfile), StandardCharsets.UTF_8);
            int teller = content.length();
            content=content.substring(teller-80,teller);
            assertTrue(content.contains("tafelnummer is niet bestaande"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wegSchrijvenTafel(){
        String waar = System.getProperty("user.dir")+"-src-main-resources-serialize-Tafel.6".replace("-", File.separator);
        File file = new File(waar );

        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cafe.inloggen(ober);
        assertTrue(cafe.isIngelogd());
        assertEquals("1",cafe.currentTafel.getNaam());
        cafe.wisselTafel(6);
        assertEquals("6",cafe.currentTafel.getNaam());
        cafe.currentTafel.addConsumption(correcteConsumptie,ober);
        assertFalse(file.exists());
        cafe.wegSchrijvenTafel();
        assertTrue(file.exists());
        //wissen na test
        file.delete();


    }

    @Test(expected = IllegalAccessError.class)
    public void wegSchrijvenTafelCatchSecion(){
        String waar = System.getProperty("user.dir")+"-src-main-resources-serialize-Tafel.6".replace("-", File.separator);
        File file = new File(waar );
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cafe.inloggen(ober);
        assertTrue(cafe.isIngelogd());
        cafe.wisselTafel(6);
        cafe.currentTafel.addConsumption(correcteConsumptie,ober);
        assertFalse(file.exists());
        cafe.wegSchrijvenTafel();
        assertTrue(file.exists());
        //read only maken
        file.setReadOnly();
        assertFalse(file.canWrite());
        cafe.wegSchrijvenTafel();
    }

    @Test
    public void wegSchrijvenTafelZonderDirectory(){
        String folder = System.getProperty("user.dir")+"-src-main-resources-serialize-".replace("-", File.separator);
        Path location = Paths.get(folder);

        //path wissen indien bestaande
        if (Files.exists(location)) {
            try {
            Files.delete(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertFalse(Files.exists(location));
        }
        cafe.inloggen(ober);
        cafe.wisselTafel(6);
        cafe.currentTafel.addConsumption(correcteConsumptie,ober);
        cafe.wegSchrijvenTafel();
        assertTrue(Files.exists(location));

    }


    @Test(timeout = 5000)
    public void mailTest(){
        String waar = System.getProperty("user.dir")+"-src-main-logs-email.log".replace("-", File.separator);
        File file = new File(waar );
        assertTrue(file.exists());
        cafe.inloggen(ober);
        assertTrue(cafe.isIngelogd());
        assertTrue(cafe.mailFile(file.toString(),"Test Email Send"));
    }

    @Test(expected = NullPointerException.class)
    public void getInstance(){
        //geen instantie getrokken
        assertFalse(Cafe.getInstance().isIngelogd());
    }

    @Test(timeout = 3000, expected = NullPointerException.class)
    public void calenderDateTestForFX(){
        cafe.inloggen(ober);
        assertTrue(cafe.isIngelogd());
        OrdersDAOImpl ordersDAOimpl = new OrdersDAOImpl();
        System.out.println(ordersDAOimpl.dateList());

    }


}
