import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({

        //logwisser weghalen -> naam zorgt ervoor dat hij eerst wordt uitgevoerd

        _1_Logtest.class,
        DbaseConnectiontest.class,
        ConsumptionTest.class,
        OberTest.class,
        StringTesterTest.class,
        TafelTest.class,
        dbasePropertiesTest.class,
        DBtest.class,
        BeveragesDaoTest.class,
        SerialTest.class,
        DeserialTest.class


        })

public class AlleTesten {



}
