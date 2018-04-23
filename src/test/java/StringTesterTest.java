import be.leerstad.StringTester;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class StringTesterTest {

    private String eerste="test";
    private String tweede="tegroot";


    @Test (expected = IllegalArgumentException.class)
    public void Lengtetest()
    {
        assertTrue(StringTester.opLengte(eerste,5));
        assertFalse(StringTester.opLengte(tweede,-1));
        assertTrue(StringTester.opLengte(tweede,50));

    }


}
