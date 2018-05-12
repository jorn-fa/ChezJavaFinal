import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({


        Logtest.class,
        DbaseConnectiontest.class,
        ConsumptionTest.class,
        OberTest.class,
        TafelTest.class,
        DbasePropertiesTest.class,
        DBtest.class,
        BeveragesDaoTest.class,
        SerialTest.class,
        DeserialTest.class,
        CafeTest.class,
        PdfFactoryTest.class,
        })

public class AlleTesten {



}
