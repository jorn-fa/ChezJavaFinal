import be.leerstad.Cafe;
import be.leerstad.Database.WaiterDAOImpl;
import be.leerstad.Ober;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class InlogTest {

    Ober ober;
    Ober vergelijk;


    //rechtstreeks op dao
    @Before
    public void Setup(){
        vergelijk=null;
    }

    @Test
    public void matchingDataWithMatchinCapitals(){
        vergelijk = new Ober(1,"Peters","Wout","password");
        WaiterDAOImpl a = new WaiterDAOImpl();
        ober=a.compareOber(vergelijk);
        assertTrue(ober.getID()==1);
    }


    @Test
    public void matchingDataCaseAlterd(){
        vergelijk = new Ober(1,"peters","wout","password");
        WaiterDAOImpl a = new WaiterDAOImpl();
        ober=a.compareOber(vergelijk);
        assertTrue(ober.getID()==1);

    }

    @Test
    public void noMatchingData()
    {
        vergelijk = new Ober(1,"iemand","anders","password");
        WaiterDAOImpl a = new WaiterDAOImpl();
        ober=a.compareOber(vergelijk);
        assertTrue(ober==null);
    }

    @Test(expected = NullPointerException.class)
    public void ChrashTest()
    {
        vergelijk = new Ober(1,null,null,"password");
        WaiterDAOImpl a = new WaiterDAOImpl();
        ober=a.compareOber(vergelijk);
        assertFalse(ober.getID()==1);
    }

    //via Cafe

    @Test
    public void loginTroughCafe()
    {
    Cafe cafe=new Cafe("cafe");
        vergelijk = new Ober(1,"Peters","Wout","password");
    assertTrue(cafe.inloggen(vergelijk));
    }

    @Test
    public void logoutTroughCafe()
    {
        Cafe cafe=new Cafe("cafe");
        vergelijk = new Ober(1,"Peters","Wout","password");
        assertFalse(cafe.uitloggen());
        assertTrue(cafe.inloggen(vergelijk));
        assertTrue(cafe.uitloggen());
    }

}
