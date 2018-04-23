import be.leerstad.Consumption;
import be.leerstad.Database.OrdersDAOImpl;
import be.leerstad.Order;
import be.leerstad.helpers.DbaseConnection;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DBtest {

    static Connection connection = DbaseConnection.getConnection();

    //sql statements + doorwerp varia

    static final String SQL2 = "DELETE FROM orders\n" + "WHERE ID >= ?;";
    private static String SQL3 = "select auto_increment from information_schema.TABLES where TABLE_SCHEMA = 'chezjava' and TABLE_NAME = 'orders'";

    static OrdersDAOImpl a = new OrdersDAOImpl();
    static int beginnummer;


    List<Consumption> lijst;
    Consumption consumption;





    @Before
    public void setup()
    {

        Order order = new Order();
        consumption = new Consumption(1,"Cola", 2.40, 1 );
        consumption.AddWaiterID(4);
        order.addConsumption(consumption);
        lijst=order.getLijstForPayment();
    }

    @BeforeClass
    public static void getBeginnummer() {
        try ( PreparedStatement pStatement = connection.prepareStatement(SQL3);
              ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                beginnummer = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }



    @Test(timeout = 2000)
    public void writeOrder() {
        a.writeOrder(lijst);
        assertTrue(a.getOrdernummer()==beginnummer+1);
    }

    //geforceerde sql error (waiterid bestaat niet )
    @Test(expected = IllegalArgumentException.class)
    public void writeOrderExeption() {

        consumption.AddWaiterID(50);
        a.writeOrder(lijst);
        assertTrue(a.getOrdernummer()==beginnummer);
    }

@AfterClass
    public static void byeBye(){
    try (PreparedStatement ps = connection.prepareStatement (SQL2)) {
        {
            ps.setInt(1, beginnummer);
            ps.executeUpdate();
        }

    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }
}

}
