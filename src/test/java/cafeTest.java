import be.leerstad.Cafe;
import be.leerstad.Ober;
import be.leerstad.Tafel;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.Collections;

import static org.junit.Assert.*;

public class cafeTest {

    Ober ober = new Ober (1,"peters","wout");
    private Cafe cafe;

    @Before
    public void setUp() {
        cafe = new Cafe("1");
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


}
