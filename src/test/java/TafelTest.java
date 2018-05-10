import be.leerstad.Consumption;
import be.leerstad.Ober;
import be.leerstad.Tafel;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.*;

import static org.junit.Assert.*;

public class TafelTest {

private Tafel tafel1, tafel2;

    @Before
    public void setup()
    {
        tafel1 = new Tafel("Tafel Een");
        tafel2 = new Tafel("Tafel Twee");
    }

    @After
    public void tearDown()
    {
        tafel1 = tafel2 = null;
    }


    @Test
    public void testEquals()
    {
        assertFalse(tafel1.equals(null));
        assertFalse(tafel2.equals(null));
        assertTrue(tafel1.equals(tafel1));
        assertFalse(tafel1.equals((tafel2)));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Tafel.class)
                .withPrefabValues(Tafel.class, tafel1, tafel2)
                .withPrefabValues(Consumption.class, new Consumption(1,"cola", 2.4D,1),new Consumption(2,"cola", 2.4D,1))
                .withNonnullFields("naam")
                .withIgnoredFields("hasOrders","order")
                .verify();
    }

    @Test
    public void hash()
    {
        assertEquals(-925039250,tafel1.hashCode());
        assertEquals(1389018307,tafel2.hashCode());
    }

    @Test
    public void toStringTest()
    {
        assertEquals("Tafel{tafel een}",tafel1.toString());
        assertEquals("Tafel{tafel twee}",tafel2.toString());
    }

    @Test
    public void compareTo()
    {
        assertTrue(tafel1.compareTo(tafel2)<0);
    }

    @Test
    public void getNaam()
    {
        assertEquals("tafel een",tafel1.getNaam().toLowerCase());
        assertNotEquals("tafel twee",tafel2.getNaam());
    }

    @Test
    public void hasOrders()
    {
        Ober ober=new Ober(1,"test","persoon");
        assertFalse(tafel1.hasOrders());
        Consumption consumption = new Consumption(1,"iets",1.0D,1);
        tafel1.addConsumption(consumption,ober);
        assertTrue(tafel1.hasOrders());
    }


    @Test
    public void checkTotaal()
    {
        hasOrders();
        assertTrue(tafel1.hasOrders());
        assertTrue(tafel1.getTotalPrice()==1d);
    }

    @Test
    public void checkTotaalMultiple()
    {
        hasOrders();
        Ober ober=new Ober(1,"test","persoon");
        assertTrue(tafel1.hasOrders());
        Consumption consumption = new Consumption(1,"anders",5.0D,1);
        tafel1.addConsumption(consumption,ober);
        assertTrue(tafel1.getTotalPrice()==6d);
    }

    @Test
    public void checkTotaalWithRemovedOrder()
    {
        hasOrders();
        Ober ober=new Ober(1,"test","persoon");
        Consumption consumption = new Consumption(1,"iets",1.0D,-1);
        tafel1.addConsumption(consumption,ober);
        assertTrue(tafel1.getTotalPrice()==0d);
    }

    @Test
    public void checkOberId(){
        hasOrders();
        assertTrue(tafel1.getOberId()==1);
    }

    @Test
    public void checkOberIDAfterRemovingOrders(){
        hasOrders();
        Ober ober=new Ober(1,"test","persoon");
        Consumption consumption = new Consumption(1,"iets",1.0D,-1);
        tafel1.addConsumption(consumption,ober);
        assertTrue(!tafel1.hasOrders());
        Ober vervanger=new Ober(2,"test","persoon");
        Consumption consumptionTest = new Consumption(1,"anders",5.0D,1);
        tafel1.addConsumption(consumptionTest,vervanger);
        assertTrue(tafel1.getOberId()==2);
    }

    @Test
    public void payOrder(){
        hasOrders();
        assertTrue(tafel1.hasOrders());
        tafel1.hasPaid();
        assertFalse(tafel1.hasOrders());
    }

}
