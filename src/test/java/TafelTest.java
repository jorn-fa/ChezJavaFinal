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
    public void tostring()
    {
        assertEquals("Tafel{tafel een}",tafel1.toString());
        assertEquals("Tafel{tafel twee}",tafel2.toString());
    }

    @Test
    public void compareto()
    {
        assertTrue(tafel1.compareTo(tafel2)<0);
    }

    @Test
    public void getnaam()
    {
        assertEquals("tafel een",tafel1.getNaam().toLowerCase());
        assertNotEquals("tafel twee",tafel2.getNaam());
    }

    @Test
    public void hasOrders()
    {
        Ober ober=new Ober(1,"test","persoon");
        assertTrue(tafel1.hasOrders()==false);
        Consumption consumption = new Consumption(1,"iets",1.0D,1);
        tafel1.addConsumption(consumption,ober);
        assertTrue(tafel1.hasOrders()==true);
    }
}
