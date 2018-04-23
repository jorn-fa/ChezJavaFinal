import be.leerstad.Consumption;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.text.DecimalFormat;

import static org.junit.Assert.*;



public class ConsumptionTest {

    private Consumption consumptie1, consumptie2;

    @Before
    public void setup()
    {
        consumptie1 = new Consumption(1,"Cola", 2.40, 1 );
        consumptie2 = new Consumption( 2,"Leffe", 3.00, 1);
    }

    @After
    public void tearDown()
    {
        consumptie1 = consumptie2 = null;
    }


    @Test
    public void testEquals()
    {
        assertFalse(consumptie1.equals(null));
        assertFalse(consumptie2.equals(null));
        assertTrue(consumptie1.equals(consumptie1));
        assertTrue(consumptie2.equals(consumptie2));
        assertFalse(consumptie1.equals((consumptie2)));
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Consumption.class)
                .withPrefabValues(Consumption.class, consumptie1,consumptie2)
                //test limiteren wegens geen controle's te voorzien op velden en javaFX onderdelen
                .withNonnullFields("beverageId","naam")
                .withIgnoredFields("prijs", "aantal", "orderNummer", "waiterID","naamProperty")
                .verify();
    }

    @Test
    public void comparing()
    {
        assertTrue((consumptie1.compareTo(consumptie2)<0));
    }

    @Test
    public void getprijs()
    {
        assertNotEquals(3.00,consumptie1.getPrijs(),0);
        assertEquals(2.4,consumptie1.getPrijs(),0);
    }

    @Test
    public void getid()
    {
        assertEquals(1,consumptie1.getBeverageId());
        assertEquals(2,consumptie2.getBeverageId());
    }

    @Test
    public void getnaam()
    {
        assertEquals("cola",consumptie1.getNaam().toLowerCase());
        assertEquals("leffe",consumptie2.getNaam().toLowerCase());
    }

    @Test
    public void hash()
    {
        assertEquals(2106144,consumptie1.hashCode());
        assertEquals(73297834,consumptie2.hashCode());
    }

    @Test
    public void tostring()
    {
        DecimalFormat df = new DecimalFormat("#0.00");
        // decimale seperator ophalen
        char a = df.getDecimalFormatSymbols().getDecimalSeparator();
        assertEquals("Consumption{naam='Cola'; prijs= 2"+a+"40; aantal=1}",consumptie1.toString());
        assertEquals("Consumption{naam='Leffe'; prijs= 3"+a+"00; aantal=1}",consumptie2.toString());
    }

}
