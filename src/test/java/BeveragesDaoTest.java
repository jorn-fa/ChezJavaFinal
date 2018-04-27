import be.leerstad.Cafe;
import be.leerstad.Database.BeveragesDAOImpl;
import be.leerstad.Ober;
import be.leerstad.helpers.DbaseConnection;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class BeveragesDaoTest {

    private static int maxNumber;

    @BeforeClass
    public static void setup()
    {
        Connection connection= DbaseConnection.getConnection();
        try (
                PreparedStatement pStatement = connection.prepareStatement("select count(price) from beverages");
                ResultSet resultSet = pStatement.executeQuery()) {

            while (resultSet.next()) {
                maxNumber = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("check sql");
        }

    }

    @Test (timeout = 2000)
    public void opvragenList()
    {
        BeveragesDAOImpl a = new BeveragesDAOImpl();
        assertTrue(a.pricelijst().size()== maxNumber);

    }

    @Test(expected = AssertionError.class)
    public void troughCafeWithError()
    {
        //lijst mag niet bestaan voor allereerste login
        Cafe cafe = new Cafe("test");
        assertTrue(cafe.fillBeverageList()==true);
    }


    @Test(timeout = 2000)
    public void troughCafe(){
        Cafe cafe = new Cafe("twee");
        cafe.uitloggen();
        Ober vergelijk = new Ober(1,"peters","wout","password");
        cafe.inloggen(vergelijk);
        assertTrue(cafe.fillBeverageList()==true);
    }

    @Test (expected = AssertionError.class)
    public void troughCafeWithLogoff(){Cafe cafe = new Cafe("test");
        Ober vergelijk = new Ober(1,"peters","wout","password");
        cafe.inloggen(vergelijk);
        assertTrue(cafe.fillBeverageList()==true);
        cafe.uitloggen();
        assertTrue(cafe.fillBeverageList()==true);
    }

}
