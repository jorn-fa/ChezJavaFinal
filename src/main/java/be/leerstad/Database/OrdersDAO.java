package be.leerstad.Database;


import be.leerstad.Consumption;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface OrdersDAO extends Serializable{


    void writeOrder(List<Consumption> temp);

    int getOrdernummer();
    List<Consumption>orderList();
    Set<Date> dateList();

}
