import be.leerstad.Ober;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class OberTest {

    private Ober ober1, ober2;

    @Before
    public void setup()
    {
    ober1 = new Ober(1,"Peters","Wout");
    ober2 = new Ober( 2,"Segers","Nathalie");
    }

    @After
    public void tearDown()
    {
        ober1 = ober2 = null;
    }


    @Test
    public void testEquals()
    {
        assertFalse(ober1.equals(null));
        assertFalse(ober2.equals(null));
        assertTrue(ober1.equals(ober1));
        assertFalse(ober1.equals((ober2)));
    }


    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Ober.class)
                .withPrefabValues(Ober.class, new Ober(1,"test1","test2"), new Ober(1,"test2","test2"))
                //test limiteren wegens geen controle's te voorzien op velden
                .withNonnullFields("voornaam","naam","oberID")
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void hash()
    {
       assertEquals(990348337,ober1.hashCode());
        assertEquals(1451973407,ober2.hashCode());
    }

    @Test
    public void tostring()
    {
        assertEquals("Ober{naam='Peters', voornaam='Wout'}",ober1.toString());
    }


    @Test
    public void getNaam()
    {
        assertEquals("Peters",ober1.getNaam());
        assertEquals("Segers",ober2.getNaam());
    }

    @Test
    public void getVoornaam()
    {
        assertEquals("Wout",ober1.getVoornaam());
        assertEquals("Nathalie",ober2.getVoornaam());
    }

    @Test
    public void getID()
    {
        assertEquals(1,ober1.getID());
        assertEquals(2,ober2.getID());
    }

    @Test
    public void comparing()
    {
        assertTrue((ober1.compareTo(ober2)<0));
    }







}
