package be.leerstad.Database;


import be.leerstad.Consumption;

import java.io.Serializable;
import java.util.List;

public interface OrdersDAO extends Serializable{


    void writeOrder(List<Consumption> temp);

    int getOrdernummer();
    List<Consumption>orderList();

}
