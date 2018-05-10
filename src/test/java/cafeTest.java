import be.leerstad.Cafe;
import be.leerstad.Consumption;
import be.leerstad.Ober;
import be.leerstad.Tafel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class cafeTest {

    private Ober ober = new Ober (1,"peters","wout");
    private Ober andereOber = new Ober (2, "Segers","Nathalie");
    private Consumption testConsumptie = new Consumption(50,"test",5d);
    private Consumption correcteConsumptie = new Consumption(1,"Cola",2.4d);
    private Cafe cafe;

    @Before
    public void setUp() {
        cafe = new Cafe("1");
    }

    @Test
    public void loadPropertiesTest(){
        assertTrue(cafe.loadProps());
    }

    @Test
    public void inlogTest(){
        assertTrue(cafe!=null);
        assertTrue(cafe.getOber()==null);
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
        //will throw invalid SQL message  //bev-ID out of range
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
        assertTrue(cafe.currentTafel.getTotalPrice()==00);
    }

    @Test
    public void payOrdersWithWrongWaiter(){
        cafe.inloggen(ober);
        correcteConsumptie.aantal=25;
        assertTrue(cafe.isIngelogd());
        cafe.currentTafel.addConsumption(correcteConsumptie,andereOber);
        assertEquals(1,cafe.currentTafel.getLijstForPayment().size());
        cafe.afrekenen();
        assertFalse(cafe.currentTafel.getTotalPrice()==00);
    }







}
