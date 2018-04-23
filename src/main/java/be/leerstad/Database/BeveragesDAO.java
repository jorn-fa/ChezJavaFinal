package be.leerstad.Database;

import be.leerstad.Consumption;

import java.util.HashMap;
import java.util.List;

public interface BeveragesDAO {


    HashMap priceList();
    List<Consumption> pricelijst();

}
