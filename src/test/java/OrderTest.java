import be.leerstad.Consumption;
import be.leerstad.Order;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OrderTest {

    Order order = new Order();
    Consumption een = new Consumption(1,"Cola", 2.40, 1 );
    Consumption twee = new Consumption(1,"Leffe", 2.40, 1 );
    Consumption drie = new Consumption(1,"Leffe", 2.40, -1 );


    @Test
    public void doesExist()
    {
        assertTrue(order!=null);
    }

    @Test
    public void hasNoOrder()
    {
        assertTrue(order.getTotalPrice()==0);
    }

    @Test
    public void addOrder()
    {
        assertTrue(order.getTotalPrice()==0);
        order.addConsumption(een);
        assertTrue(order.getTotalPrice()==2.4d);
    }

    @Test
    public void subtractBeverageFromOrder()
    {
        assertTrue(order.getTotalPrice()==0);
        order.addConsumption(een);
        order.addConsumption(twee);
        assertTrue(order.getTotalPrice()==2.4d*2);
        order.addConsumption(een);
        assertTrue(order.getTotalPrice()==2.4d*3);
        order.addConsumption(drie);
        assertTrue(order.getTotalPrice()==2.4d*2);
    }


}
