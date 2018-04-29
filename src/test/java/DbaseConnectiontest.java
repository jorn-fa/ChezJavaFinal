import be.leerstad.helpers.DbaseConnection;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DbaseConnectiontest {


    //testen op singleton
    @Test
    public void testCreatie() {
        assertTrue(DbaseConnection.getInstance() != null);
    }

    @Test
    public void testMultipleCreation() {
        assertTrue(DbaseConnection.getInstance() == DbaseConnection.getInstance());
    }


    @Test(timeout = 3000)
    public void isAlive() {
        assertTrue(DbaseConnection.isAlive() == true);
    }

}


